package mcol.treasuregame.utils;

import com.badlogic.gdx.math.Vector3;
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
}
