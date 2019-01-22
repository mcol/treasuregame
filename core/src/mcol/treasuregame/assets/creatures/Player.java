package mcol.treasuregame.assets.creatures;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import mcol.treasuregame.assets.Assets;

public class Player extends Creature {

    /** Length of each animation frame. */
    private static final float ANIMATION_TIME = 0.15f;

    /** Movement speed. */
    private static final int SPEED = 4;

    /** Amount of fog cleared. */
    private static final int FOG_RADIUS = 3;

    /** Cost of a lamb. */
    private static final int LAMB_COST = 20;

    /** Current score. */
    private int score;

    /** Current score towards acquiring a lamb. */
    private int lambScore;

    /** Number of bombs available. */
    private int bombs;

    /** Number of lambs available. */
    private int lambs;

    /** Constructor. */
    public Player(int x, int y, int character) {
        super(x, y, SPEED, FOG_RADIUS);
        int row = character / 4 * 4;
        int col = character % 4 * 3;
        TextureRegion[] d = Assets.playTexture[row + 0];
        TextureRegion[] l = Assets.playTexture[row + 1];
        TextureRegion[] r = Assets.playTexture[row + 2];
        TextureRegion[] u = Assets.playTexture[row + 3];
        dn = new Animation<TextureRegion>(ANIMATION_TIME, d[col], d[col + 1], d[col + 2]);
        lt = new Animation<TextureRegion>(ANIMATION_TIME, l[col], l[col + 1], l[col + 2]);
        rt = new Animation<TextureRegion>(ANIMATION_TIME, r[col], r[col + 1], r[col + 2]);
        up = new Animation<TextureRegion>(ANIMATION_TIME, u[col], u[col + 1], u[col + 2]);
        dn.setPlayMode(Animation.PlayMode.LOOP_PINGPONG);
        lt.setPlayMode(Animation.PlayMode.LOOP_PINGPONG);
        rt.setPlayMode(Animation.PlayMode.LOOP_PINGPONG);
        up.setPlayMode(Animation.PlayMode.LOOP_PINGPONG);
    }

    /** Increments the score. */
    public void addScore(int value) {
        score += value;
        lambScore += value;
    }

    /** Adds a bomb. */
    public void addBomb() {
        bombs++;
    }

    /** Removes a bomb. */
    public void removeBomb() {
        if (bombs > 0)
            bombs--;
    }

    /** Removes a lamb. */
    public void removeLamb() {
        if (lambs > 0)
            lambs--;
    }

    @Override
    public void update(float dt) {
        super.update(dt);
        if (lambScore > LAMB_COST) {
            lambScore -= LAMB_COST;
            lambs++;
        }
    }

    // getters and setters

    /** Returns the current score. */
    public int getScore() {
        return score;
    }

    /** Returns the number of bombs available. */
    public int getBombs() {
        return bombs;
    }

    /** Returns the number of lambs available. */
    public int getLambs() {
        return lambs;
    }
}
