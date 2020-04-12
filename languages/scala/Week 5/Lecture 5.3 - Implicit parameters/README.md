# Implicit parameters

## Making sort more general
**Problem**

How to parametrize `mergeSort` so that it can be used for lists with element other than `Int`?
``` scala
def mergeSort[T](xs: List[T]): List[T] = ...
```
does not work because comparison operator `<` is not defined for arbitrary types `T`.

**Idea**

Parametrize merge with the necessary comparison function.

## Parametrization of sort
The most flexible design is to make the function `sort` polymorphic and to pass the comparison operation as an additional parameter.

``` scala
  def mergeSort[T](xs: List[T])(lt: (T, T) => Boolean): List[T] = {
    val n = xs.length / 2
    if (n == 0) xs
    else {
      def merge(xs: List[T], ys: List[T]): List[T] = {
        (xs, ys) match {
          case (Nil, ys) => ys
          case (xs, Nil) => xs
          case (x :: xs1, y :: ys1) =>
            if (lt(x, y)) x :: merge(xs1, ys) else y :: merge(xs, ys1)
        }
      }
      val (first, second) = xs splitAt n
      merge(mergeSort(first)(lt), mergeSort(second)(lt))
    }
  }
```

Usage
``` scala
val nums = List(2, 1, 6, 3, -1)
val fruits = List("pineapple", "apple", "pear", "banana")
mergeSort(nums)((x: Int, y: Int) => x < y)
mergeSort(fruits)((x: String, y: String) => x.compareTo(y))
```

We can also simplify it further by removing the types in compare function as scala compiler can automatically infer types using the first argument.
``` scala
val nums = List(2, 1, 6, 3, -1)
val fruits = List("pineapple", "apple", "pear", "banana")
mergeSort(nums)((x, y) => x < y)
mergeSort(fruits)((x, y) => x.compareTo(y))
```

## Parametrization with ordered
There is already a class in standard library that represents orderings.
``` scala
scala.math.Ordering
```
provides ways to compare elements of type `T`. So, instead of parametrizing with the `lt` operation directly, we can parametrize with `Ordering` instead.
``` scala
  def mergeSort[T](xs: List[T])(ord: Ordering[T]): List[T] = {
    val n = xs.length / 2
    if (n == 0) xs
    else {
      def merge(xs: List[T], ys: List[T]): List[T] = {
        (xs, ys) match {
          case (Nil, ys) => ys
          case (xs, Nil) => xs
          case (x :: xs1, y :: ys1) =>
            if (ord.lt(x, y)) x :: merge(xs1, ys) else y :: merge(xs, ys1)
        }
      }
      val (first, second) = xs splitAt n
      merge(mergeSort(first)(ord), mergeSort(second)(ord))
    }
  }
```
and can be used as
``` scala
val nums = List(2, 1, 6, 3, -1)
val fruits = List("pineapple", "apple", "pear", "banana")
mergeSort(nums)(Ordering.Int)
mergeSort(fruits)(Ordering.String)
```

## Implicit parameters
**Problem**

Passing around `lt` or `ord` values is cumbersome. We can avoid this by making ord an `implicit` parameter.
``` scala
  def mergeSort[T](xs: List[T])(implicit ord: Ordering[T]): List[T] = {
    val n = xs.length / 2
    if (n == 0) xs
    else {
      def merge(xs: List[T], ys: List[T]): List[T] = {
        (xs, ys) match {
          case (Nil, ys) => ys
          case (xs, Nil) => xs
          case (x :: xs1, y :: ys1) =>
            if (ord.lt(x, y)) x :: merge(xs1, ys) else y :: merge(xs, ys1)
        }
      }
      val (first, second) = xs splitAt n
      merge(mergeSort(first), mergeSort(second))
    }
  }
```
Then calls to `mergeSort` can avoid the ordering parameters. The compiler will figure out the right implicit to pass based on demanded type.

## Rules for implicit parameters
Say, a function takes an implicit parameter of type `T`. The compiler will search for an implicit definition that
1. is marked implicit
2. has a type compatible with `T`
3. is visible at the point of function call, or is defined in a companion object associated with `T`

If there is a single(most specific) definition, it will be taken as the actual argument for the implicit parameter. Otherwise, it's an error.
