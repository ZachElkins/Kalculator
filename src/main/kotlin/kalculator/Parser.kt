package kalculator

import kalculator.TokenType.*

class Parser (tokens: List<Token>) {
    val tokens = tokens
    var current = 0

    fun parse(): List<Expr> {
        val expressions: MutableList<Expr> = ArrayList()

        while (!atEnd()) {
            expressions.add(expression())
        }

        return expressions
    }

    private fun expression(): Expr {
        return term()
    }

    private fun term(): Expr {
        var expr = factor()
        while (match(CARROT, MINUS, PLUS)) {
            val operator = previous()
            val right = factor()
            expr = Expr.Binary(expr, operator, right)
        }

        return expr
    }

    private fun factor(): Expr {
        var expr = unary()

        while (match(SLASH, STAR)) {
            val operator= previous()
            val right = unary()
            expr = Expr.Binary(expr, operator, right)
        }

        return expr
    }

    private fun unary(): Expr {
        if (match(MINUS)) {
            val operator = previous()
            val right = unary()
            return Expr.Unary(operator, right)
        }

        return primary()
    }

    private fun primary(): Expr {
        if (match(NUMBER)) {
            return Expr.Literal(value = previous().literal)
        }

        if (match(LEFT_PAREN)) {
            val expr = expression()
            consume(RIGHT_PAREN, "Expected ')' after expression.")
            return Expr.Grouping(expr)
        }

        if (match(IDENTIFIER)) {
            return Expr.Variable(previous())
        }
        throw ParseError(message="Expected expression ${errorLocation()}.")
    }

    private fun match(vararg types: TokenType): Boolean {
        for (type in types) {
            if (check(type)) {
                advance()
                return true
            }
        }
        return false
    }

    private fun consume(type: TokenType, message: String): Token {
        if (check(type)) return advance()
        throw ParseError(message=message)
    }

    private fun check(type: TokenType): Boolean {
        if (atEnd()) return false
        return peek().type == type
    }

    private fun peek(): Token {
        return tokens[current]
    }

    private fun previous(): Token {
        return tokens[current - 1]
    }

    private fun advance(): Token {
        if (!atEnd()) current++
        return previous()
    }

    private fun atEnd(): Boolean {
        return current > tokens.size-1
    }

    private fun errorLocation(): String {
        when (atEnd()) {
            true -> return "after '${previous().lexeme}'"
            false -> return "at '${peek().lexeme}'"
        }
    }
}