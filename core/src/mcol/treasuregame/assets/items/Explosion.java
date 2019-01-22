package mcol.treasuregame.assets.items;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import mcol.treasuregame.assets.Assets;
import mcol.treasuregame.gfx.CameraShake;

public class Explosion extends AnimatedItem {

    /** Radius of the explosion. */
    private final int radius;

    /** Constructor. */
    public Explosion(float x, float y, int radius) {
        super(x, y, 0, Assets.explTexture, 0.1f, 0);
        this.radius = radius;
        CameraShake.set(0.4f, 0.2f * radius / 2);
    }

    @Override
    public void render(SpriteBatch sb) {
        TextureRegion frame = animation.getKeyFrame(stateTime);
        for (int i = 0; i < radius * radius; i++)
            sb.draw(frame, x + i / radius, y + i % radius, SIZE, SIZE);
    }

    @Override
    public boolean shouldRemove() {
        return animation.isAnimationFinished(stateTime);
    }
}
