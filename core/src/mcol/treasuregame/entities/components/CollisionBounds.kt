package mcol.treasuregame.entities.components

import com.badlogic.gdx.math.Rectangle

class CollisionBounds(x: Float, y: Float, size: Float) : Component {

    val offset = size / 4f

    /** Collision bounding box.  */
    val bounds = Rectangle(x + offset, y + offset, size / 2f, size / 2f)
}
