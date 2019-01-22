package mcol.treasuregame.assets.items;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public abstract class AnimatedItem extends Item {

    /** Animation representing the item. */
    protected final Animation<TextureRegion> animation;

    /** Lifetime of the item. */
    protected final float durationTime;

    /** Time since the item has been created. */
    protected float stateTime;

    /** Constructor. */
    public AnimatedItem(float x, float y, int value,
                        TextureRegion[] animationTexture,
                        float animationTime, float durationTime) {
        super(x, y, value, null);
        this.durationTime = durationTime;
        animation = new Animation<TextureRegion>(animationTime, animationTexture);
    }

    @Override
    public void update(float dt) {
        stateTime += dt;
        texture = animation.getKeyFrame(stateTime);
    }

    @Override
    public boolean shouldRemove() {
        return stateTime > durationTime;
    }
}
