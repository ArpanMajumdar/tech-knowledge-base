# Pattern matching

## Functional decomposition with pattern matching
**Observation**

The sole purpose of test and accessor methods is to reverse the construction process.
- Which subclass was used?
- What were the arguments of the constructor?

This situation is so common that many functional languages automate it.

## Case classes
A **case class** definition is similar to a normal class definition, except that it is preceded by a modifier `case`. For example:
``` scala
trait Expr
case class Number(n: Int) extends Expr
case class Sum(e1: Int, e2: Int) extends Expr
```
It defines a trait `Expr` and two concrete subclasses `Number` and `Sum`.

Defining case classes also implicitly defines companion objects with apply methods.
``` scala
object Number {
    def apply(n: Int) = new Number(n)
}

object Sum {
    def apply(e1: Expr, e2: Expr) = new Sum(e1, e2)
}
```

So, we can write `Number(1)` instead of `new Number(1)`.

However, these classes are empty. How can we access the members?

## Pattern matching
Pattern matching is a generalization of switch from C/Java to class hierarchies. It's expressed in Scala using the keyword `match`.

Example
``` scala
def eval(e: Expr): Int = e match {
    case Number(n) => n
    case Sum(e1,e2) => eval(e1) + eval(e2)
}
```

### Match syntax
Rules
- `match` is followed by a sequence of cases, `pat => expr`
- Each case associates an **expression** with a **pattern**
- A `MatchError` exception is thrown if no pattern matches the value of the selector

``` scala
e match {
    case pat1 => expr1
    case pat2 => expr2
    ...

    case patn => exprn
}
```

### Forms of patterns
Patterns are constructed from
- constructors e.g. - `Number`, `Sum`
- variables e.g. - n, e1, e2
- wildcard patterns e.g. - `_`
- constants e.g. - `1`,`true`

Variables always begin with a lowercase letter. 

Same variable name can occur only once in a pattern. So, `Sum(x, x)` is not a legal pattern.

Names of constants begins with a capital letter.

### Pattern matching and methods
Of course, it is also possible to define the evaluation function as a method of the base trait.
```scala
trait Expr {
    def eval: Int = this match {
        case Number(n) => n
        case Sum(e1, e2) => e1.eval + e2.eval
    }
}
```

