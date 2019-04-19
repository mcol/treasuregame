package mcol.treasuregame.entities.components

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.g2d.ParticleEffect

class Explosion(x: Float, y: Float, val radius: Int) : Component {
    val effect = ParticleEffect()
    init {
        effect.load(Gdx.files.internal("particles/explosion.p"),
                    Gdx.files.internal("particles"))
        effect.start()
        effect.setPosition(x + 0.5f, y + 0.5f)
        effect.scaleEffect(0.3f)
    }
}
