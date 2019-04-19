package mcol.treasuregame.entities.systems

import com.badlogic.gdx.math.Vector3
import mcol.treasuregame.entities.EntityManager
import mcol.treasuregame.entities.Map
import mcol.treasuregame.entities.components.Appearance
import mcol.treasuregame.entities.components.CollisionBounds
import mcol.treasuregame.entities.components.FacingDirection
import mcol.treasuregame.entities.components.Navigation
import mcol.treasuregame.entities.components.Position
import mcol.treasuregame.entities.components.TargetIndicator
import mcol.treasuregame.entities.components.Velocity
import mcol.treasuregame.utils.PathFinder
import mcol.treasuregame.utils.Utils

class NavigationSystem(val map: Map) : GameSystem {

    fun setTarget(em: EntityManager, entity: Long, coords: Vector3) {
        val navigation = em.getComponent(entity, Navigation::class.java)
        val position = em.getComponent(entity, Position::class.java)
        navigation.targetTile.set(Utils.toTile(coords))
        navigation.path = PathFinder.findPath(map, position.current, navigation.targetTile)
        val targetIndicator = em.getUniqueEntityWith(TargetIndicator::class.java)
        em.getComponent(targetIndicator, Position::class.java).current
          .set(navigation.targetTile)
    }

    override fun update(em: EntityManager, dt: Float) {
        for (entity in em.getEntitiesWith(Navigation::class.java)) {
            val position = em.getComponent(entity, Position::class.java).current
            val bounds = em.getComponent(entity, CollisionBounds::class.java).bounds
            val navigation = em.getComponent(entity, Navigation::class.java)
            val velocity = em.getComponent(entity, Velocity::class.java)
            velocity.moving = false

            if (Math.abs(position.x - navigation.currentTarget.x) < MIN_STEP)
                position.x = navigation.currentTarget.x
            if (Math.abs(position.y - navigation.currentTarget.y) < MIN_STEP)
                position.y = navigation.currentTarget.y

            // move towards the target
            if (navigation.path.isNotEmpty() &&
                Math.abs(position.x - navigation.currentTarget.x) < MIN_STEP &&
                Math.abs(position.y - navigation.currentTarget.y) < MIN_STEP)
                navigation.currentTarget
                          .set(navigation.path.removeAt(navigation.path.size - 1))

            if (Math.abs(position.x - navigation.currentTarget.x) > MIN_STEP ||
                Math.abs(position.y - navigation.currentTarget.y) > MIN_STEP) {
                velocity.velocity.x = Math.signum(navigation.currentTarget.x - position.x) * velocity.speed
                velocity.velocity.y = Math.signum(navigation.currentTarget.y - position.y) * velocity.speed
                velocity.velocity.nor().scl(velocity.speed)
                velocity.dir = when {
                    velocity.velocity.x < 0 -> FacingDirection.LEFT
                    velocity.velocity.x > 0 -> FacingDirection.RIGHT
                    velocity.velocity.y < 0 -> FacingDirection.DOWN
                    velocity.velocity.y > 0 -> FacingDirection.UP
                    else -> velocity.dir
                }

                velocity.velocity.scl(Math.min(dt, 1 / 30f))
                position.x += velocity.velocity.x
                position.y += velocity.velocity.y
                velocity.moving = true
                val offset = em.getComponent(entity, CollisionBounds::class.java).offset
                bounds.setPosition(position.x + offset, position.y + offset)
            }

            // hide the target indicator if the player has stopped moving
            if (entity == em.player) {
                val targetIndicator = em.getUniqueEntityWith(TargetIndicator::class.java)
                em.getComponent(targetIndicator, Appearance::class.java).skipRendering = !velocity.moving
            }

            // remove lamb when its item has been discovered
            else if (!velocity.moving && !map.hasFog(navigation.targetTile.x, navigation.targetTile.y)) {
                em.remove(entity)
            }
        }
    }

    companion object {
        /** Minimum movement before rounding to the tile edge.  */
        private const val MIN_STEP = 0.1f
    }
}
