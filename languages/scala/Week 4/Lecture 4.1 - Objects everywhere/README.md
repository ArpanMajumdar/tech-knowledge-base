#  Objects everywhere

## Pure object orientation
A pure object oriented language is the one where every value is object. If language is based on classes, this means that type of each value is a class.

## Standard classes
Conceptually, classes `Int` and `Boolean` do not receive any special treatment in Scala. They are like other classes defined in Scala.
However, for reasons of efficiency, scala compiler represents the values of type `scala.Int` by 32-bit integers and `scala.Boolean` by Java's booleans.

## Pure booleans
The boolean type maps to JVM's primitive type boolean. But one could define it as a class from first principles.

``` scala
package idealized.scala

abstract class Boolean {
    def ifThenElse[T](t: => T,e: => T):T
    def && (x: => Boolean) : Boolean = ifThenElse(x,false)
    def || (x: => Boolean) : Boolean = ifThenElse(true,x)
    def unary_! : Boolean            = ifThenElse(false, true)
    def == (x: => Boolean) : Boolean = ifThenElse(x, x.unary_!)
    def != (x: => Boolean) : Boolean = ifThenElse(x.unary_!, x)
}
```

Then `if(cond) te else ee` can be translated to `cond.ifThenElse(te,ee)`.

We still have to define `true` and `false` as objects of type boolean.

``` scala
package idealized.scala

object true extends Boolean {
    def ifThenElse[T](t: => T,e: => T) : T = t
}

object false extends Boolean {
    def ifThenElse[T](t: => T,e: => T) : T = e
}
```
