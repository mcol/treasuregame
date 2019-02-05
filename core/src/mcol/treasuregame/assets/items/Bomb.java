package mcol.treasuregame.assets.items;

import mcol.treasuregame.assets.Assets;

public class Bomb extends CollectableItem {

    /** Constructor. */
    public Bomb(float x, float y) {
        super(x, y, 0, Assets.bombTexture[0]);
    }
}
