package mcol.treasuregame.utils;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector3;

public class Utils {

    /** Converts a value to tile coordinates. */
    public static int toTile(float coord) {
        return MathUtils.round(coord);
    }

    /** Converts a vector to tile coordinates. */
    public static Vector3 toTile(Vector3 coords) {
        return new Vector3((int) coords.x, (int) coords.y, 0);
    }
}
