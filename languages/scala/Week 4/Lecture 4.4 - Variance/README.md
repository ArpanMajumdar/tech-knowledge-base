# Variance

We have seen that some types must be covariant and other types should not.
Roughly speaking, a type that allows mutations of its elements should not be covariant. But immutable types, can be covariant, if some condotions are met.

## Definition of variance
Say `C[T]` is a parametrized type and `A`, `B` are types such that `A <: B`.
In general, there are three possible relationships between `C[A]` and `C[B]`.
1. `C[A] <: C[B]`                                   : `C` is covariant
2. `C[A] >: C[B]`                                   : `C` is contravariant
3. Neither `C[A]` nore `C[B]` is a subtype of other : `C` is nonvariant

Scala lets you declare the variance of a type by annotating the type parameter.
1. 
``` scala
class C[+A] {...} // C is covariant
class C[-A] {...} // C is contravariant
class C[A] {...}  // C is nonvariant
```

## Typing rules for functions
Generally, we have the following rule for subtyping between function types.

If `A2 <: A1` and `B1 <: B2`, then `A1 => B1 <: A2 => B2`.

## Function trait declaration
So, functions are **contravariant** in their argument types, and **covariant** in their result type. This leads to the following revised definition if the `Function1` trait.
``` scala
package scala
trait Function1[-T,+U] {
    def apply(x: T): U
}
```

## Variance checks
We have seen in the array example that the combination of covariance with certain operations is unsound. In this case, the problematic operation was the update operation on an array.

If we turn Array into a class, and update into a method, it would look like this.
``` scala
class Array[+T] {
    def update(x: T)
}
```
The problematic combination is the covariant type parameter `T` which appears in the parameter position of method update.

## Covariance example
What if we want to use a `Nil` object instead of a `Nil` class as there is only one instance of `Nil`.

``` scala
trait List[+T] {
  def isEmpty: Boolean
  def head: T
  def tail: List[T]
}

class Cons[T](val head: T, val tail: List[T]) extends List[T] {
  override def isEmpty: Boolean = false
}

object Nil extends List[Nothing] {
  override def isEmpty: Boolean = true

  override def head: Nothing = throw new NoSuchElementException("Nil.head")

  override def tail: List[Nothing] =
    throw new NoSuchElementException("Nil.tail")
}

object Test {
  val x: List[String] = Nil
}
```

## Lower bounds
But prepend is a natural method to have on immutable lists. How to make it variance corrent?

We can use a lower bound.
``` scala
def prepend[U >: T](elem: U): List[U] = new Cons(elem, this)
```

This passes varince checks as 
- covariant type parameters may appear in the lower bounds of method type parameters.
- contravariant type parameters may appear in the upper bounds of the method.
