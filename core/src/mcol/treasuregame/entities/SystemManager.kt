package mcol.treasuregame.entities

import mcol.treasuregame.entities.systems.*

class SystemManager(val em: EntityManager, map: Map) {

    private val systems = listOf(
            AnimationSystem(),
            CameraSystem(),
            CollisionSystem(),
            DestructionSystem(map),
            DurationSystem(),
            MovementSystem(map),
            NavigationSystem(map)
    )

    fun update(dt: Float) {
        systems.forEach { it.update(em, dt) }
    }
}
