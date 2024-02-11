# Kalculator
A math language proof of concept that can handle basic equations.

_Inspired by [Crafting Interpreters' JLox](https://github.com/munificent/craftinginterpreters)_

## Usage

### Compile
```agsl
kotlinc src/main/kotlin/kalculator/* -include-runtime -d ./build/kalculator.jar
```
### Run

#### Single equation
```agsl
java -jar ./build/kalculator.jar
```

#### REPL

```agsl
java -jar ./build/kalculator.jar [equation]
```

### Arguments

| Flag      | Shorthand | Operation         | 
|-----------|-----------|-------------------|
| `--debug` | `-d`      | Run in debug mode |

## Syntax
| Operation      | Syntax    |
|----------------|-----------|
| Addition       | `n + n`   |
| Subtraction    | `n - n`   |
| Division       | `n / n`   |
| Multiplication | `n * n`   |
| Ordering       | `( ... )` |