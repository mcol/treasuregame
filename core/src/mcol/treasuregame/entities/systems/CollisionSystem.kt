package mcol.treasuregame.entities.systems

import mcol.treasuregame.entities.EntityManager
import mcol.treasuregame.entities.components.Collectable
import mcol.treasuregame.entities.components.Collector
import mcol.treasuregame.entities.components.CollisionBounds
import mcol.treasuregame.entities.components.HUDComponent

class CollisionSystem : GameSystem {

    override fun update(em: EntityManager, dt: Float) {
        val collectors = em.getEntitiesWith(Collector::class.java)
        val collectables = em.getEntitiesWith(Collectable::class.java)
        val hudComponent = em.getUniqueComponent(HUDComponent::class.java)

        for (entity in collectors) {
            val bounds = em.getComponent(entity, CollisionBounds::class.java).bounds

            for (item in collectables) {
                val itemBounds = em.getComponent(item, CollisionBounds::class.java).bounds
                if (bounds.overlaps(itemBounds)) {
                    val collectable = em.getComponent(item, Collectable::class.java)
                    val value = collectable.value
                    hudComponent.score += value
                    hudComponent.lambScore += value
                    val message = when (collectable.type) {
                        "bomb" -> {
                            hudComponent.bombs++
                            "+1 bomb"
                        }
                        "hurricane" -> {
                            hudComponent.hurricanes++
                            "+1 hurricane"
                        }
                        else -> Integer.toString(value)
                    }
                    em.factory.createMessage(message, itemBounds.x, itemBounds.y)
                    em.remove(item)
                }
            }
        }

        if (hudComponent.lambScore >= hudComponent.lambCost) {
            hudComponent.lambScore -= hudComponent.lambCost
            hudComponent.lambs++
        }
    }
}
