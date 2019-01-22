package mcol.treasuregame.assets;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.resolvers.InternalFileHandleResolver;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.utils.GdxRuntimeException;

public class Assets {

    /** Static fields. */
    public static Texture menubg;
    public static Texture iconTexture;
    public static Texture exitTexture;
    public static TextureRegion[] buttonBombTexture;
    public static TextureRegion[] buttonLambTexture;
    public static TextureRegion[] buttonPlayTexture;
    public static TextureRegion[] bombTexture;
    public static TextureRegion[] explTexture;
    public static TextureRegion[] fontTexture;
    public static TextureRegion[] hurrTexture;
    public static TextureRegion[] trgtTexture;
    public static TextureRegion[][] itemTexture;
    public static TextureRegion[][] lambTexture;
    public static TextureRegion[][] playTexture;

    /** Container for all assets. */
    private static AssetManager assets;

    /** Constructor. */
    public Assets() {
        assets = new AssetManager();

        // menu state
        assets.load("exit.png", Texture.class);
        assets.load("icon32.png", Texture.class);
        assets.load("menubg.png", Texture.class);

        // play state
        assets.load("bomb.png", Texture.class);
        assets.load("button-bomb.png", Texture.class);
        assets.load("button-lamb.png", Texture.class);
        assets.load("button-player.png", Texture.class);
        assets.load("explosion.png", Texture.class);
        assets.load("font.png", Texture.class);
        assets.load("hurricane.png", Texture.class);
        assets.load("items.png", Texture.class);
        assets.load("lamb.png", Texture.class);
        assets.load("players.png", Texture.class);
        assets.load("target.png", Texture.class);
        assets.setLoader(TiledMap.class, new TmxMapLoader(new InternalFileHandleResolver()));
        assets.load("maps/00.tmx", TiledMap.class);
        assets.update();
    }

    /** Loads the textures required for the menu state. */
    public void getBackgroundImage() {
        while (!assets.isLoaded("menubg.png"))
            assets.update();
        exitTexture = assets.get("exit.png", Texture.class);
        iconTexture = assets.get("icon32.png", Texture.class);
        menubg = assets.get("menubg.png", Texture.class);
    }

    /** Ensures that all assets have been loaded. */
    public void finishLoading() {
        assets.finishLoading();
        buttonBombTexture = TextureRegion.split(assets.get("button-bomb.png", Texture.class), 32, 32)[0];
        buttonLambTexture = TextureRegion.split(assets.get("button-lamb.png", Texture.class), 32, 32)[0];
        buttonPlayTexture = TextureRegion.split(assets.get("button-player.png", Texture.class), 32, 32)[0];
        bombTexture = TextureRegion.split(assets.get("bomb.png", Texture.class), 16, 16)[0];
        explTexture = TextureRegion.split(assets.get("explosion.png", Texture.class), 32, 32)[0];
        fontTexture = TextureRegion.split(assets.get("font.png", Texture.class), 9, 9)[0];
        hurrTexture = TextureRegion.split(assets.get("hurricane.png", Texture.class), 52, 52)[0];
        itemTexture = TextureRegion.split(assets.get("items.png", Texture.class), 32, 32);
        lambTexture = TextureRegion.split(assets.get("lamb.png", Texture.class), 24, 21);
        playTexture = TextureRegion.split(assets.get("players.png", Texture.class), 32, 32);
        trgtTexture = TextureRegion.split(assets.get("target.png", Texture.class), 32, 32)[0];
    }

    /** Extracts an asset already loaded in the asset manager. */
    public static <T> T get(String fileName, Class<T> type) {
        T asset = assets.get(fileName, type);
        if (asset == null)
            throw new GdxRuntimeException("Asset not loaded: " + fileName);
        return asset;
    }

    public void dispose() {
        assets.dispose();
    }
}
