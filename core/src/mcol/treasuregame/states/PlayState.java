package mcol.treasuregame.states;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector3;
import mcol.treasuregame.TreasureGame;
import mcol.treasuregame.entities.EntityManager;
import mcol.treasuregame.entities.Map;
import mcol.treasuregame.entities.SystemManager;
import mcol.treasuregame.entities.components.CameraComponent;
import mcol.treasuregame.entities.components.Collectable;
import mcol.treasuregame.entities.components.HUDComponent;
import mcol.treasuregame.entities.components.Position;
import mcol.treasuregame.entities.systems.NavigationSystem;
import mcol.treasuregame.entities.systems.RenderSystem;
import mcol.treasuregame.gfx.HUD;
import mcol.treasuregame.gfx.MiniMap;

import java.util.List;

public class PlayState extends State {

    /** Current level. */
    private final int level;

    /** The world map. */
    private Map map;

    /** Heads-up display. */
    private HUD hud;

    /** Work vector. */
    private final Vector3 coords = new Vector3();

    private EntityManager entityManager;
    private HUDComponent hudComponent;
    private Vector3 playerPosition;
    private long playerEntity;

    /** System manager. */
    private SystemManager systemManager;
    private NavigationSystem navigationSystem;

    /** Rendering system. */
    private RenderSystem renderSystem;

    /** Constructor. */
    public PlayState(TreasureGame game, Batch sb) {
        super(game, sb);
        level = 0;
        initializeWorld(level);
        setupMultiplexer();
    }

    /** Initializes the world. */
    private void initializeWorld(int level) {
        map = new Map(sb, camera, level);
        entityManager = new EntityManager();

        // create the player
        playerEntity = entityManager.getFactory().createPlayer(12, 12, 0);
        entityManager.add(playerEntity, new CameraComponent(camera));
        playerPosition = entityManager.getComponent(playerEntity, Position.class).getCurrent();
        entityManager.getFactory().createTargetIndicator();

        // create a hud component
        hudComponent = new HUDComponent();
        entityManager.add(entityManager.createEntity(), hudComponent);
        hud = new HUD(sb, new MiniMap(map, entityManager), hudComponent);

        // create entities for items
        for (MapObject obj : map.getObjects())
            entityManager.getFactory().createItem(obj.getProperties());

        systemManager = new SystemManager(entityManager, map);
        navigationSystem = new NavigationSystem(map);
        renderSystem = new RenderSystem(entityManager, map);
    }

    /** Sets up the input processors. */
    private void setupMultiplexer() {
        InputMultiplexer multiplexer = new InputMultiplexer();
        multiplexer.addProcessor(hud.getStage());
        multiplexer.addProcessor(this);
        Gdx.input.setInputProcessor(multiplexer);
    }

    /** Handles the input. */
    private void handleInput() {
        if (Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE))
            game.setScreen(new MenuState(game, sb));
    }

    /** Releases a bomb. */
    private void releaseBomb() {
        entityManager.getFactory().createBomb(playerPosition.x, playerPosition.y);
        hudComponent.setBombs(hudComponent.getBombs() - 1);
    }

    /** Releases a hurricane. */
    private void releaseHurricane() {
        entityManager.getFactory().createHurricane(playerPosition.x, playerPosition.y);
        hudComponent.setHurricanes(hudComponent.getHurricanes() - 1);
    }

    /** Releases a lamb. */
    private void releaseLamb() {
        List<Long> collectables = entityManager.getEntitiesWith(Collectable.class);
        while (collectables.size() > 0) {
            long entity = collectables.remove(MathUtils.random(collectables.size() - 1));
            Vector3 target = entityManager.getComponent(entity, Position.class).getCurrent();
            if (map.hasFog(target.x, target.y)) {
                long lambEntity = entityManager.getFactory().createLamb(playerPosition.x, playerPosition.y);
                navigationSystem.setTarget(entityManager, lambEntity, target);
                hudComponent.setLambs(hudComponent.getLambs() - 1);
                break;
            }
        }
    }

    @Override
    protected void update(float dt) {
        handleInput();
        hud.update(dt);
        if (hud.isHoldingBomb())
            releaseBomb();
        if (hud.isHoldingHurricane())
            releaseHurricane();
        if (hud.isHoldingLamb())
            releaseLamb();

        systemManager.update(dt);
    }

    @Override
    public void render(float delta) {
        super.render(delta);
        map.renderBackgroundLayers();
        renderSystem.render(sb);
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
        coords.set(screenX, screenY, 0f);
        navigationSystem.setTarget(entityManager, playerEntity, viewport.unproject(coords));
        return false;
    }
}
