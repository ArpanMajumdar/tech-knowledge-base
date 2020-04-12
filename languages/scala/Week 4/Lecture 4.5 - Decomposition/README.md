# Decomposition

Suppose we want to write a small inpterpreter for arithmatic expressions. Let's restrict ourselves to numbers and additions. Expressions can be represented as a class hierarchy, with base trait `Expr` and two subclasses `Sum` and `Number`. To treat an expression, we need to know the expression's shape and its components.

``` scala
trait Expr {
  def isNumber: Boolean
  def isSum: Boolean
  def numValue: Int
  def leftOp: Expr
  def rightOp: Expr
}

class Number(n: Int) extends Expr {
  override def isNumber: Boolean = true

  override def isSum: Boolean = false

  override def numValue: Int = n

  override def leftOp: Expr = throw new Error("Number.leftOp is not defined")

  override def rightOp: Expr = throw new Error("Number.rightOp is not defined")
}

class Sum(e1: Expr, e2: Expr) extends Expr {
  override def isNumber: Boolean = false

  override def isSum: Boolean = true

  override def numValue: Int = throw new Error("Sum.numValue is not defined")

  override def leftOp: Expr = e1

  override def rightOp: Expr = e2

}

object test {

  def eval(e: Expr): Int = {
    if (e.isNumber) e.numValue
    else if (e.isSum) eval(e.leftOp) + eval(e.rightOp)
    else throw new Error("Unrecognized expression" + e)
  }
}
```

But problem with this approach is writing all the classifictaion and accessor methods is tedious.

## Adding new expressions
So, what happens if we want to add new expressions.
e.g.- 
``` scala
class Prod(e1: Expr, e2: Expr) extends Expr
class Var(name: String) extends Expr
```
We will need to add methods for classification and access to all the classes defined above.

## Solution 1 (Hacky): Type tests and type casts
A hacky solution could use type tests and type casts. Scala let's you do this using methods defined in class `Any`.
``` scala
def isInstanceOf[T]: Boolean // Checks whether an object's type conforms to T
def asInstanceOf[T]: T       // Threats this object as instance of T, throws ClassCastException if it isn't
```
But their use in scala is discouraged. This is how we can do it.
``` scala
def eval(e: Expr): Int = {
if (e.isInstanceOf[Number]) e.asInstanceOf[Number].numValue
else if (e.isInstanceOf[Sum])
    eval(e.asInstanceOf[Sum].leftOp) + eval(e.asInstanceOf[Sum].rightOp)
else throw new Error("Unrecognized expression" + e)
}
```

## Solution 2: Object oriented decomposition
For example all you want to do is evaluate expressions, we can define
``` scala
trait Expr {
  def eval: Int
}

class Number(n: Int) extends Expr {
  override def eval: Int = n
}

class Sum(e1: Expr, e2: Expr) extends Expr {
  override def eval: Int = e1.eval + e2.eval
}
```
But what happens if we want to display expressions now? We have to define new methods in all subclasses.

