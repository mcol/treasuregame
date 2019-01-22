package mcol.treasuregame;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import mcol.treasuregame.assets.Assets;
import mcol.treasuregame.states.MenuState;

public class TreasureGame extends Game {

    /** Height of the game viewport in world units. */
    public static final int HEIGHT = 16;

    /** Width of the game viewport in world units. */
    public static final int WIDTH = HEIGHT * 16 / 9;

    /** Scaling factor for rendering. */
    public static final int SCALE = 32;

    /** Size of each tile in pixels. */
    private static final int ORIG_TILESIZE = 32;

    /** Scaled size of each tile in world units. */
    public static final int TILESIZE = ORIG_TILESIZE / SCALE;

    /** Name of the game. */
    public static final String TITLE = "TreasureGame";

    /** Object to batch the drawing of the graphics. */
    private SpriteBatch batch;

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
