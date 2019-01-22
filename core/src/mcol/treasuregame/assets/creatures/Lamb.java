package mcol.treasuregame.assets.creatures;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import mcol.treasuregame.assets.Assets;
import mcol.treasuregame.assets.Map;

public class Lamb extends Creature {

    /** Length of each animation frame. */
    private static final float ANIMATION_TIME = 0.1f;

    /** Movement speed. */
    private static final int SPEED = 3;

    /** Amount of fog cleared. */
    private static final int FOG_RADIUS = 3;

    /** Constructor. */
    public Lamb(int x, int y, float xTarget, float yTarget, Map map) {
        super(x, y, SPEED, FOG_RADIUS);

        dn = new Animation<TextureRegion>(ANIMATION_TIME, Assets.lambTexture[0]);
        lt = new Animation<TextureRegion>(ANIMATION_TIME, Assets.lambTexture[1]);
        rt = new Animation<TextureRegion>(ANIMATION_TIME, Assets.lambTexture[2]);
        up = new Animation<TextureRegion>(ANIMATION_TIME, Assets.lambTexture[3]);
        dn.setPlayMode(Animation.PlayMode.LOOP_PINGPONG);
        lt.setPlayMode(Animation.PlayMode.LOOP_PINGPONG);
        rt.setPlayMode(Animation.PlayMode.LOOP_PINGPONG);
        up.setPlayMode(Animation.PlayMode.LOOP_PINGPONG);
        target.x = xTarget;
        target.y = yTarget;
        findPathToTarget(map.getObstacles(), target);
    }

    @Override
    public boolean shouldRemove() {
        return !walking && currentTile.epsilonEquals(targetTile);
    }
}
