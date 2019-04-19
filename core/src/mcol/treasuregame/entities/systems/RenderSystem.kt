package mcol.treasuregame.entities.systems

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.GL20
import com.badlogic.gdx.graphics.Pixmap
import com.badlogic.gdx.graphics.g2d.Batch
import com.badlogic.gdx.graphics.glutils.FrameBuffer
import com.badlogic.gdx.graphics.glutils.ShaderProgram
import mcol.treasuregame.entities.Assets
import mcol.treasuregame.entities.EntityManager
import mcol.treasuregame.entities.Map
import mcol.treasuregame.entities.components.Appearance
import mcol.treasuregame.entities.components.Explosion
import mcol.treasuregame.entities.components.Light
import mcol.treasuregame.entities.components.Message
import mcol.treasuregame.entities.components.Position
import mcol.treasuregame.entities.components.TargetIndicator
import mcol.treasuregame.gfx.BitFont

class RenderSystem(private val em: EntityManager,
                   private val shader: ShaderProgram) {

    private val fbo = FrameBuffer(Pixmap.Format.RGBA8888,
                                  Gdx.graphics.width, Gdx.graphics.height, false)
    init {
        shader.begin()
        shader.setUniformf("u_ambient", 0.3f, 0.3f, 0.7f, 0.3f)
        shader.setUniformi("u_mask", 1)
        fbo.colorBufferTexture.bind(1)
        Assets.maskTexture.bind(0)
        shader.end()
    }

    fun render(sb: Batch, map: Map) {

        fbo.begin()
        sb.begin()
        sb.setBlendFunction(GL20.GL_ONE, GL20.GL_SRC_ALPHA)
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT)
        for (entity in em.getEntitiesWith(Light::class.java)) {
            val light = em.getComponent(entity, Light::class.java)
            val position = em.getComponent(entity, Position::class.java).current
            val size = light.flickerSize * 0.5f - 0.5f
            sb.draw(light.texture,
                    position.x - size, position.y - size,
                    light.flickerSize, light.flickerSize)
        }
        sb.setBlendFunction(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA)
        sb.end()
        fbo.end()

        sb.shader = shader
        sb.setColor(0.5f, 0.5f, 0.6f, 1.0f)
        map.renderBackgroundLayers()

        sb.begin()

        for (entity in em.getEntitiesWith(Appearance::class.java)) {
            val appearance = em.getComponent(entity, Appearance::class.java)
            if (appearance.skipRendering || appearance.texture == null)
                continue
            val position = em.getComponent(entity, Position::class.java).current
            sb.setColor(1.0f, 1.0f, 1.0f,
                        if (map.hasFog(position.x, position.y)) 0.1f else 1.0f)
            sb.draw(appearance.texture,
                    position.x + appearance.offset,
                    position.y + appearance.offset,
                    appearance.size, appearance.size)
        }

        sb.shader = null
        sb.setColor(1.0f, 1.0f, 1.0f, 1.0f)

        for (entity in em.getEntitiesWith(Explosion::class.java)) {
            val effect = em.getComponent(entity, Explosion::class.java).effect
            effect.draw(sb, Gdx.graphics.deltaTime)
        }

        for (entity in em.getEntitiesWith(TargetIndicator::class.java)) {
            val appearance = em.getComponent(entity, Appearance::class.java)
            if (appearance.skipRendering || appearance.texture == null)
                continue
            val position = em.getComponent(entity, Position::class.java).current
            sb.draw(appearance.texture,
                    position.x + appearance.offset,
                    position.y + appearance.offset,
                    appearance.size, appearance.size)
        }

        for (entity in em.getEntitiesWith(Message::class.java)) {
            val msg = em.getComponent(entity, Message::class.java).message
            val position = em.getComponent(entity, Position::class.java).current
            BitFont.renderMessage(sb, msg, position.x, position.y,
                                  BitFont.Size.MICRO, BitFont.Align.CENTRE)
        }

        sb.end()
    }
}
