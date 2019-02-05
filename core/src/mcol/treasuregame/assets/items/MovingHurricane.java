package mcol.treasuregame.assets.items;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.math.MathUtils;
import mcol.treasuregame.TreasureGame;
import mcol.treasuregame.assets.Assets;
import mcol.treasuregame.assets.Map;
import mcol.treasuregame.utils.Utils;

public class MovingHurricane extends AnimatedItem {

    /** The world map. */
    private final Map map;

    /** Movement speed in the two directions. */
    private float xSpeed, ySpeed;

    /** Time of next change of direction. */
    private float xNextChange, yNextChange;

    /**  Constructor. */
    public MovingHurricane(float x, float y, Map map) {
        super(x, y, Assets.hurrTexture, 0.1f, 20f);
        this.map = map;
        animation.setPlayMode(Animation.PlayMode.LOOP);
        xSpeed = MathUtils.random(1, 3) * MathUtils.randomSign() * TreasureGame.TILESIZE;
        ySpeed = MathUtils.random(1, 3) * MathUtils.randomSign() * TreasureGame.TILESIZE;
        xNextChange = MathUtils.random(2, 7);
        yNextChange = MathUtils.random(2, 7);
    }

    /** Moves the hurricane. */
    private void move(float dt) {
        if (stateTime > xNextChange) {
            xSpeed *= MathUtils.randomSign();
            xNextChange += MathUtils.random(2, 7);
        }
        if (stateTime > yNextChange) {
            ySpeed *= MathUtils.randomSign();
            yNextChange += MathUtils.random(2, 7);
        }
        x += xSpeed * dt;
        y += ySpeed * dt;

        // don't go outside of the map
        if (x < -0.5 || x > map.getWidth() - 0.5)
            xSpeed *= -1;
        if (y < -0.5 || y > map.getHeight() - 0.5)
            ySpeed *= -1;
    }

    @Override
    public void update(float dt) {
        super.update(dt);
        move(dt);
        map.destroyFog(Utils.toTile(x), Utils.toTile(y), 1);
    }
}
