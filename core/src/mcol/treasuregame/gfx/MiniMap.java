package mcol.treasuregame.gfx;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Pixmap.Format;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.FrameBuffer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import mcol.treasuregame.assets.ItemManager;
import mcol.treasuregame.assets.Map;
import mcol.treasuregame.assets.creatures.Creature;
import mcol.treasuregame.assets.creatures.Player;
import mcol.treasuregame.assets.items.CollectableItem;
import mcol.treasuregame.assets.items.Item;
import mcol.treasuregame.utils.Utils;

import java.util.List;

public class MiniMap extends Image {

    /** Size of the minimap in pixels. */
    private static final int SIZE_PIXELS = 100;

    /** Number of tiles visible in the minimap. */
    private static final int TILES = SIZE_PIXELS / 2;

    /** The world map. */
    private final Map map;

    /** The player object. */
    private final Player player;

    /** Container for all items. */
    private final ItemManager items;

    /** The list of creatures. */
    private final List<Creature> creatures;

    /** Width of the world map in tiles. */
    private int mapWidth;

    /** Height of the world map in tiles. */
    private int mapHeight;

    /** The blinker object. */
    private Blinker blinker;

    /** The minimap renderer. */
    private ShapeRenderer renderer;

    /** Frame buffer object. */
    private FrameBuffer fb;

    /** Constructor. */
    public MiniMap(Map map, Player player, ItemManager items, List<Creature> creatures) {
        this.map = map;
        this.player = player;
        this.items = items;
        this.creatures = creatures;
        initialize();
    }

    /** Set up the minimap. */
    private void initialize() {
        mapWidth = Utils.toTile(map.getWidth());
        mapHeight = Utils.toTile(map.getHeight());
        blinker = new Blinker(0.5f);
        renderer = new ShapeRenderer();
        renderer.scale((float) Gdx.graphics.getWidth() / TILES,
                       (float) Gdx.graphics.getHeight() / TILES, 0);
        fb = new FrameBuffer(Format.RGB565, Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), false);
        TextureRegion rr = new TextureRegion(fb.getColorBufferTexture());
        rr.flip(false, true);
        setDrawable(new TextureRegionDrawable(rr));
        getDrawable().setMinWidth(SIZE_PIXELS);
        getDrawable().setMinHeight(SIZE_PIXELS);
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        batch.flush();
        fb.begin();
        Gdx.graphics.getGL20().glClear(GL20.GL_COLOR_BUFFER_BIT);
        render(items);
        fb.end();

        batch.end();
        batch.begin();
        super.draw(batch, parentAlpha);
    }

    public void update(float dt) {
        blinker.update(dt);
    }

    private void render(ItemManager items) {

        // background
        renderer.begin(ShapeType.Filled);

        // border
        renderer.setColor(Color.BLACK);
        renderer.rect(0, 0, TILES, TILES);

        int px = Utils.toTile(player.getX());
        int py = Utils.toTile(player.getY());
        int xmin = MathUtils.clamp(px - TILES / 2, -1, mapWidth - TILES + 1);
        int xmax = xmin + TILES;
        int ymin = MathUtils.clamp(py - TILES / 2, -1, mapHeight - TILES + 1);
        int ymax = ymin + TILES;

        // background
        int bx = xmin == -1 ? 1 : xmax == mapWidth + 1 ? -1 : 0;
        int by = ymin == -1 ? 1 : ymax == mapHeight + 1 ? -1 : 0;
        renderer.setColor(Color.LIGHT_GRAY);
        renderer.rect(bx, by, TILES, TILES);

        // obstacles
        renderer.setColor(Color.BROWN);
        for (int x = xmin; x <= xmax; x++) {
            for (int y = ymin; y <= ymax; y++) {
                if (map.hasObstacle(x, y)) {
                    renderer.rect(x - xmin, y - ymin, 1, 1);
                }
            }
        }

        // fog
        renderer.setColor(Color.SLATE);
        for (int x = xmin; x <= xmax; x++) {
            for (int y = ymin; y <= ymax; y++) {
                if (map.hasFog(x, y))
                    renderer.rect(x - xmin, y - ymin, 1, 1);
            }
        }

        // creatures
        for (Creature creature : creatures) {
            renderer.setColor(creature instanceof Player ? Color.RED : Color.WHITE);
            renderer.circle(Utils.toTile(creature.getX()) - xmin,
                            Utils.toTile(creature.getY()) - ymin,
                            creature instanceof Player ? 2 : 1);
        }

        // items
        if (blinker.isVisible()) {
            for (Item item : items.getItems()) {
                renderer.setColor(item instanceof CollectableItem ? Color.GOLD : Color.BLUE);
                int tx = Utils.toTile(item.getX());
                int ty = Utils.toTile(item.getY());
                if (!map.hasFog(tx, ty))
                    renderer.circle(tx - xmin, ty - ymin, 1.5f);

            }
        }

        renderer.end();
    }
}
