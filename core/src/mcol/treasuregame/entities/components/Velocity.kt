package mcol.treasuregame.entities.components

import com.badlogic.gdx.math.Vector3

class Velocity(val speed: Float = 0f) : Component {

    /** Current velocity.  */
    val velocity = Vector3(0f, 0f, 0f)

    /** Current facing direction.  */
    var dir = FacingDirection.DOWN

    /** Whether the entity is moving. */
    var moving = false
}
