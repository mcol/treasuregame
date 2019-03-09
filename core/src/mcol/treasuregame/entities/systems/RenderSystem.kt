package mcol.treasuregame.entities.systems

import com.badlogic.gdx.graphics.g2d.Batch
import mcol.treasuregame.entities.EntityManager
import mcol.treasuregame.entities.Map
import mcol.treasuregame.entities.components.Appearance
import mcol.treasuregame.entities.components.Message
import mcol.treasuregame.entities.components.Position
import mcol.treasuregame.entities.components.TargetIndicator
import mcol.treasuregame.gfx.BitFont

class RenderSystem(private val em: EntityManager, private val map: Map) {

    fun render(sb: Batch) {

        sb.begin()

        for (entity in em.getEntitiesWith(Appearance::class.java)) {
            val appearance = em.getComponent(entity, Appearance::class.java)
            if (appearance.skipRendering || appearance.texture == null)
                continue
            val position = em.getComponent(entity, Position::class.java).current
            sb.draw(appearance.texture,
                    position.x + appearance.offset,
                    position.y + appearance.offset,
                    appearance.size, appearance.size)
        }

        map.renderFog(sb)

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
