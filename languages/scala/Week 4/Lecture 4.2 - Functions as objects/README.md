# Functions as objects
We have seen that scala's numeric and boolean types can be implemented like normal classes. But what about functions?
In fact function values are treated as objects in scala. The function type `A => B` is just an abbreviation for the class `scala.Function1[A, B]`, which is roughly defined as follows.

``` scala
package scala

trait Function1[A , B] {
    def apply(x: A): B
}
```

So, functions are objects with apply methods. There are also traits `Function2`, `Function3` for functions which take more parameters (currently upto 22).

## Expansion of function values
An anonymous function such as
``` scala
(x: Int) => x * x
```
is expanded to
``` scala
{
    class AnonFun extends Function1[Int, Int] {
        def apply(x: Int) = x * x
    }
    new AnonFun
}
```
or shorter using the Anonymous class syntax.
``` scala
new Function1{
    def apply(x: Int) = x * x
}
```
Java and Scala have same syntax for anonymous functions.

## Functions and methods
Note that a method such as 
``` scala
def f(x: Int): Boolean = ...
```
is not itself a function value.
But if `f` is used in a place where a `Function` type is expected, it is converted automatically to the function value.
``` scala
(x: Int) => f(x)
```
or expanded
``` scala
new Function1[Int, Boolean]{
    def apply(x: Int) = f(x)
}
```
This expansion of `def` is called `eta-expansion`.

## Exercise
Create a `List` class and create methods to create lists of 0 and 2 parameters.

``` scala
trait List[T] {
  def isEmpty: Boolean
  def head: T
  def tail: List[T]
}

class Cons[T](val head: T, val tail: List[T]) extends List[T] {
  override def isEmpty: Boolean = false
}

class Nil[T] extends List[T] {
  override def isEmpty: Boolean = true

  override def head: T = throw new NoSuchElementException("Nil.head")

  override def tail: List[T] = throw new NoSuchElementException("Nil.tail")
}

object List {
  // List(1,2) = List.apply(1,2)
  def apply[T](x1: T, x2: T): List[T] =
    new Cons[T](x1, new Cons[T](x2, new Nil[T]))

  // List() = List.apply
  def apply[T](): List[T] = new Nil[T]
}
```