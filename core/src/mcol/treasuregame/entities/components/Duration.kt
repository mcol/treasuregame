package mcol.treasuregame.entities.components

class Duration(val duration: Float,
               val nextEvent: Component = object: Component {}) : Component {

    /** Time since the creation of the component. */
    var stateTime = 0f
}
