package mcol.treasuregame.assets.items;

import com.badlogic.gdx.graphics.g2d.TextureRegion;

public abstract class CollectableItem extends Item {

    /** Points awarded by the item. */
    protected int value;

    /** Constructor. */
    public CollectableItem(float x, float y, int value, TextureRegion texture) {
        super(x, y, texture);
        this.value = value;
    }

    /** Returns the value of the item. */
    public int getValue() {
        return value;
    }
}
