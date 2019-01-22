package mcol.treasuregame.assets.items;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import mcol.treasuregame.TreasureGame;

public abstract class Item {

    /** Size of the item in pixels. */
    protected static final int ORIG_SIZE = 32;

    /** Rendered width and height in world units. */
    protected static final int SIZE = ORIG_SIZE / TreasureGame.SCALE;

    /** Texture representing the item. */
    protected TextureRegion texture;

    /** Coordinates of the bottom left corner of the item in world units. */
    protected float x, y;

    /** Points awarded by the item. */
    protected int value;

    /** Collision bounding box. */
    protected Rectangle bounds;

    /** Constructor. */
    public Item(float x, float y, int value, TextureRegion texture) {
        this.x = x;
        this.y = y;
        this.value = value;
        this.texture = texture;
        this.bounds = new Rectangle(x + SIZE / 4f, y + SIZE / 4f, SIZE / 2f, SIZE / 2f);
    }

    public void update(float dt) { /* nothing to do */ }

    public void render(SpriteBatch sb) {
        sb.draw(texture, x, y, SIZE, SIZE);
    }

    // getters and setters

    /** Gets the horizontal coordinate. */
    public float getX() {
        return x;
    }

    /** Gets the vertical coordinate. */
    public float getY() {
        return y;
    }

    /** Returns the value of the item. */
    public int getValue() {
        return value;
    }

    /** Returns the collision rectangle. */
    public Rectangle getBounds() {
        return bounds;
    }

    /** Returns whether the entity should be removed. */
    public boolean shouldRemove() {
        return false;
    }
}
