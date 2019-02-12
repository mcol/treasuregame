package mcol.treasuregame.assets;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.MapObjects;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapRenderer;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import mcol.treasuregame.TreasureGame;
import mcol.treasuregame.utils.Utils;

import java.util.Locale;

public class Map {

    /** Identifier of the tile used for fog. */
    private static final int FOG_TILE_ID = 35;

    /** Name of the obstacles layer. */
    private static final String OBS_LAYER_NAME = "Obs";

    /** The whole map. */
    private final TiledMap map;

    /** The copy of the obstacles layer. */
    private TiledMapTileLayer obs;

    /** The fog layer. */
    private final boolean[] fog;

    /** The map renderer. */
    private final TiledMapRenderer mapRenderer;

    /** Layers to be drawn in the background. */
    private final int[] backLayers;

    /** The texture used to render the fog. */
    private final TextureRegion fogTexture;

    /** Width of the map in tiles. */
    private final int width;

    /** Height of the map in tiles. */
    private final int height;

    /** Whether the fog should be rendered. */
    private boolean showFog;

    /** Constructor. */
    public Map(Batch sb, int level) {
        map = Assets.get(String.format(Locale.US, "maps/%02d.tmx", level), TiledMap.class);
        mapRenderer = new OrthogonalTiledMapRenderer(map, TreasureGame.SCALE, sb);
        TiledMapTileLayer layer = (TiledMapTileLayer) map.getLayers().get(0);
        width = layer.getWidth();
        height = layer.getHeight();
        fog = new boolean[width * height];
        fogTexture = map.getTileSets().getTileSet(0).getTile(FOG_TILE_ID).getTextureRegion();
        showFog = true;
        backLayers = new int[] {0, 1};
        generateMap();
    }

    /** Generates the map for this level. */
    private void generateMap() {
        copyObstaclesLayer();

        // initialise the fog layer
        for (int i = 0, end = width * height; i < end; i++)
            fog[i] = true;
    }

    /** Creates a copy of the obstacles layer. */
    private void copyObstaclesLayer() {

        // remove layer if already there
        int id = map.getLayers().getIndex(OBS_LAYER_NAME);
        if (id > 0)
            map.getLayers().remove(id);

        // create the new layer
        TiledMapTileLayer obsOrig = (TiledMapTileLayer) map.getLayers().get("Obstacles");
        final int tileSize = TreasureGame.TILESIZE_PIXELS;
        obs = new TiledMapTileLayer(width, height, tileSize, tileSize);
        obs.setName(OBS_LAYER_NAME);

        // copy the original layer
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++)
                obs.setCell(i, j, obsOrig.getCell(i, j));
        }
        map.getLayers().add(obs);
        backLayers[1] = map.getLayers().getIndex(OBS_LAYER_NAME);
    }

    /** Removes the fog tiles in a radius around a point. */
    public void destroyFog(float x, float y, int radius) {
        int tx = Utils.toTile(x);
        int ty = Utils.toTile(y);
        int xmin = Math.max(tx - radius, 0);
        int ymin = Math.max(ty - radius, 0) * width;
        int xmax = Math.min(tx + radius + 1, width);
        int ymax = Math.min(ty + radius + 1, height) * width;
        for (int j = ymin; j < ymax; j += width)
            for (int i = xmin; i < xmax; i++)
                fog[i + j] = false;
    }

    /** Removes the obstacle tiles in a radius around a point. */
    public void destroyObstacles(float x, float y, int radius) {
        int tx = Utils.toTile(x) - radius;
        int ty = Utils.toTile(y) - radius;
        int diameter = radius * 2 + 1;
        for (int j = 0, end = diameter * diameter; j < end; j++)
            obs.setCell(tx + j % diameter, ty + j / diameter, null);
    }

    /** Toggles visibility of the fog layer. */
    public void toggleFogLayer() {
        showFog = !showFog;
    }

    /** Sets the projection matrix from the given camera. */
    public void setView(OrthographicCamera camera) {
        mapRenderer.setView(camera);
    }

    /** Renders the background layers. */
    public void renderBackgroundLayers() {
        mapRenderer.render(backLayers);
    }

    /** Renders the fog layer. */
    public void renderFog(Batch sb) {
        for (int j = showFog ? 0 : fog.length; j < fog.length; j++) {
            if (fog[j])
                sb.draw(fogTexture, j % width * TreasureGame.TILESIZE, j / width * TreasureGame.TILESIZE,
                        TreasureGame.TILESIZE, TreasureGame.TILESIZE);
        }
    }

    // getters and setters

    /** Returns the map objects. */
    public MapObjects getObjects() {
        return map.getLayers().get("Objects").getObjects();
    }

    /** Returns whether there is an obstacle at the given tile coordinates. */
    public boolean hasObstacle(int x, int y) {
        return obs.getCell(x, y) != null;
    }

    /** Returns whether there is fog at the given tile coordinates. */
    public boolean hasFog(int x, int y) {
        if (x < 0 || y < 0 || x >= width || y >= height)
            return false;
        return fog[x + y * width];
    }

    /** Returns the width of the map in world units. */
    public float getWidth() {
        return width * TreasureGame.TILESIZE;
    }

    /** Returns the height of the map in world units. */
    public float getHeight() {
        return height * TreasureGame.TILESIZE;
    }
}
