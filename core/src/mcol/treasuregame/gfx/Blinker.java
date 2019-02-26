package mcol.treasuregame.gfx;

public class Blinker {

    /** Duration of each blink. */
    private final float blinkTime;

    /** Timer for the blink state. */
    private float stateTime;

    /** Whether the state is visible. */
    private boolean visible;

    /** Constructor. */
    public Blinker(float blinkTime) {
        this.blinkTime = blinkTime;
    }

    public void update(float dt) {
        stateTime += dt;
        if (stateTime > blinkTime) {
            stateTime = 0;
            visible = !visible;
        }
    }

    /** Returns whether the blinker is in the visible state. */
    public boolean isVisible() {
        return visible;
    }
}
