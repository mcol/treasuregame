package mcol.treasuregame.assets.items;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import mcol.treasuregame.TreasureGame;
import mcol.treasuregame.assets.Assets;
import mcol.treasuregame.gfx.CameraShake;

public class Explosion extends AnimatedItem {

    /** Radius of the explosion. */
    private final int radius;

    /** Constructor. */
    public Explosion(float x, float y, int radius) {
        super(x - radius * TreasureGame.TILESIZE,
              y - radius * TreasureGame.TILESIZE,
              0, Assets.explTexture, 0.1f, 0);
        this.radius = radius;
        CameraShake.set(0.4f, 0.2f * radius);
    }

    @Override
    public void render(SpriteBatch sb) {
        TextureRegion frame = animation.getKeyFrame(stateTime);
        int diameter = radius * 2 + 1;
        for (int i = 0, end = diameter * diameter; i < end; i++)
            sb.draw(frame, x + i / diameter * TreasureGame.TILESIZE,
                           y + i % diameter * TreasureGame.TILESIZE, SIZE, SIZE);
    }

    @Override
    public boolean shouldRemove() {
        return animation.isAnimationFinished(stateTime);
    }
}
