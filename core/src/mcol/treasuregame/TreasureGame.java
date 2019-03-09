package mcol.treasuregame;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import mcol.treasuregame.entities.Assets;
import mcol.treasuregame.states.MenuState;

public class TreasureGame extends Game {

    /** Height of the game viewport in world units. */
    public static final int HEIGHT = 16;

    /** Width of the game viewport in world units. */
    public static final int WIDTH = HEIGHT * 16 / 9;

    /** Number of pixels per world unit. */
    public static final float SCALE = 1f / 32;

    /** Size of each tile in pixels. */
    public static final int TILESIZE_PIXELS = 32;

    /** Size of a tile in world units. */
    public static final float TILESIZE = TILESIZE_PIXELS * SCALE;

    /** Name of the game. */
    public static final String TITLE = "TreasureGame";

    /** Object to batch the drawing of the graphics. */
    private Batch batch;

    /** Asset manager. */
    private Assets assets;

	@Override
	public void create () {
		batch = new SpriteBatch();
		assets = new Assets();
		assets.getBackgroundImage();
		setScreen(new MenuState(this, batch));
		assets.finishLoading();
	}

	@Override
	public void dispose () {
		batch.dispose();
		assets.dispose();
	}
}
