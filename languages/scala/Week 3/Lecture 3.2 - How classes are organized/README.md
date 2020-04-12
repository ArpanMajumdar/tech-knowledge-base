# How classes are organized

## Packages
Classes and objects are organized in packages. To place an class or object inside a package, use `package` clause at the top of your source file.

Example - 
``` scala
package progfun.examples

object Hello { ... }
```

This would place class `Hello` in package `progfun.examples`. You can then refer to hello using its fully qualified name `progfun.examples.Hello`.

## Forms of imports
To import everything from package, we can use

``` scala
import progfun.examples.Rational            // Imports just Rational
import progfun.examples.{Rational,Hello}    // Imports both Rational and Hello
import progfun.examples._                   // Imports everything in package progfun.examples
```

- First two forms are called **named imports** 
- Last one is called a **wildcard import**. 
- You can import from either a package or object.
- All members of package `scala`, `java.lang` and 

## Automatic imports
Some entities are automatically imported in any scala program. These are:
- All members of package `scala`
- All members of package `java.lang`
- All members of singleton object `scala.Predef`.

Here are the fully qualified names of some auto-imported types and functions.
``` scala
Int         scala.Int
Boolean     scala.Boolean
Object      java.lang.Object
require     scala.Predef.require
assert      scala.Predef.assert
```

## Traits
In Scala like in Java, a class can have only one superclass. But what if a class has several natural supertypes to which it conforms or from which it wants to inherit code?

To solve this problem, we can use `Traits`. 
A `trait` can be declared like an abstract class, just with trait instead of `abstract class`.

``` scala
trait Planar {
    def height: Int
    def Width: Int
    def surface: height * width
}
```

Classes, objects and traits can inherit from atmost one class but arbitrarily many traits.

Example - 
``` scala
class Square extends Shape with Planar with Movable ...
```

- Traits resemble interfaces in Java, but are more powerful because they can contain fields and concrete methods.
- On the other hand, traits cannot have value parameters, only classes can.
- We can give default implementations of methods in traits.

## Scala class hierarchy
![Scala class hierarchy](https://scala-lang.org/files/archive/spec/2.12/public/images/classhierarchy.png)

### Top types
At the top of the hierarchy, we find:

| Type | Explanation
|---|---
| Any | The base type of all types. Methods: `==`, `!=`, `equals`, `hashCode` and `toString`.
| AnyRef | The base type of all reference types. Alias of `java.lang.Object`.
| AnyVal | The base type of all primitive types.

### The `Nothing` type
Nothing is at the bottom of the scala's type hierarchy. It Is a subtype of every other type. There is no value of type Nothing. 

Why is that useful?
- To signal abnormal termination
- As an element type of empty collections. e.g. - `Set[Nothing]`

### The `Null` type
Every reference class type also has `null` as a value. The type of `null` is `Null`. `Null` is the subtype of every class that inherits from `Object`. It is incompatible with subtypes of `AnyVal`.
``` scala
val x = null            // x: Null
val y: String = null    // y: String
val z: Int = null       // error: Type mismatch
```

## Exceptions
Scala's exception handling is similar to Java's. The expression 
``` scala
throw Exception
```
aborts evaluation with `Exception`. The type of exception is nothing.
