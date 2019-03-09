package mcol.treasuregame.entities.components

import com.badlogic.gdx.math.MathUtils
import mcol.treasuregame.TreasureGame

class RandomMovement(minSpeed: Float = 1f, maxSpeed: Float = 3f,
                     minTimeChange: Float = 2f, maxTimeChange: Float = 7f) : Component {

    /** Movement speed in the two directions.  */
    var xSpeed = MathUtils.random(minSpeed, maxSpeed) * TreasureGame.TILESIZE
    var ySpeed = MathUtils.random(minSpeed, maxSpeed) * TreasureGame.TILESIZE

    /** Time of next change of direction.  */
    val nextChangeTime = { MathUtils.random(minTimeChange, maxTimeChange) }
    var xNextChange = 0f
    var yNextChange = 0f
    var stateTime = 0f
}
