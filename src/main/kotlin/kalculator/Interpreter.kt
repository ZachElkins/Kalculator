package kalculator

import kalculator.TokenType.*

class Interpreter: Expr.Visitor<Any?> {

    fun interpret(expressions: List<Expr>) {
        for (expression in expressions) {
            execute(expression)
        }
    }

    private fun execute(expr: Expr) {
        val output = evaluate(expr)
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
}