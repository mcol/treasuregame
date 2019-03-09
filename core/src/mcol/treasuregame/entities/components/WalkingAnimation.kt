package mcol.treasuregame.entities.components

import com.badlogic.gdx.graphics.g2d.Animation
import com.badlogic.gdx.graphics.g2d.TextureRegion

class WalkingAnimation(d: Array<TextureRegion>, l: Array<TextureRegion>,
                       r: Array<TextureRegion>, u: Array<TextureRegion>,
                       animationTime: Float) : Component {

    /** Time since the animation has started. */
    var stateTime = 0f

    val dn = Animation(animationTime, *d)
    val lt = Animation(animationTime, *l)
    val rt = Animation(animationTime, *r)
    val up = Animation(animationTime, *u)
    init {
        dn.playMode = Animation.PlayMode.LOOP_PINGPONG
        lt.playMode = Animation.PlayMode.LOOP_PINGPONG
        rt.playMode = Animation.PlayMode.LOOP_PINGPONG
        up.playMode = Animation.PlayMode.LOOP_PINGPONG
    }
}
