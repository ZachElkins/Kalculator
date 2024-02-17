package kalculator.builtin

import kotlin.math.pow

class Root: Function("root") {
    override fun arity(): Number {
        return 2
    }

    override fun call(arguments: List<Any?>): Any {
        return (arguments[0] as Double).pow(1/arguments[1] as Double)
    }
}