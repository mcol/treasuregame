package mcol.treasuregame.entities.systems

import mcol.treasuregame.entities.EntityManager
import mcol.treasuregame.entities.Map
import mcol.treasuregame.entities.components.Destructor
import mcol.treasuregame.entities.components.Position

class DestructionSystem(val map: Map) : GameSystem {

    override fun update(em: EntityManager, dt: Float) {
        for (entity in em.getEntitiesWith(Destructor::class.java)) {
            val destructor = em.getComponent(entity, Destructor::class.java)
            val position = em.getComponent(entity, Position::class.java).current
            if (destructor.fog)
                map.destroyFog(position.x, position.y, destructor.radius)
            if (destructor.obstacles)
                map.destroyObstacles(position.x, position.y, destructor.radius)
        }
    }
}
