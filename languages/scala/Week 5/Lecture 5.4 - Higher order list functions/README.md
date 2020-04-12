# Higher order list functions

## Recurring patterns for computations on lists

The examples have shown that functions on lists have similar structures. We can identify several recurring patterns like 
- Transforming each element in a list in a certain way
- Retrieving a list of all elements satisfying a criterion
- Combining the elements of a list using an operator

Functional languages allow programmers to write generic functions that implement patterns such as these using **higher-order functions**.

## Applying a function to elements of a list

A common operation is to transform each element of a list and then return the list of results. For example, to multiply each element of a list by the same factor, you could write

``` scala
def scaleList(xs: List[Double], factor: Double):List[Double] = xs match {
    case Nil => xs
    case y :: ys => y * factor :: scaleList(ys, factor)
}
```

## Map

This scheme can be generalized to method `map` of `List` class. A simple way to define map is as follows:

``` scala
abstract class List[T] {
    def map[U](f: T => U): List[U] = this match {
        case Nil => this
        case y :: ys => f(y) :: xs.map(f)
    }
}
```
Actual definition of `map` is a bit more complicated, because it is tail recursive and also because it works for arbitrary collections, not just lists.

Using map, `scaleList` can be written concisely as 
``` scala
def scaleList(xs: List[Double], factor: Double) = xs map (x => x * factor)
```

## Filtering
Another common operation on  lists is the selection of the elements satisfying a given condition. For example:
``` scala
def posElements(xs: List[Int]): List[Int] = xs match {
    case Nil => xs
    case y :: ys => if(y > 0) y :: posElements(ys) else posElements(ys)
}
```

This pattern is generalized by the method `filter` of the list class.
``` scala
abstract class List[T] {
    def filter[U](p: T => Boolean): List[U] = this match {
        case Nil => this
        case y :: ys => if(p(x)) x :: xs.filter(p) else xs.filter(p)
    }
}
```

Using filter, `posElements` can be written more concisely.
``` scala
def posElements(xs: List[Int]): List[Int] = xs.filter(x => x > 0)
```

## List functions

``` scala
object ListFun extends App {
  val nums = List(2, 6, 3, -1, -2, 4)
  val fruits = List("apple", "pineapple", "banana", "pear")

  println(nums filter (x => x > 0))      // List(2, 6, 3, 4)List(2, 6, 3, 4)
  println(nums filterNot (x => x > 0))   // List(-1, -2)
  println(nums partition (x => x > 0))   // (List(2, 6, 3, 4),List(-1, -2))

  println(nums takeWhile (x => x > 0))   // List(2, 6, 3)
  println(nums dropWhile (x => x > 0))   // List(-1, -2, 4)
  println(nums span (x => x > 0))        // (List(2, 6, 3),List(-1, -2, 4))
}
```

## Exercise

Write a function pack that packs consecutive duplicates oflist elements of elements into sublists. For instance
``` scala
pack(List("a", "a", "a", "b", "c", "c", "a"))
```
should give
``