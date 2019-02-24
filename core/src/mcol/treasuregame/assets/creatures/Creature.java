package mcol.treasuregame.assets.creatures;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;
import mcol.treasuregame.TreasureGame;
import mcol.treasuregame.assets.Map;
import mcol.treasuregame.gfx.MessageManager;
import mcol.treasuregame.utils.PathFinder;
import mcol.treasuregame.utils.Utils;

import java.util.List;

public abstract class Creature {

    /** Facing direction. */
    protected enum Direction {
        DOWN, LEFT, RIGHT, UP
    }

    /** Rendered width and height in world units. */
    protected static final float SIZE = 32 * TreasureGame.SCALE;

    /** Minimum movement before rounding to the tile edge. */
    private static final float MIN_STEP = 0.1f;

    /** Container for all messages. */
    protected static final MessageManager messageManager = new MessageManager();

    /** Animations representing the creature. */
    protected Animation<TextureRegion> dn, lt, rt, up;

    /** Texture corresponding to the current animation frame. */
    protected TextureRegion frame;

    /** Movement speed. */
    protected final float speed;

    /** Amount of fog cleared in each direction. */
    protected final int fogRadius;

    /** Current facing direction. */
    protected Direction dir;

    /** Current position. */
    protected final Vector3 position;

    /** Current velocity. */
    protected final Vector3 velocity;

    /** Collision bounding box. */
    protected final Rectangle bounds;

    /** Target coordinates. */
    protected final Vector3 target;

    /** Current tile. */
    protected Vector3 currentTile;

    /** Target tile. */
    protected Vector3 targetTile;

    /** Path to the target tile. */
    protected List<Vector3> path;

    /** Timer for the animation state. */
    protected float stateTime;

    /** Whether the moving animation should be used. */
    protected boolean moving;

    /** Constructor. */
    public Creature(float x, float y, float speed, int fogRadius) {
        this.speed = speed;
        this.fogRadius = fogRadius;
        dir = Direction.DOWN;
        position = new Vector3(x, y, 0);
        velocity = new Vector3();
        bounds = new Rectangle(position.x, position.y, SIZE, SIZE);
        target = new Vector3(position);
        currentTile = Utils.toTile(position);
        targetTile = new Vector3(currentTile);
    }

    /** Computes the path to the target tile. */
    public void findPathToTarget(Map map, Vector3 coords) {
        targetTile = Utils.toTile(coords);
        target.set(position);
        path = PathFinder.findPath(map, currentTile, targetTile);
    }

    /** Selects the current animation frame. */
    private void selectCurrentFrame() {
        float idx = moving ? stateTime : 0f;
        switch (dir) {
            case UP:
                frame = up.getKeyFrame(idx);
                break;
            case DOWN:
                frame = dn.getKeyFrame(idx);
                break;
            case LEFT:
                frame = lt.getKeyFrame(idx);
                break;
            case RIGHT:
                frame = rt.getKeyFrame(idx);
                break;
        }
    }

    /** Destroys elements of the map. */
    public void destroy(Map map) {
        map.destroyFog(position.x, position.y, fogRadius);
    }

    public void update(float dt) {
        dt = Math.min(dt, 1 / 30f);
        stateTime += dt;
        moving = false;

        if (Math.abs(target.x - position.x) < MIN_STEP)
            position.x = target.x;
        if (Math.abs(target.y - position.y) < MIN_STEP)
            position.y = target.y;

        // move towards the target
        if (path != null && path.size() > 0 && position.epsilonEquals(target, MIN_STEP))
            target.set(path.remove(path.size() - 1).scl(TreasureGame.TILESIZE));

        if (!position.epsilonEquals(target, MIN_STEP)) {
            velocity.x = Math.signum(target.x - position.x) * speed;
            velocity.y = Math.signum(target.y - position.y) * speed;
            velocity.nor().scl(speed);
            dir = (velocity.y < 0) ? Direction.DOWN :
                  (velocity.y > 0) ? Direction.UP : dir;
            dir = (velocity.x < 0) ? Direction.LEFT :
                  (velocity.x > 0) ? Direction.RIGHT : dir;

            velocity.scl(dt);
            position.add(velocity);
            moving = true;
        }

        currentTile = Utils.toTile(position);
        bounds.setPosition(position.x, position.y);
        selectCurrentFrame();
    }

    public void render(Batch sb) {
        sb.draw(frame, position.x, position.y, SIZE, SIZE);
    }

    public void renderMessages(Batch sb) {
        messageManager.render(sb);
    }

    // getters and setters

    /** Sets the current position. */
    public void setPosition(float x, float y) {
        position.x = x;
        position.y = y;
        currentTile = Utils.toTile(position);
    }

    /** Returns the current position in world units. */
    public Vector3 getPosition() {
        return position;
    }

    /** Returns the horizontal coordinate in world units. */
    public float getX() {
        return position.x;
    }

    /** Returns the vertical coordinate in world units. */
    public float getY() {
        return position.y;
    }

    /** Returns the collision bounding box. */
    public Rectangle getBounds() {
        return bounds;
    }

    /** Returns the current tile. */
    public Vector3 getCurrentTile() {
        return currentTile;
    }

    /** Returns the target tile. */
    public Vector3 getTargetTile() {
        return targetTile;
    }

    /** Returns the path to the target tile. */
    public List<Vector3> getPath() {
        return path;
    }

    /** Returns whether the creature is moving. */
    public boolean isMoving() {
        return moving;
    }

    /** Returns whether the creature should be removed. */
    public boolean shouldRemove() {
        return false;
    }
}
