package mcol.treasuregame.entities

import com.badlogic.gdx.assets.AssetManager
import com.badlogic.gdx.assets.loaders.resolvers.InternalFileHandleResolver
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.NinePatch
import com.badlogic.gdx.graphics.g2d.TextureRegion
import com.badlogic.gdx.maps.tiled.TiledMap
import com.badlogic.gdx.maps.tiled.TmxMapLoader
import com.badlogic.gdx.utils.GdxRuntimeException

object Assets {

    /** Container for all assets.  */
    private val assets: AssetManager = AssetManager()

    /** Static fields.  */
    lateinit var menubg: Texture
    lateinit var iconTexture: Texture
    lateinit var exitTexture: Texture
    lateinit var maskTexture: Texture
    lateinit var buttonBombTexture: Array<TextureRegion>
    lateinit var buttonHurrTexture: Array<TextureRegion>
    lateinit var buttonLambTexture: Array<TextureRegion>
    lateinit var buttonPlayTexture: Array<TextureRegion>
    lateinit var bombTexture: Array<TextureRegion>
    lateinit var fontTexture: Array<TextureRegion>
    lateinit var hurrTexture: Array<TextureRegion>
    lateinit var trgtTexture: Array<TextureRegion>
    lateinit var itemTexture: Array<Array<TextureRegion>>
    lateinit var lambTexture: Array<Array<TextureRegion>>
    lateinit var playTexture: Array<Array<TextureRegion>>
    lateinit var buttonPatch: NinePatch

    init {
        // menu state
        assets.load("exit.png", Texture::class.java)
        assets.load("icon32.png", Texture::class.java)
        assets.load("menubg.png", Texture::class.java)

        // play state
        assets.load("bomb.png", Texture::class.java)
        assets.load("button-bomb.png", Texture::class.java)
        assets.load("button-hurricane.png", Texture::class.java)
        assets.load("button-lamb.png", Texture::class.java)
        assets.load("button-player.png", Texture::class.java)
        assets.load("font.png", Texture::class.java)
        assets.load("hud-bg.png", Texture::class.java)
        assets.load("hurricane.png", Texture::class.java)
        assets.load("items.png", Texture::class.java)
        assets.load("lamb.png", Texture::class.java)
        assets.load("lightmask.png", Texture::class.java)
        assets.load("players.png", Texture::class.java)
        assets.load("target.png", Texture::class.java)
        assets.setLoader(TiledMap::class.java, TmxMapLoader(InternalFileHandleResolver()))
        assets.load("maps/00.tmx", TiledMap::class.java)
        assets.update()
    }

    /** Loads the textures required for the menu state.  */
    fun getBackgroundImage() {
        while (!assets.isLoaded("menubg.png"))
            assets.update()
        exitTexture = assets.get("exit.png", Texture::class.java)
        iconTexture = assets.get("icon32.png", Texture::class.java)
        menubg = assets.get("menubg.png", Texture::class.java)
    }

    /** Ensures that all assets have been loaded.  */
    fun finishLoading() {
        assets.finishLoading()
        buttonBombTexture = TextureRegion.split(assets.get("button-bomb.png", Texture::class.java), 100, 110)[0]
        buttonHurrTexture = TextureRegion.split(assets.get("button-hurricane.png", Texture::class.java), 100, 110)[0]
        buttonLambTexture = TextureRegion.split(assets.get("button-lamb.png", Texture::class.java), 100, 110)[0]
        buttonPlayTexture = TextureRegion.split(assets.get("button-player.png", Texture::class.java), 100, 110)[0]
        bombTexture = TextureRegion.split(assets.get("bomb.png", Texture::class.java), 16, 16)[0]
        fontTexture = TextureRegion.split(assets.get("font.png", Texture::class.java), 9, 9)[0]
        hurrTexture = TextureRegion.split(assets.get("hurricane.png", Texture::class.java), 52, 52)[0]
        itemTexture = TextureRegion.split(assets.get("items.png", Texture::class.java), 32, 32)
        lambTexture = TextureRegion.split(assets.get("lamb.png", Texture::class.java), 24, 21)
        maskTexture = assets.get("lightmask.png", Texture::class.java)
        playTexture = TextureRegion.split(assets.get("players.png", Texture::class.java), 32, 32)
        trgtTexture = TextureRegion.split(assets.get("target.png", Texture::class.java), 32, 32)[0]
        buttonPatch = NinePatch(assets.get("hud-bg.png", Texture::class.java), 5, 5, 5, 5)
    }

    fun dispose() {
        assets.dispose()
    }

    /** Extracts an asset already loaded in the asset manager.  */
    operator fun <T> get(fileName: String, type: Class<T>): T {
        return assets.get(fileName, type)
               ?: throw GdxRuntimeException("Asset not loaded: $fileName")
    }
}
