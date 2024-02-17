package kalculator

import kalculator.TokenType.*
import kalculator.builtin.Function
import kalculator.builtin.Root
import kotlin.math.pow

class Interpreter: Expr.Visitor<Any?> {
    val environment = Environment()

    constructor() {
        environment.assign("root", Root())
    }

    fun interpret(expressions: List<Expr>) {
        for (expression in expressions) {
            execute(expression)
        }
    }

    private fun execute(expr: Expr) {
        val output = evaluate(expr)
        environment.assign(Token(type=IDENTIFIER, lexeme=VAR_NAME, literal=null), output!!)
        println("= $output")
    }

    private fun evaluate(expr: Expr): Any? {
        return expr.accept(this)
    }

    override fun visitLiteralExpr(expr: Expr.Literal): Any? {
        return expr.value
    }

    override fun visitBinaryExpr(expr: Expr.Binary): Any {
        val left = evaluate(expr.left)
        val right = evaluate(expr.right)
        when (expr.operator.type) {
            CARROT -> return (left as Double).pow(right as Double)
            PLUS -> return left as Double + right as Double
            MINUS -> return left as Double - right as Double
            SLASH -> return left as Double / right as Double
            STAR -> return left as Double * right as Double
            else -> throw InterpretError(message="Error evaluating $left ${expr.operator.lexeme} $right")
        }
    }

    override fun visitUnaryExpr(expr: Expr.Unary): Any {
        val right = evaluate(expr.right)
        when (expr.operator.type) {
            MINUS -> return -1 * (right as Double)
            else -> throw InterpretError(message="Error evaluating ${expr.operator.lexeme} $right")
        }
    }

    override fun visitGroupingExpr(expr: Expr.Grouping): Any? {
        return evaluate(expr.expression)
    }

    override fun visitVariableExpr(expr: Expr.Variable): Any? {
        return environment.get(expr.name)
    }

    override fun visitCallExpr(expr: Expr.Call): Any? {
        val callee: Any? = evaluate(expr.callee)
        val arguments: MutableList<Any?> = ArrayList()
        for (argument in expr.arguments) {
            arguments.add(evaluate(argument))
        }
        if (callee !is Function)
            throw InterpretError("Can only call functions")

        if (arguments.size != callee.arity())
            throw InterpretError("Expected ${callee.arity()} arguments but received ${arguments.size}.")

        return callee.call(arguments)
    }
}