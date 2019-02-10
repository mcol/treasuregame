package mcol.treasuregame.assets.items;

import com.badlogic.gdx.graphics.g2d.TextureRegion;

public abstract class CollectableItem extends Item {

    /** Constructor. */
    public CollectableItem(float x, float y, int value, TextureRegion texture) {
        super(x, y, texture);
        this.value = value;
        this.collectable = true;
    }
}
