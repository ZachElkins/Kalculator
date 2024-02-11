package kalculator

import java.lang.RuntimeException

open class CalculatorError(message: String): RuntimeException(message)
class TokenError(message: String): CalculatorError(message)
class ParseError(message: String): CalculatorError(message)
class InterpretError(message: String): CalculatorError(message)
class VarError(message: String): CalculatorError(message)
