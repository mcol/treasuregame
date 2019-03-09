package mcol.treasuregame.entities.components

import com.badlogic.gdx.math.Vector3

class Position(x: Float, y: Float) : Component {
    val current = Vector3(x, y, 0f)
}
