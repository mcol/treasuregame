package mcol.treasuregame.entities.components

import com.badlogic.gdx.graphics.g2d.Animation
import com.badlogic.gdx.graphics.g2d.TextureRegion

class AnimationComponent(animationTexture: Array<TextureRegion>,
                         animationTime: Float,
                         playMode: Animation.PlayMode = Animation.PlayMode.NORMAL) : Component {

    /** Time since the animation has started. */
    var stateTime = 0f

    val animation = Animation(animationTime, *animationTexture)
    init {
        animation.playMode = playMode
    }
}
