package mcol.treasuregame.entities

import com.badlogic.gdx.graphics.OrthographicCamera
import com.badlogic.gdx.graphics.g2d.Batch
import com.badlogic.gdx.maps.MapObjects
import com.badlogic.gdx.maps.tiled.TiledMap
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer
import mcol.treasuregame.TreasureGame
import mcol.treasuregame.utils.Utils
import java.util.*

class Map(sb: Batch, level: Int) {

    /** The whole map. */
    private val map: TiledMap = Assets[String.format(Locale.US, "maps/%02d.tmx", level), TiledMap::class.java]

    /** The copy of the obstacles layer. */
    private val obsCopy: TiledMapTileLayer

    /** The fog layer. */
    private val fog: BooleanArray

    /** The map renderer. */
    private val mapRenderer = OrthogonalTiledMapRenderer(map, TreasureGame.SCALE, sb)

    /** Layers to be drawn in the background. */
    private val backLayers = intArrayOf(0, -1)

    /** The texture used to render the fog. */
    private val fogTexture = map.tileSets.getTileSet(0).getTile(FOG_TILE_ID).textureRegion

    /** Width of the map in tiles. */
    private val width: Int

    /** Height of the map in tiles. */
    private val height: Int

    /** Whether the fog should be rendered. */
    private var showFog: Boolean = true

    /** Returns the map objects. */
    val objects: MapObjects
        get() = map.layers.get("Objects").objects

    init {
        val layer = map.layers.get(0) as TiledMapTileLayer
        width = layer.width
        height = layer.height
        val tileSize = TreasureGame.TILESIZE_PIXELS
        obsCopy = TiledMapTileLayer(width, height, tileSize,  tileSize)
        fog = BooleanArray(width * height) { true }
        copyObstaclesLayer()
    }

    /** Creates a copy of the obstacles layer. */
    private fun copyObstaclesLayer() {

        // remove layer if already there
        val obsCopyName = "ObsCopy"
        val id = map.layers.getIndex(obsCopyName)
        if (id > 0)
            map.layers.remove(id)

        // copy the original layer
        val obsOrig = map.layers.get("Obstacles") as TiledMapTileLayer
        for (i in 0 until width) {
            for (j in 0 until height)
                obsCopy.setCell(i, j, obsOrig.getCell(i, j))
        }
        obsCopy.name = obsCopyName
        map.layers.add(obsCopy)
        backLayers[1] = map.layers.getIndex(obsCopyName)
    }

    /** Removes the fog tiles in a radius around a point. */
    fun destroyFog(x: Float, y: Float, radius: Int) {
        val tx = Utils.toTile(x)
        val ty = Utils.toTile(y)
        val xmin = Math.max(tx - radius, 0)
        val ymin = Math.max(ty - radius, 0) * width
        val xmax = Math.min(tx + radius + 1, width)
        val ymax = Math.min(ty + radius + 1, height) * width
        for (j in ymin until ymax step width) {
            for (i in xmin until xmax)
                fog[i + j] = false
        }
    }

    /** Removes the obstacle tiles in a radius around a point. */
    fun destroyObstacles(x: Float, y: Float, radius: Int) {
        val tx = Utils.toTile(x) - radius
        val ty = Utils.toTile(y) - radius
        val diameter = radius * 2 + 1
        for (j in diameter * diameter - 1 downTo 0)
            obsCopy.setCell(tx + j % diameter, ty + j / diameter, null)
    }

    /** Toggles visibility of the fog layer. */
    fun toggleFogLayer() {
        showFog = !showFog
    }

    /** Renders the background layers. */
    fun renderBackgroundLayers(camera: OrthographicCamera) {
        mapRenderer.setView(camera)
        mapRenderer.render(backLayers)
    }

    /** Renders the fog layer. */
    fun renderFog(sb: Batch) {
        for (j in (if (showFog) 0 else fog.size) until fog.size) {
            if (fog[j])
                sb.draw(fogTexture, j % width * TreasureGame.TILESIZE, j / width * TreasureGame.TILESIZE,
                        TreasureGame.TILESIZE, TreasureGame.TILESIZE)
        }
    }

    /** Returns whether there is an obstacle at the given tile coordinates. */
    fun hasObstacle(x: Int, y: Int): Boolean {
        return obsCopy.getCell(x, y) != null
    }

    /** Returns whether there is fog at the given tile coordinates. */
    fun hasFog(x: Int, y: Int): Boolean {
        return if (x < 0 || y < 0 || x >= width || y >= height) false else fog[x + y * width]
    }

    /** Returns whether there is fog at the given world coordinates. */
    fun hasFog(x: Float, y: Float): Boolean {
        return hasFog(Utils.toTile(x), Utils.toTile(y))
    }

    /** Returns the width of the map in world units. */
    fun getWidth(): Float {
        return width * TreasureGame.TILESIZE
    }

    /** Returns the height of the map in world units. */
    fun getHeight(): Float {
        return height * TreasureGame.TILESIZE
    }

    companion object {
        /** Identifier of the tile used for fog. */
        private const val FOG_TILE_ID = 35
    }
}
