package mcol.treasuregame.entities.components

import com.badlogic.gdx.graphics.g2d.TextureRegion
import mcol.treasuregame.TreasureGame

class Appearance(var texture: TextureRegion? = null,
                 val size: Float = SIZE,
                 val offset: Int = 0) : Component {

    var skipRendering: Boolean = false

    companion object {
        /** Renderable size of each entity.  */
        private const val SIZE = 32 * TreasureGame.SCALE
    }
}
