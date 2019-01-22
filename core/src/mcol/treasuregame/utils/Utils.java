package mcol.treasuregame.utils;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import mcol.treasuregame.TreasureGame;

public class Utils {

    /** Converts a value to tile coordinates. */
    public static int toTile(float coord) {
        return (int) (coord / TreasureGame.TILESIZE);
    }

    /** Converts a vector to tile coordinates. */
    public static Vector3 toTile(Vector3 coords) {
        return new Vector3((int) (coords.x / TreasureGame.TILESIZE),
                           (int) (coords.y / TreasureGame.TILESIZE), 0);
    }

    /** Converts a vector to tile coordinates after some rounding. */
    public static Vector3 toTile(Vector3 coords, float round) {
        return new Vector3((int) ((coords.x + round) / TreasureGame.TILESIZE),
                           (int) ((coords.y + round) / TreasureGame.TILESIZE), 0);
    }

    /** Enforces a value to stay between two extremes. */
    public static float clampValue(float x, float min, float max) {
        return x < min ? min : x > max ? max : x;
    }

    /** Creates a button with the given images. */
    public static Button createButton(TextureRegion[] upImage) {
        Button.ButtonStyle style = new Button.ButtonStyle();
        style.up = new TextureRegionDrawable(upImage[0]);
        style.down = new TextureRegionDrawable(upImage[1]);
        style.checked = new TextureRegionDrawable(upImage[2]);
        style.disabled = new TextureRegionDrawable(upImage[3]);
        return new Button(style);
    }
}
