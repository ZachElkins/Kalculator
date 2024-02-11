package kalculator

class AstPrinter: Expr.Visitor<String> {

    fun printExpr(expr: Expr) {
        println(expr.accept(this))
    }

    override fun visitLiteralExpr(expr: Expr.Literal): String {
        return expr.value.toString()
    }

    override fun visitBinaryExpr(expr: Expr.Binary): String {
        return "(${expr.operator.lexeme} ${expr.left.accept(this)}, ${expr.right.accept(this)})"
    }

    override fun visitUnaryExpr(expr: Expr.Unary): String {
        return "(${expr.operator.lexeme} ${expr.right.accept(this)})"
    }

    override fun visitGroupingExpr(expr: Expr.Grouping): String {
        return "(group ${expr.expression.accept(this)})"
    }
}