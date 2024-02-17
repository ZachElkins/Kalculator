package kalculator

class Environment {
    val values: MutableMap<String, Any?> = mutableMapOf()

    fun get(name: Token): Any? {
        values[name.lexeme]?.let {
            return values[name.lexeme]
        }?: run {
            throw VarError(message="Undefined variable ${name.lexeme}.")
        }
    }

    fun assign(name: Token, value: Any) {
        assign(name.lexeme, value)
    }

    fun assign(name: String, value: Any) {
        values.put(name, value)
    }
}