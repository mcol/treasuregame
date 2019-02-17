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

    /** Boundaries for the camera position. */
    private float left, right, bottom, top;

    /** Constructor. */
    public Camera() {
        target = new Vector3();
    }

    public void update(float dt, Vector3 target) {
        centreCamera(target, dt);

        if (CameraShake.isShaking()) {
            CameraShake.update(dt);
            translate(CameraShake.getTranslation());
            float currentAngle = (float) Math.atan2(up.x, up.y) * MathUtils.radiansToDegrees;
            rotate(CameraShake.getRotation() - currentAngle);
        }
    }

    /** Moves the camera so that the target is at the target. */
    private void centreCamera(Vector3 pos, float dt) {

        // check the map bounds to avoid showing blank space outside of the map
        target = pos.cpy();
        target.x = MathUtils.clamp(target.x, left, right);
        target.y = MathUtils.clamp(target.y, bottom, top);

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

    @SuppressWarnings("unused")
    public void resize(int width, int height) {
        float hudWidth = 120 / (width / viewportWidth);
        left = viewportWidth / 2 * zoom;
        right = worldWidth - left + hudWidth * zoom;
        bottom = viewportHeight / 2 * zoom;
        top = worldHeight - bottom;
    }
}
