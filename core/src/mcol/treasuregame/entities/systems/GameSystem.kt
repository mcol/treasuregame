package mcol.treasuregame.entities.systems

import mcol.treasuregame.entities.EntityManager

interface GameSystem {
    fun update(em: EntityManager, dt: Float)
}
