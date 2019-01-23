package mcol.treasuregame.assets;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.MapObjects;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapRenderer;
import com.badlogic.gdx.maps.tiled.TiledMapTile;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer.Cell;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import mcol.treasuregame.TreasureGame;

public class Map {

    /** Size of a tile in pixels. */
    private static final int TILESIZE = 32;

    /** Identifier of the tile used for fog. */
    private static final int FOG_TILE_ID = 35;

    /** Name of the fog layer. */
    private static final String FOG_LAYER_NAME = "Fog";

    /** Name of the obstacles layer. */
    private static final String OBS_LAYER_NAME = "Obs";

    /** The whole map. */
    TiledMap map;

    /** The copy of the obstacles layer. */
    TiledMapTileLayer obs;

    /** The fog layer. */
    TiledMapTileLayer fog;

    /** The map renderer. */
    private final TiledMapRenderer mapRenderer;

    /** Layers to be drawn in the background. */
    private final int[] backLayers;

    /** Layers to be drawn in the foreground. */
    private final int[] foreLayers;

    /** Width of the map in tiles. */
    private final int width;

    /** Height of the map in tiles. */
    private final int height;

    /** Constructor. */
    public Map(SpriteBatch sb, int level) {
        map = Assets.get(String.format("maps/%02d.tmx", level), TiledMap.class);
        mapRenderer = new OrthogonalTiledMapRenderer(map, (float) 1 / TreasureGame.SCALE, sb);
        TiledMapTileLayer layer = (TiledMapTileLayer) map.getLayers().get(0);
        width = layer.getWidth();
        height = layer.getHeight();
        backLayers = new int[] {0, 1};
        foreLayers = new int[] {-1};
        generateMap();
    }

    /** Generates the map for this level. */
    private void generateMap() {
        copyObstaclesLayer();
        createFogLayer();
        backLayers[1] = map.getLayers().getIndex(OBS_LAYER_NAME);
        foreLayers[0] = map.getLayers().getIndex(FOG_LAYER_NAME);
    }

    /** Creates a copy of the obstacles layer. */
    private void copyObstaclesLayer() {

        // remove layer if already there
        int id = map.getLayers().getIndex(OBS_LAYER_NAME);
        if (id > 0)
            map.getLayers().remove(id);

        // create the new layer
        TiledMapTileLayer obsOrig = (TiledMapTileLayer) map.getLayers().get("Obstacles");
        obs = new TiledMapTileLayer(width, height, TILESIZE, TILESIZE);
        obs.setName(OBS_LAYER_NAME);

        // copy the original layer
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++)
                obs.setCell(i, j, obsOrig.getCell(i, j));
        }
        map.getLayers().add(obs);
    }

    /** Creates the fog layer. */
    private void createFogLayer() {

        // remove layer if already there
        int id = map.getLayers().getIndex(FOG_LAYER_NAME);
        if (id > 0)
            map.getLayers().remove(id);

        // create new layer
        fog = new TiledMapTileLayer(width, height, TILESIZE, TILESIZE);
        fog.setName(FOG_LAYER_NAME);

        // get the tile corresponding to fog
        TiledMapTile fogTile = map.getTileSets().getTileSet(0).getTile(FOG_TILE_ID);
        Cell fogCell = new Cell().setTile(fogTile);

        // fill the layer with the fog tile
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++)
                fog.setCell(i, j, fogCell);
        }
        map.getLayers().add(fog);
    }

    /** Removes the fog tiles in a radius around a point. */
    public void destroyFog(int tx, int ty, int radius) {
        for (int j = 0; j < radius * radius; j++)
            fog.setCell(tx + j % radius, ty + j / radius, null);
    }

    /** Removes the obstacle tiles in a radius around a point. */
    public void destroyObstacles(int tx, int ty, int radius) {
        for (int j = 0; j < radius * radius; j++)
            obs.setCell(tx + j % radius, ty + j / radius, null);
    }

    /** Toggles visibility of the fog layer. */
    public void toggleFogLayer() {
        foreLayers[0] = foreLayers[0] == -1 ? map.getLayers().getIndex(FOG_LAYER_NAME) : -1;
    }

    /** Sets the projection matrix from the given camera. */
    public void setView(OrthographicCamera camera) {
        mapRenderer.setView(camera);
    }

    /** Renders the background layers. */
    public void renderBackgroundLayers() {
        mapRenderer.render(backLayers);
    }

    /** Renders the foreground layers. */
    public void renderForegroundLayers() {
        if (foreLayers[0] > 0)
            mapRenderer.render(foreLayers);
    }

    // getters and setters

    /** Returns the map objects. */
    public MapObjects getObjects() {
        return map.getLayers().get("Objects").getObjects();
    }

    /** Returns the obstacles layer. */
    public TiledMapTileLayer getObstacles() {
        return obs;
    }

    /** Returns the fog layer. */
    public TiledMapTileLayer getFog() {
        return fog;
    }

    /** Returns the width of the map in world units. */
    public int getWidth() {
        return width * TreasureGame.TILESIZE;
    }

    /** Returns the height of the map in world units. */
    public int getHeight() {
        return height * TreasureGame.TILESIZE;
    }
}
