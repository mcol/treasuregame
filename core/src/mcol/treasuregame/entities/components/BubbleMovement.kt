package mcol.treasuregame.entities.components

import com.badlogic.gdx.math.MathUtils

class BubbleMovement(minX: Int = -2, maxX: Int = 2,
                     minY: Int = 0, maxY: Int = 3) : Component {
    val xMovement = { MathUtils.random(minX, maxX) }
    val yMovement = { MathUtils.random(minY, maxY) }
}
