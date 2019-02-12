package mcol.treasuregame.assets.creatures;

import com.badlogic.gdx.graphics.g2d.Animation;
import mcol.treasuregame.TreasureGame;
import mcol.treasuregame.assets.Assets;
import mcol.treasuregame.assets.ItemManager;
import mcol.treasuregame.assets.Map;

public class Lamb extends Creature {

    /** Length of each animation frame. */
    private static final float ANIMATION_TIME = 0.1f;

    /** Movement speed. */
    private static final float SPEED = 3 * TreasureGame.TILESIZE;

    /** The world map. */
    private final Map map;

    /** Constructor. */
    public Lamb(float x, float y, Map map) {
        super(x, y, SPEED, 1);
        this.map = map;
        dn = new Animation<>(ANIMATION_TIME, Assets.lambTexture[0]);
        lt = new Animation<>(ANIMATION_TIME, Assets.lambTexture[1]);
        rt = new Animation<>(ANIMATION_TIME, Assets.lambTexture[2]);
        up = new Animation<>(ANIMATION_TIME, Assets.lambTexture[3]);
        dn.setPlayMode(Animation.PlayMode.LOOP_PINGPONG);
        lt.setPlayMode(Animation.PlayMode.LOOP_PINGPONG);
        rt.setPlayMode(Animation.PlayMode.LOOP_PINGPONG);
        up.setPlayMode(Animation.PlayMode.LOOP_PINGPONG);
    }

    /** Decides the target to move to. */
    public void setTarget(ItemManager items) {
        int attempts = 10;
        items.getRandomCollectableItemCoordinates(target);
        while (!map.hasFog(target.x, target.y) && attempts > 0) {
            items.getRandomCollectableItemCoordinates(target);
            attempts--;
        }
        findPathToTarget(map, target);
    }

    @Override
    public boolean shouldRemove() {
        return !moving && !map.hasFog((int) targetTile.x, (int) targetTile.y);
    }
}
