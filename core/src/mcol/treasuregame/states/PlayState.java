package mcol.treasuregame.states;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.MapObjects;
import com.badlogic.gdx.maps.MapProperties;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector3;
import mcol.treasuregame.TreasureGame;
import mcol.treasuregame.assets.Map;
import mcol.treasuregame.assets.creatures.Creature;
import mcol.treasuregame.assets.creatures.Lamb;
import mcol.treasuregame.assets.creatures.Player;
import mcol.treasuregame.assets.items.*;
import mcol.treasuregame.gfx.CameraShake;
import mcol.treasuregame.gfx.HUD;
import mcol.treasuregame.gfx.MessageManager;
import mcol.treasuregame.utils.Utils;

import java.util.ArrayList;

public class PlayState extends State {

    /** Player object. */
    private final Player player;

    /** Current level. */
    private final int level;

    /** The target indicator. */
    private final TargetIndicator targetIndicator;

    /** The world map. */
    private Map map;

    /** Heads-up display. */
    private HUD hud;

    /** Height of the world in world units. */
    private int worldWidth;

    /** Width of the world in world units. */
    private int worldHeight;

    /** Container for all items. */
    private ArrayList<Item> items;

    /** Container for all creatures. */
    private ArrayList<Creature> creatures;

    /** Container for all messages. */
    private static MessageManager messageManager;

    /** Constructor. */
    public PlayState(TreasureGame game, SpriteBatch sb) {
        super(game, sb);
        player = new Player(13, 13, 0);
        targetIndicator = new TargetIndicator(player);
        level = 0;
        initializeWorld(level);
        setupMultiplexer();
    }

    /** Initializes the world. */
    private void initializeWorld(int level) {
        map = new Map(sb, level);
        worldWidth = map.getHeight();
        worldHeight = map.getHeight();
        items = new ArrayList<>();
        creatures = new ArrayList<>();
        creatures.add(player);
        messageManager = new MessageManager();
        hud = new HUD(sb, map, player);
        placeItems();
    }

    /** Sets up the input processors. */
    private void setupMultiplexer() {
        InputMultiplexer multiplexer = new InputMultiplexer();
        multiplexer.addProcessor(hud.getStage());
        multiplexer.addProcessor(this);
        Gdx.input.setInputProcessor(multiplexer);
    }

    /** Extracts the items from the layer and puts them on the map. */
    private void placeItems() {
        MapObjects objects = map.getObjects();
        for (MapObject obj : objects) {
            MapProperties mp = obj.getProperties();
            int x = mp.get("x", float.class).intValue() / TreasureGame.SCALE;
            int y = mp.get("y", float.class).intValue() / TreasureGame.SCALE;
            String type = mp.get("type", String.class);
            try {
                switch (type) {
                    case "sweet":
                        items.add(new Sweet(x, y, mp.get("value", int.class)));
                        break;
                    case "bomb":
                        items.add(new Bomb(x, y));
                        break;
                    case "hurricane":
                        items.add(new Hurricane(x, y));
                }
            } catch (Exception e) {
                e.printStackTrace();
                Gdx.app.log("getItems", x + " " + y);
            }
        }
    }

