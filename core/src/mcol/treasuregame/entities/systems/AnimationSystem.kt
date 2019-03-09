package mcol.treasuregame.entities.systems

import mcol.treasuregame.entities.EntityManager
import mcol.treasuregame.entities.components.*

class AnimationSystem : GameSystem {

    override fun update(em: EntityManager, dt: Float) {

        for (entity in em.getEntitiesWith(AnimationComponent::class.java)) {
            val animation = em.getComponent(entity, AnimationComponent::class.java)
            val appearance = em.getComponent(entity, Appearance::class.java)
            animation.stateTime += dt
            appearance.texture = animation.animation.getKeyFrame(animation.stateTime)
        }

        for (entity in em.getEntitiesWith(WalkingAnimation::class.java)) {
            val animation = em.getComponent(entity, WalkingAnimation::class.java)
            val appearance = em.getComponent(entity, Appearance::class.java)
            val velocity = em.getComponent(entity, Velocity::class.java)
            animation.stateTime += dt
            val idx = if (velocity.moving) animation.stateTime else 0.15f
            appearance.texture = when (velocity.dir) {
                FacingDirection.UP -> animation.up.getKeyFrame(idx)
                FacingDirection.DOWN -> animation.dn.getKeyFrame(idx)
                FacingDirection.LEFT -> animation.lt.getKeyFrame(idx)
                FacingDirection.RIGHT -> animation.rt.getKeyFrame(idx)
            }
        }
    }
}
