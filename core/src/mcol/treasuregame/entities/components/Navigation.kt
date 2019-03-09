package mcol.treasuregame.entities.components

import com.badlogic.gdx.math.Vector3

class Navigation(x: Float, y: Float) : Component {
    val currentTarget = Vector3(x, y, 0f)
    val targetTile = Vector3()
    var path: MutableList<Vector3> = ArrayList()
}
