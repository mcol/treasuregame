package mcol.treasuregame.gfx;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector3;

public class CameraShake {

    /** Duration of the shake. */
    private static float length = 0;

    /** Strength of the shake. */
    private static float strength = 0;

    /** Time passed since the start of the shake. */
    private static float stateTime = 0;

    /** Position adjustment for the camera during the shake. */
    private static Vector3 pos = new Vector3();

    /** Initialises the shaking time and strength. */
    public static void set(float shakeLength, float shakeStrength) {
        length = shakeLength;
        strength = shakeStrength;
        stateTime = 0;
    }

    /** Computes the new camera position adjustment. */
    public static Vector3 update(float dt) {
        if (stateTime < length) {
            stateTime += dt;

            float currentPower = strength * (length - stateTime) / length;
            pos.x = MathUtils.random(-1f, 1f) * currentPower;
            pos.y = MathUtils.random(-1f, 1f) * currentPower;

        } else {
            length = 0;
            stateTime = 0;
        }
        return pos;
    }

    // getters and setters

    /** Returns whether the shaking is occurring. */
    public static boolean isShaking() {
        return length > 0;
    }
}
