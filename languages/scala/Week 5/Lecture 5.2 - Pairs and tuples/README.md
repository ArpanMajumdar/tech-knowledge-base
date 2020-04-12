# Pairs and tuples

## Sorting lists faster

Let's design a function to sort lists that is faster than **insertion sort**. A good algorithm for this is merge sort. The idea is as follows.

1. If a list contains 0 or 1 elements, it is already sorted.
2. Separate the list onto 2 sublists, each containing around half of the elements of the original list.
3. Sort the two sub lists.
4. Merge the two sorted sublists into a single sorted list.

## Merge sort: Implementation 1

``` scala
  def mergeSort(xs: List[Int]): List[Int] = {
    val n = xs.length / 2
    if (n == 0) xs
    else {
      def merge(xs: List[Int], ys: List[Int]): List[Int] = {
        xs match {
          case Nil => ys
          case x :: xs1 =>
            ys match {
              case Nil => xs
              case y :: ys1 =>
                if (x < y) x :: merge(xs1, ys) else y :: merge(xs, ys1)
            }
        }
      }
      val (first, second) = xs splitAt n
      merge(mergeSort(first), mergeSort(second))
    }
  }
```

## Closer look at `splitAt` function
The split at function returns 2 sublists
1. Elements upto the given index
2. Elements from that index

The lists are returned in a **pair**.

## Pair and tuples
A pair consisting of `x` and `y` is written as `(x, y)` in scala.
Example
``` scala
val pair = ("Answer", 42)
```
The type of above pair is `(String, Int)`.
Pairs can also be used as patterns.
``` scala
val (label, value) = pair
```
This works analogously for tuples with more than 2 elements.

## Translation of tuples
- A tuple type `(T1, ..., Tn)` is an abbreviation of the parametrized type `scala.Tuplen[T1, ..., Tn]`.
- A tuple expression `(e1, ... , en)` is equivalent to the function application `scala.Tuplen(e1, ..., en)`
- A tuple pattern `(p1, ... , pn)` is equivalent to the constructor pattern `scala.Tuplen(p1, ..., pn)`.

## The tuple class
Here all `Tuplen` classes are modeled after the following pattern.
``` scala
case class Tuple2[T1, T2](_1: +T1, _2: +T2){
    override def toString = "(" + _1 + "," + _2 + ")"
}
```
Fields of a tuple can be accessed with names `_1`, `_2` etc
So, instead of pattern binding
``` scala
val (label, value) = pair
```
one can also write
``` scala
val label = pair._1
val value = pair._2
```
But the pattern matching form is generally preferred.

## Merge sort: Implementation 2 (Pattern matching using tuples)
``` scala
  def mergeSort(xs: List[Int]): List[Int] = {
    val n = xs.length / 2
    if (n == 0) xs
    else {
      def merge(xs: List[Int], ys: List[Int]): List[Int] = {
        (xs, ys) match {
          case (Nil, ys) => ys
          case (xs, Nil) => xs
          case (x :: xs1, y :: ys1) =>
            if (x < y) x :: merge(xs1, ys) else y :: merge(xs, ys1)
        }
      }
      val (first, second) = xs splitAt n
      merge(mergeSort(first), mergeSort(second))
    }
  }
```