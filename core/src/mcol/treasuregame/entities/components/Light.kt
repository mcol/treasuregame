package mcol.treasuregame.entities.components

import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.math.MathUtils
import mcol.treasuregame.entities.Assets

class Light(val size: Float) : Component {
    val texture: Texture = Assets.maskTexture
    var stateTime = 0f
    var flickerSize = size
    val nextFlickerSize = {
        size * (1 + MathUtils.random(0.05f)
                  + 0.03f * MathUtils.sin(size / 4 * stateTime))
    }
}
