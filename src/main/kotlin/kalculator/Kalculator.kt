package kalculator

import kotlin.system.exitProcess

var DEBUG = false
var VAR_NAME = "x"

fun main(args: Array<String>) {
    val equation: String? = if (args.isNotEmpty()) handleArgs(args) else null

    equation?.let {
        runOnce(equation)
    } ?: run {
        prompt()
    }
}

fun handleArgs(args: Array<String>): String? {
    var drop = 0
    if (args[0] in listOf("-d", "--debug")) {
        DEBUG = true
        drop++
    }

    return if (drop < args.size) args.drop(drop).joinToString("") else null
}

fun runOnce(equation: String) {
    try {
        run(equation)
    } catch (e: CalculatorError) {
        println("$e\nUsage: kalculator.jar [equation]")
        exitProcess(64)
    }
}

fun prompt() {
    while(true) {
        try {
            val line: String = readln()
            run(source=line)
        } catch (e: CalculatorError){
            println("Error parsing equation: ${e.message}")
        }
    }
}

val interpreter = Interpreter()

fun run(source: String) {
    val scanner = Scanner(source=source)
    val tokens = scanner.scan()
    val parser = Parser(tokens=tokens)
    val expressions = parser.parse()
    interpreter.interpret(expressions=expressions)

    if (!DEBUG) return
    val printer = AstPrinter()
    for (expr in expressions) {
        printer.printExpr(expr)
    }
}

