package mcol.treasuregame.entities.components

import com.badlogic.gdx.math.MathUtils
import mcol.treasuregame.TreasureGame

class BubbleMovement(minX: Int = -2, maxX: Int = 2,
                     minY: Int = 0, maxY: Int = 3) : Component {
    val xMovement= { MathUtils.random(minX, maxX) * TreasureGame.TILESIZE}
    val yMovement= { MathUtils.random(minY, maxY) * TreasureGame.TILESIZE}
}
