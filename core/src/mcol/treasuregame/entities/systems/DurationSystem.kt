package mcol.treasuregame.entities.systems

import mcol.treasuregame.entities.EntityManager
import mcol.treasuregame.entities.components.Duration
import mcol.treasuregame.entities.components.Explosion
import mcol.treasuregame.entities.components.Position

class DurationSystem : GameSystem {

    override fun update(em: EntityManager, dt: Float) {
        for (entity in em.getEntitiesWith(Duration::class.java).listIterator()) {
            val dc = em.getComponent(entity, Duration::class.java)
            dc.stateTime += dt
            if (dc.stateTime > dc.duration) {
                val position = em.getComponent(entity, Position::class.java).current
                when (dc.nextEvent) {
                    is Explosion -> em.factory.createExplosion(position.x, position.y,
                                                               dc.nextEvent.radius)
                }
                em.remove(entity)
            }
        }
    }
}
