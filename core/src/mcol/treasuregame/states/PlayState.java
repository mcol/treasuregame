package mcol.treasuregame.states;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.MapObjects;
import com.badlogic.gdx.math.Vector3;
import mcol.treasuregame.TreasureGame;
import mcol.treasuregame.assets.ItemFactory;
import mcol.treasuregame.assets.ItemManager;
import mcol.treasuregame.assets.Map;
import mcol.treasuregame.assets.creatures.Creature;
import mcol.treasuregame.assets.creatures.Lamb;
import mcol.treasuregame.assets.creatures.Player;
import mcol.treasuregame.assets.items.ArmedBomb;
import mcol.treasuregame.assets.items.Item;
import mcol.treasuregame.assets.items.MovingHurricane;
import mcol.treasuregame.assets.items.TargetIndicator;
import mcol.treasuregame.gfx.HUD;

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

    /** Container for all items. */
    private ItemManager itemManager;

    /** Container for all creatures. */
    private ArrayList<Creature> creatures;

    /** Constructor. */
    public PlayState(TreasureGame game, Batch sb) {
        super(game, sb);
        player = new Player(12, 12, 0);
        targetIndicator = new TargetIndicator(player);
        level = 0;
        initializeWorld(level);
        setupMultiplexer();
    }

    /** Initializes the world. */
    private void initializeWorld(int level) {
        map = new Map(sb, level);
        camera.setWorldSize(map.getWidth(), map.getHeight());
        itemManager = new ItemManager();
        creatures = new ArrayList<>();
        creatures.add(player);
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
            Item item = ItemFactory.createItem(obj.getProperties());
            if (item != null)
                itemManager.add(item);
        }
    }

    /** Handles the input. */
    private void handleInput() {
        if (Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE))
            game.setScreen(new MenuState(game, sb));
    }

    /** Releases a bomb. */
    private void releaseBomb() {
        itemManager.add(new ArmedBomb(player.getX(), player.getY()));
        player.removeBomb();
    }

    /** Releases a hurricane. */
    private void releaseHurricane() {
        itemManager.add(new MovingHurricane(player.getX(), player.getY()));
        player.removeHurricane();
    }

    /** Releases a lamb. */
    private void releaseLamb() {
        Lamb lamb = new Lamb(player.getX(), player.getY(), map);
        lamb.setTarget(itemManager);
        creatures.add(lamb);
        player.removeLamb();
    }

    @Override
    protected void update(float dt) {
        handleInput();
        hud.update();
        if (hud.isHoldingBomb())
            releaseBomb();
        if (hud.isHoldingHurricane())
            releaseHurricane();
        if (hud.isHoldingLamb())
            releaseLamb();

        // update all creatures
        for (int i = 0; i < creatures.size(); i++) {
            Creature creature = creatures.get(i);
            creature.update(dt);
            creature.destroy(map);
            if (creature.shouldRemove())
                creatures.remove(i);
        }

        // update all items
        itemManager.checkCollisions(player);
        itemManager.update(dt);
        itemManager.destroy(map);

        camera.update(dt, player.getPosition());
        targetIndicator.update(dt);
    }

    @Override
    public void render(float delta) {
        super.render(delta);

        map.setView(camera);
        map.renderBackgroundLayers();
        sb.begin();
        for (Creature creature : creatures)
            creature.render(sb);
        itemManager.render(sb);
        map.renderFog(sb);
        player.renderMessages(sb);
        targetIndicator.render(sb);
        sb.end();

        hud.render();
    }

    @Override
    public void resize(int width, int height) {
        super.resize(width, height);
        camera.resize(width, height);
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
