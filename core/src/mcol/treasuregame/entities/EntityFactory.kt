package mcol.treasuregame.entities

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.g2d.Animation
import com.badlogic.gdx.maps.MapProperties
import mcol.treasuregame.TreasureGame
import mcol.treasuregame.entities.components.*
import mcol.treasuregame.gfx.CameraShake

class EntityFactory(private val em: EntityManager) {

    fun createBomb(x: Float, y: Float) {
        val id = em.createEntity()
        em.add(id, AnimationComponent(Assets.bombTexture, 0.2f,
                                      Animation.PlayMode.LOOP))
        em.add(id, Appearance())
        em.add(id, Duration(1.2f, Explosion(3)))
        em.add(id, MiniMappable(Color.BLACK, 1f))
        em.add(id, Position(x, y))
    }

    fun createExplosion(x: Float, y: Float, radius: Int) {
        val id = em.createEntity()
        em.add(id, AnimationComponent(Assets.explTexture, 0.1f))
        em.add(id, Appearance(size = SIZE * radius, offset = -radius / 2))
        em.add(id, Destructor(radius, fog = true, obstacles = true))
        em.add(id, Duration(0.4f))
        em.add(id, Position(x, y))
        CameraShake.set(0.4f, 0.2f * radius)
    }

    fun createHurricane(x: Float, y: Float) {
        val id = em.createEntity()
        em.add(id, AnimationComponent(Assets.hurrTexture, 0.1f,
                                      Animation.PlayMode.LOOP))
        em.add(id, Appearance())
        em.add(id, Destructor(1, fog = true))
        em.add(id, Duration(20f))
        em.add(id, MiniMappable(Color.BLUE, 1f))
        em.add(id, Position(x, y))
        em.add(id, RandomMovement())
        em.add(id, Velocity())
    }

    fun createItem(mp: MapProperties) {
        val type = mp.get("type", String::class.java)
        val id = em.createEntity()
        var x = -1f
        var y = -1f
        try {
            x = mp.get("x", Float::class.javaPrimitiveType) * TreasureGame.SCALE
            y = mp.get("y", Float::class.javaPrimitiveType) * TreasureGame.SCALE
            val value = mp.get("value", Int::class.javaPrimitiveType) ?: 0
            val texture = when (type) {
                "sweet" -> Assets.itemTexture[4][2]
                "bomb" -> Assets.bombTexture[0]
                "hurricane" -> Assets.hurrTexture[2]
                else -> {
                    Gdx.app.log("createItem", "Ignored $type $x $y")
                    null
                }
            }
            em.add(id, Appearance(texture))
            em.add(id, Collectable(type, value))
            em.add(id, CollisionBounds(x, y, SIZE))
            em.add(id, MiniMappable(Color.GOLD, 1.5f, true))
            em.add(id, Position(x, y))

        } catch (e: Exception) {
            e.printStackTrace()
            Gdx.app.log("createItem", "$type $x $y")
        }
    }

    fun createLamb(x: Float, y: Float): Long {
        val id = em.createEntity()
        em.add(id, Appearance())
        em.add(id, CollisionBounds(x, y, SIZE))
        em.add(id, Destructor(1, fog = true))
        em.add(id, MiniMappable(Color.WHITE, 1f))
        em.add(id, Navigation(x, y))
        em.add(id, Position(x, y))
        em.add(id, Velocity(3 * TreasureGame.TILESIZE))
        em.add(id, WalkingAnimation(Assets.lambTexture[0], Assets.lambTexture[1],
                                    Assets.lambTexture[2], Assets.lambTexture[3],
                                    0.1f))
        return id
    }

    fun createMessage(msg: String, x: Float, y: Float) {
        val id = em.createEntity()
        em.add(id, BubbleMovement())
        em.add(id, Duration(2.5f))
        em.add(id, Message(msg))
        em.add(id, Position(x + TreasureGame.TILESIZE * 0.5f,
                            y + TreasureGame.TILESIZE))
    }

    fun createPlayer(x: Float, y: Float, character: Int): Long {
        val row = character * 4
        val id = em.player
        em.add(id, Appearance())
        em.add(id, Collector())
        em.add(id, CollisionBounds(x, y, SIZE))
        em.add(id, Destructor(1, fog = true))
        em.add(id, MiniMappable(Color.RED, 2f))
        em.add(id, Navigation(x, y))
        em.add(id, Position(x, y))
        em.add(id, Velocity(4 * TreasureGame.TILESIZE))
        em.add(id, WalkingAnimation(Assets.playTexture[row],
                                    Assets.playTexture[row + 1],
                                    Assets.playTexture[row + 2],
                                    Assets.playTexture[row + 3], 0.15f))
        return id
    }

    fun createTargetIndicator() {
        val id = em.createEntity()
        em.add(id, AnimationComponent(Assets.trgtTexture, 0.15f,
                                      Animation.PlayMode.LOOP_PINGPONG))
        em.add(id, Appearance())
        em.add(id, Position(0f, 0f))
        em.add(id, TargetIndicator())
    }

    companion object {
        /** Renderable size of each entity. */
        private const val SIZE = 32 * TreasureGame.SCALE
    }
}
