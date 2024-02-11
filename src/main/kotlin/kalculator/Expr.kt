package kalculator

abstract class Expr {

    interface Visitor<T> {
        fun visitLiteralExpr(expr: Literal): T
        fun visitBinaryExpr(expr: Binary): T
        fun visitUnaryExpr(expr: Unary): T
        fun visitGroupingExpr(expr: Grouping): T
        fun visitVariableExpr(expr: Variable): T
    }

    abstract fun <T>accept(visitor: Visitor<T>): T

    data class Literal(val value: Any?): Expr() {
        override fun <T>accept(visitor: Visitor<T>): T {
            return visitor.visitLiteralExpr(this)
        }
    }

    data class Binary(val left: Expr, val operator: Token, val right: Expr): Expr() {
        override fun <T>accept(visitor: Visitor<T>): T {
            return visitor.visitBinaryExpr(this)
        }
    }

    data class Unary(val operator: Token, val right: Expr): Expr() {
        override fun <T>accept(visitor: Visitor<T>): T {
            return visitor.visitUnaryExpr(this)
        }
    }

    data class Grouping(val expression: Expr): Expr() {
        override fun <T>accept(visitor: Visitor<T>): T {
            return visitor.visitGroupingExpr(this)
        }
    }

    data class Variable(val name: Token): Expr() {
        override fun <T>accept(visitor: Visitor<T>): T {
            return visitor.visitVariableExpr(this)
        }
    }
}