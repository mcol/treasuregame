package mcol.treasuregame.entities

import mcol.treasuregame.entities.components.Component
import java.util.*

class EntityManager {

    val player: Long = 1
    private var lastEntityID: Long = 2
    private val allEntities: MutableList<Long> = LinkedList()
    val factory = EntityFactory(this)
    val components = HashMap<Class<*>, HashMap<Long, Component>>()

    fun <T : Component> add(entity: Long, component: T) {
        val store = components[component.javaClass] ?: HashMap()
        store[entity] = component
        components[component.javaClass] = store
    }

    fun remove(entity: Long) {
        for (component in components.values)
            component.remove(entity)
        allEntities.remove(entity)
    }

    fun <T : Component> getComponent(entity: Long, type: Class<T>): T {
        val store = components[type] ?: HashMap()
        return type.cast(store[entity]) ?: throw IllegalArgumentException("$entity does not possess Component of class $type")
    }

    fun <T : Component> hasComponent(entity: Long, type: Class<T>): Boolean {
        val store = components[type] ?: HashMap()
        return type.cast(store[entity]) is T
    }

    fun <T : Component> getUniqueComponent(type: Class<T>): T {
        val store = components[type] ?: throw IllegalArgumentException("No components of class $type")
        return type.cast(ArrayList(store.values)[0])
    }

    fun <T : Component> getEntitiesWith(componentType: Class<T>): MutableList<Long> {
        val store = components[componentType] ?: return LinkedList()
        return store.keys.toMutableList()
    }

    fun <T : Component> getUniqueEntityWith(componentType: Class<T>): Long {
        val store = components[componentType] ?: return 0
        return store.keys.toLongArray()[0]
    }

    fun createEntity(): Long {
        val id = generateEntityID()
        if (id < 1)
            return 0
        allEntities.add(id)
        return id
    }

    private fun generateEntityID(): Long {
        // prevent it generating two entities with same ID at once
        synchronized(this) {
            if (lastEntityID < Long.MAX_VALUE)
                return lastEntityID++
            for (i in 1 until Long.MAX_VALUE) {
                if (!allEntities.contains(i))
                    return i
            }

            throw Error("ERROR: no available Entity IDs; too many entities!")
        }
    }

    fun show() {
        for (key in components.keys) {
            println("Entities with $key")
            println(components[key]?.keys)
        }
    }
}
