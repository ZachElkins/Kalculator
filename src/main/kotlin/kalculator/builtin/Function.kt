package kalculator.builtin

abstract class Function(val name: String) {
    abstract fun arity(): Number
    abstract fun call(arguments: List<Any?>): Any?
    override fun toString(): String {
        return "<native fn: '${name}'>"
    }
}