    /** Handles the input. */
    private void handleInput() {
        if (Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE))
            game.setScreen(new MenuState(game, sb));
    }

    /** Moves the camera so that the player is at the centre. */
    private void centreCameraOnPlayer(float dt) {

        // position the camera is aiming to move to
        Vector3 pos = player.getPosition().cpy();

        // move gradually towards the target
        pos = limitCamera(pos);
        float tween = 4f;
        float dist = pos.dst(camera.position);
        if (dist < 1 / tween) {
            tween = Utils.clampValue(1 / dist, tween, 10);
        }
        camera.position.lerp(pos, tween * dt);
        camera.update();
    }

    /** Checks the map bounds to avoid showing blank space outside of the map. */
    private Vector3 limitCamera(Vector3 pos) {
        pos.x = MathUtils.clamp(pos.x, camera.viewportWidth / 2 * camera.zoom,
                                worldHeight - camera.viewportWidth / 2 * camera.zoom);
        pos.y = MathUtils.clamp(pos.y, camera.viewportHeight / 2 * camera.zoom,
                                worldWidth - camera.viewportHeight / 2 * camera.zoom);
        return pos;
    }

    /** Checks for collisions between player and items. */
    private void checkCollisionWithItems() {
        for (int i = 0; i < items.size(); i++) {
            Item item = items.get(i);
            if (item.getBounds().overlaps(player.getBounds())) {
                if (item instanceof ArmedBomb ||
                    item instanceof Explosion ||
                    item instanceof MovingHurricane) {
                    continue;
                }
                int value = item.getValue();
                if (value > 0) {
                    player.addScore(value);
                    messageManager.addMessage(Integer.toString(value),
                                              item.getX(), item.getY());
                }
                if (item instanceof Hurricane) {
                    items.add(new MovingHurricane(item.getX(), item.getY(), map));
                }
                else if (item instanceof Bomb) {
                    player.addBomb();
                    messageManager.addMessage("+1 bomb",
                                              item.getX(), item.getY());
                }
                items.remove(i);
                break;
            }
        }
    }

    /** Drops a bomb. */
    private void dropBomb() {
        items.add(new ArmedBomb(player.getX(), player.getY()));
        player.removeBomb();
        hud.resetCheckedButton();
    }

    /** Explodes the bomb. */
    private void explodeBomb(Item item) {
        int radius = 3;
        float tx = item.getX();
        float ty = item.getY();
        items.add(new Explosion(tx, ty, radius));
        map.destroyFog(Utils.toTile(tx), Utils.toTile(ty), radius);
        map.destroyObstacles(Utils.toTile(tx), Utils.toTile(ty), radius);
    }

    /** Releases a lamb. */
    private void releaseLamb() {
        float maxDist = 0, dist;
        float x = player.getX(), y = player.getY();
        float tx = 0, ty = 0, tempx, tempy;
        for (Item item : items) {
            if (item instanceof AnimatedItem)
                continue;
            tempx = item.getX();
            tempy = item.getY();
            if (!map.hasFog(Utils.toTile(tempx), Utils.toTile(tempy)))
                continue;
            dist = Math.abs(tempx - x) + Math.abs(tempy - y);
            if (dist > maxDist) {
                maxDist = dist;
                tx = tempx;
                ty = tempy;
            }
        }
        creatures.add(new Lamb(x, y, tx, ty, map));
        player.removeLamb();
        hud.resetCheckedButton();
    }

    @Override
    protected void update(float dt) {
        handleInput();
        if (hud.isHoldingBomb())
            dropBomb();
        if (hud.isHoldingLamb())
            releaseLamb();

        // update all creatures
        for (int i = 0; i < creatures.size(); i++) {
            Creature creature = creatures.get(i);
            creature.update(dt);
            int tx = Utils.toTile(creature.getPosition().x);
            int ty = Utils.toTile(creature.getPosition().y);
            map.destroyFog(tx, ty, creature.getFogRadius());
            if (creature.shouldRemove())
                creatures.remove(i);
        }

        // check for collisions between player and items
        checkCollisionWithItems();

        // update all items
        for (int i = 0; i < items.size(); i++) {
            Item item = items.get(i);
            item.update(dt);
            if (item.shouldRemove()) {
                items.remove(i);
                if (item instanceof ArmedBomb)
                    explodeBomb(item);
            }
        }

        targetIndicator.update(dt);
        messageManager.update(dt);
        centreCameraOnPlayer(dt);
        if (CameraShake.isShaking())
            camera.translate(CameraShake.update(dt));

        hud.setScore();
    }

    @Override
    public void render(float delta) {
        super.render(delta);

        map.setView(camera);
        map.renderBackgroundLayers();
        sb.begin();
        for (Creature creature : creatures)
            creature.render(sb);
        for (Item item : items)
            item.render(sb);
        map.renderFog(sb);
        messageManager.render(sb);
        targetIndicator.render(sb);
        sb.end();

        hud.render();
    }

    @Override
    public void resize(int width, int height) {
        super.resize(width, height);
        hud.resize(width, height);
    }

    // InputProcessor interface

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        Vector3 coords = new Vector3(screenX, screenY, 0);
        viewport.unproject(coords);

        // transform screen coordinates to world coordinates
        player.findPathToTarget(map, coords);
        return false;
    }
}
