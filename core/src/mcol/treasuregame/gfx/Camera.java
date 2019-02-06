package mcol.treasuregame.gfx;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector3;

public class Camera extends OrthographicCamera {

    /** Target position for the camera. */
    private Vector3 target;

    /** Width of the world in world units. */
    private float worldWidth;

    /** Height of the world in world units. */
    private float worldHeight;

    /** Constructor. */
    public Camera() {
        target = new Vector3();
    }

    public void update(float dt, Vector3 target) {
        centreCamera(target, dt);
        if (CameraShake.isShaking()) {
            translate(CameraShake.update(dt));
        }
    }

    /** Moves the camera so that the target is at the target. */
    private void centreCamera(Vector3 pos, float dt) {

        // check the map bounds to avoid showing blank space outside of the map
        target = pos.cpy();
        target.x = MathUtils.clamp(target.x, viewportWidth / 2 * zoom,
                                   worldWidth - viewportWidth / 2 * zoom);
        target.y = MathUtils.clamp(target.y, viewportHeight / 2 * zoom,
                                   worldHeight - viewportHeight / 2 * zoom);

        // move gradually towards the target
        float tween = 4f;
        float dist = target.dst(position);
        if (dist < 1 / tween) {
            tween = MathUtils.clamp(1 / dist, tween, 10);
        }
        position.lerp(target, tween * dt);
        update();
    }

    // getters and setters

    /** Sets the dimensions of the world. */
    public void setWorldSize(float worldWidth, float worldHeight) {
        this.worldWidth = worldWidth;
        this.worldHeight = worldHeight;
    }
}
