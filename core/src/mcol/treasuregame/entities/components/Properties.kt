package mcol.treasuregame.entities.components

import com.badlogic.gdx.graphics.Color

class Collectable(val type: String,
                  val value: Int) : Component

class Collector : Component

class Destructor(val radius: Int,
                 val fog: Boolean,
                 val obstacles: Boolean = false) : Component

class MiniMappable(val color: Color,
                   val size: Float,
                   val blinking: Boolean = false) : Component

enum class FacingDirection {
    DOWN, LEFT, RIGHT, UP
}
