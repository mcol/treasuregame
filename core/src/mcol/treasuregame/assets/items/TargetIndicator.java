package mcol.treasuregame.assets.items;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import mcol.treasuregame.TreasureGame;
import mcol.treasuregame.assets.Assets;
import mcol.treasuregame.assets.creatures.Player;

public class TargetIndicator extends AnimatedItem {

    /** The player object. */
    private final Player player;

    /** Constructor. */
    public TargetIndicator(Player player) {
        super(0, 0, Assets.trgtTexture, 0.15f, 0);
        this.player = player;
        animation.setPlayMode(Animation.PlayMode.LOOP_PINGPONG);
    }

    @Override
    public void render(SpriteBatch sb) {
        if (!player.isWalking())
            return;
        sb.draw(animation.getKeyFrame(stateTime),
                player.getTargetTile().x * TreasureGame.TILESIZE,
                player.getTargetTile().y * TreasureGame.TILESIZE, SIZE, SIZE);
    }
}
