package mcol.treasuregame.assets.items;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import mcol.treasuregame.TreasureGame;

public abstract class Item {

    /** Rendered width and height in world units. */
    protected static final float SIZE = 32 * TreasureGame.SCALE;

    /** Texture representing the item. */
    protected TextureRegion texture;

    /** Coordinates of the bottom left corner of the item in world units. */
    protected float x, y;

    /** Collision bounding box. */
    protected Rectangle bounds;

    /** Whether the item is collectable. */
    protected boolean collectable;

    /** Points awarded by the item. */
    protected int value;

    /** Constructor. */
    public Item(float x, float y, TextureRegion texture) {
        this.x = x;
        this.y = y;
        this.texture = texture;
        this.bounds = new Rectangle(x + SIZE / 4f, y + SIZE / 4f, SIZE / 2f, SIZE / 2f);
    }

    public void update(float dt) { /* nothing to do */ }

    public void render(SpriteBatch sb) {
        sb.draw(texture, x, y, SIZE, SIZE);
    }

    // getters and setters

    /** Returns the horizontal coordinate in world units. */
    public float getX() {
        return x;
    }

    /** Returns the vertical coordinate in world units. */
    public float getY() {
        return y;
    }

    /** Returns the collision rectangle. */
    public Rectangle getBounds() {
        return bounds;
    }

    /** Returns the value of the item. */
    public int getValue() {
        return value;
    }

    /** Returns whether the item is collectable. */
    public boolean isCollectable() {
        return collectable;
    }

    /** Returns whether the item should be removed. */
    public boolean shouldRemove() {
        return false;
    }
}
