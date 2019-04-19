package mcol.treasuregame.entities.systems

import com.badlogic.gdx.math.MathUtils
import mcol.treasuregame.entities.EntityManager
import mcol.treasuregame.entities.Map
import mcol.treasuregame.entities.components.BubbleMovement
import mcol.treasuregame.entities.components.Light
import mcol.treasuregame.entities.components.Position
import mcol.treasuregame.entities.components.RandomMovement

class MovementSystem(private val map: Map) : GameSystem {

    override fun update(em: EntityManager, dt: Float) {

        for (entity in em.getEntitiesWith(BubbleMovement::class.java)) {
            val mov = em.getComponent(entity, BubbleMovement::class.java)
            val position = em.getComponent(entity, Position::class.java).current
            position.x += mov.xMovement() * dt
            position.y += mov.yMovement() * dt
        }

        for (entity in em.getEntitiesWith(RandomMovement::class.java)) {
            val mov = em.getComponent(entity, RandomMovement::class.java)
            val position = em.getComponent(entity, Position::class.java).current
            mov.stateTime += dt
            if (mov.stateTime > mov.xNextChange) {
                mov.xSpeed *= MathUtils.randomSign()
                mov.xNextChange += mov.nextChangeTime()
            }
            if (mov.stateTime > mov.yNextChange) {
                mov.ySpeed *= MathUtils.randomSign()
                mov.yNextChange += mov.nextChangeTime()
            }
            position.x += mov.xSpeed * dt
            position.y += mov.ySpeed * dt

            // don't go outside of the map
            if (position.x < -0.5 || position.x > map.width - 0.5)
                mov.xSpeed *= -1f
            if (position.y < -0.5 || position.y > map.height - 0.5)
                mov.ySpeed *= -1f
        }

        // light flicker
        for (entity in em.getEntitiesWith(Light::class.java)) {
            val light = em.getComponent(entity, Light::class.java)
            light.stateTime += dt
            light.flickerSize = light.nextFlickerSize()
        }
    }
}
