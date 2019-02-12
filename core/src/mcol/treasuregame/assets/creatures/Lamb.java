package mcol.treasuregame.assets.creatures;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector3;
import mcol.treasuregame.TreasureGame;
import mcol.treasuregame.assets.Assets;
import mcol.treasuregame.assets.Map;
import mcol.treasuregame.assets.items.Item;

import java.util.List;

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
    public void setTarget(List<Item> items) {
        int attempts = 10;
        getRandomCollectableItemCoordinates(items, target);
        while (!map.hasFog(target.x, target.y) && attempts > 0) {
            getRandomCollectableItemCoordinates(items, target);
            attempts--;
        }
        findPathToTarget(map, target);
    }

    /** Returns the coordinates of a random collectable item. */
    private void getRandomCollectableItemCoordinates(List<Item> items, Vector3 coords) {
        int numberOfCollectableItems = 0;
        for (Item item : items) {
            if (item.isCollectable())
                numberOfCollectableItems++;
        }
        if (numberOfCollectableItems > 0) {
            Item item = items.get(MathUtils.random(items.size() - 1));
            while (!item.isCollectable())
                item = items.get(MathUtils.random(items.size() - 1));
            coords.x = item.getX();
            coords.y = item.getY();
        }
    }

    @Override
    public boolean shouldRemove() {
        return !moving && !map.hasFog((int) targetTile.x, (int) targetTile.y);
    }
}
