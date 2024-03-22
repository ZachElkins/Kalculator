package kalculator

import kalculator.TokenType.*

class Scanner(source: String) {
    val source = source
    val tokens: MutableList<Token> = ArrayList()
    var start = 0
    var current = 0

    companion object {
        val tokenMap: Map<Char, TokenType> = mapOf(
            '^' to CARROT,
            '-' to MINUS,
            '+' to PLUS,
            '/' to SLASH,
            '*' to STAR,
            '(' to LEFT_PAREN,
            ')' to RIGHT_PAREN,
            '{' to LEFT_BRACE,
            '}' to RIGHT_BRACE,
            ',' to COMMA,
            '|' to PIPE,
        )
    }

    fun scan(): List<Token> {
        while (!atEnd()){
            start = current
            val c = advance()
            when (c) {
                in tokenMap.keys -> addToken(tokenMap[c]!!)
                in '0'..'9' -> scanNumber()
                in 'a'..'z' -> scanVariable()
                ' ' -> continue
                else -> throw TokenError(message="Unexpected character $c at place $current.")
            }
        }
        return tokens
    }

    private fun scanNumber() {
        while (peek().isDigit()) advance();

        if (peek() == '.' && (peekNext().isDigit())) {
            advance()
            while (peek().isDigit()) advance()
        }

        addToken(tokenType=NUMBER, literal=source.substring(start, current).toDouble())
    }

    private fun scanVariable() {
        while (peek().isLetter()) advance()

        addToken(tokenType=IDENTIFIER, literal=source.substring(start, current))
    }

    private fun addToken(tokenType: TokenType, literal: Any?) {
        val text: String = source.substring(start..current-1)
        tokens.add(Token(type=tokenType, lexeme=text, literal=literal))
    }

    private fun addToken(tokenType: TokenType) {
        addToken(tokenType=tokenType, literal=null)
    }

    private fun advance(): Char {
        return source[current++]
    }

    private fun peek(): Char {
        if (atEnd()) return '\u0000'
        return source[current]
    }

    private fun peekNext(): Char {
        return source[current + 1]
    }

    private fun atEnd(): Boolean {
        return current >= source.length || source[current] == '\n'
    }
}