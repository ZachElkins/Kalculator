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

        return call()
    }

    private fun call(): Expr {
        val expr: Expr = primary()
        if (match(LEFT_BRACE)) {
            val arguments: MutableList<Expr> = ArrayList()
            if (!check(RIGHT_BRACE)) {
                do {
                    arguments.add(expression())
                } while (match(COMMA))
            }

            consume(RIGHT_BRACE, "Expected '}' after argument list")

            return Expr.Call(callee=expr, arguments=arguments)
        }

        return expr
    }

    private fun primary(): Expr {
        if (match(NUMBER)) {
            val literal = Expr.Literal(value = previous().literal)
            if (check(LEFT_PAREN) || check(IDENTIFIER)) return implicitMultiplication(expr=literal)
            return Expr.Literal(value = previous().literal)
        }

        if (match(LEFT_PAREN)) {
            val expr = expression()
            consume(RIGHT_PAREN, "Expected ')' after expression.")
            val grouping = Expr.Grouping(expr)
            if (check(LEFT_PAREN) || check(NUMBER) || check(IDENTIFIER)) return implicitMultiplication(expr=grouping)
            return grouping
        }

        if (match(IDENTIFIER)) {
            val variable = Expr.Variable(previous())
            if (check(LEFT_PAREN) || check(NUMBER)) return implicitMultiplication(expr=variable)
            return variable
        }
        throw ParseError(message="Expected expression ${errorLocation()}.")
    }

    private fun implicitMultiplication(expr: Expr): Expr {
        val operator = Token(STAR, "*", null)
        return Expr.Binary(expr, operator, term())
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