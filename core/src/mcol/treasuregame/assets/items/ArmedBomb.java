package mcol.treasuregame.assets.items;

import mcol.treasuregame.assets.Assets;

public class ArmedBomb extends AnimatedItem {

    /** Constructor. */
    public ArmedBomb(float x, float y) {
        super((int) x, (int) y, 0, Assets.bombTexture, 0.2f, 1.2f);
    }
}
