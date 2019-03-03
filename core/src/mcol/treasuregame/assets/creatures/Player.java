package mcol.treasuregame.assets.creatures;

import com.badlogic.gdx.graphics.g2d.Animation;
import mcol.treasuregame.TreasureGame;
import mcol.treasuregame.assets.Assets;

public class Player extends Creature {

    /** Length of each animation frame. */
    private static final float ANIMATION_TIME = 0.15f;

    /** Movement speed. */
    private static final float SPEED = 4 * TreasureGame.TILESIZE;

    /** Cost of a lamb. */
    private static final int LAMB_COST = 20;

    /** Current score. */
    private int score;

    /** Current score towards acquiring a lamb. */
    private int lambScore;

    /** Number of bombs available. */
    private int bombs;

    /** Number of hurricanes available. */
    private int hurricanes;

    /** Number of lambs available. */
    private int lambs;

    /** Constructor. */
    public Player(float x, float y, int character) {
        super(x, y, SPEED, 1);
        int row = character * 4;
        dn = new Animation<>(ANIMATION_TIME, Assets.playTexture[row]);
        lt = new Animation<>(ANIMATION_TIME, Assets.playTexture[row + 1]);
        rt = new Animation<>(ANIMATION_TIME, Assets.playTexture[row + 2]);
        up = new Animation<>(ANIMATION_TIME, Assets.playTexture[row + 3]);
        dn.setPlayMode(Animation.PlayMode.LOOP_PINGPONG);
        lt.setPlayMode(Animation.PlayMode.LOOP_PINGPONG);
        rt.setPlayMode(Animation.PlayMode.LOOP_PINGPONG);
        up.setPlayMode(Animation.PlayMode.LOOP_PINGPONG);
    }

    /** Increments the score. */
    public void addScore(int value) {
        messageManager.addMessage(Integer.toString(value), position.x, position.y);
        score += value;
        lambScore += value;
    }

    /** Adds a bomb. */
    public void addBomb() {
        messageManager.addMessage("+1 bomb", position.x, position.y);
        bombs++;
    }

    /** Removes a bomb. */
    public void removeBomb() {
        if (bombs > 0)
            bombs--;
    }

    /** Adds a hurricane. */
    public void addHurricane() {
        messageManager.addMessage("+1 hurricane", position.x, position.y);
        hurricanes++;
    }

    /** Removes a hurricane. */
    public void removeHurricane() {
        if (hurricanes > 0)
            hurricanes--;
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
        messageManager.update(dt);
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

    /** Returns the number of hurricanes available. */
    public int getHurricanes() {
        return hurricanes;
    }

    /** Returns the number of lambs available. */
    public int getLambs() {
        return lambs;
    }
}
