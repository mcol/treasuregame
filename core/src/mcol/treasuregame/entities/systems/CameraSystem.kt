package mcol.treasuregame.entities.systems

import mcol.treasuregame.entities.EntityManager
import mcol.treasuregame.entities.components.CameraComponent
import mcol.treasuregame.entities.components.Position

class CameraSystem : GameSystem {

    override fun update(em: EntityManager, dt: Float) {
        for (entity in em.getEntitiesWith(CameraComponent::class.java)) {
            val position = em.getComponent(entity, Position::class.java)
            em.getComponent(entity, CameraComponent::class.java).camera
              .update(dt, position.current)
        }
    }
}
