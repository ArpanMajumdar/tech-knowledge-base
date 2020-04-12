# Reduction of lists

Another common operation on lists is to combine the elements of a lists using a given operator.
For example
``` scala
sum(List(x1, x2, ... , xn))     = 0 + x1 + ... + xn
product(List(x1, x2, ... , xn)) = 1 * x1 * ... * xn
```
We can implement this with usual recursive schema.

``` scala
def sum(xs: List[Int]): Int = xs match {
    case Nil => 0
    case y :: ys => y + sum(ys)
}
```

## Reduce left
This pattern can be abstracted out using the generic method `reduceLeft`. `reduceLeft` inserts a given binary operator betwene adjacent elements of a list.

``` scala
List(x1, ..., xn) reduceLeft op = (...(x1 op x2) op ...) op xn 
```

Using reduce left, we can simplify
``` scala
def sum(xs: List[Int]) = (0 :: xs) reduceLeft ((x,y) => x + y)
def product(xs: List[Int]) = (1 :: xs) reduceLeft((x,y) => x * y)
```

## Shorter way to write functions
Instead of `((x, y) => x * y)`, one can also write shorter `(_ * _)`.
Every `_` represents a new parameter, going from left to right. The parameters are defined at the next outer pair of parentheses (or the whole expression if there are no enclosing parentheses).

So, `sum` and `product` can also be expressed like this
``` scala
def sum(xs: List[Int]) = (0 :: xs) reduceLeft (_ + _)
def product(xs: List[Int]) = (1 :: xs) reduceLeft (_ * _)
```

## Fold left
The function `reduceLeft` is defined in terms of a more general function, `foldLeft`. `foldLeft` is like `reduceLeft` but takes an **accumulator** `z`, as an additional parameter, which is returned when `foldLeft` is called on an empty list.

``` scala
(List(x1, ..., xn) foldLeft z)(op) = (...(z op x1) op ...) op xn
```

So, `sum` and `product` can also be defined as follows
``` scala
def sum(xs: List[Int]) = (xs foldLeft 0)(_ + _)
def product(xs: List[Int]) = (xs foldLeft 1)(_ * _)
``` 

## Implementations of `ReduceLeft` and `FoldLeft`
`foldLeft` and `reduceLeft` can be implemented in class `List` as follows.

``` scala
abstract class List[T]{
    def reduceLeft(op: (T,T) => T): T = this match {
        case Nil => throw new Error("Nil.reduiceLeft is not defined")
        case x :: xs => (xs foldLeft x)(op)
    }

    def foldLeft[U](z: U)(op: (U,T) => U) : U = this match {
        case Nil => z
        case x :: xs => (xs foldLeft op(z,x))(op)
    }
}
```

## `FoldRight` and `ReduceRight`
Applications of `foldLeft` and `reduceLeft` unfold on trees that lean to the left. They have two dual functions, `foldRight` and `reduceRight`, which produce trees which lean to the right i.e.
``` scala
List(x1, ..., x{n-1}, xn) reduceRight op = x1 op (... (x{n-1} op xn) ...)
List(x1, ..., x{n-1}, xn) foldRight op = x1 op (... (xn op acc) ...)
```

## Implementation of `FoldRight` and `ReduceRight`
They are defined as follows
``` scala
  def reduceRight(op: (T, T) => T): T = this match {
    case Nil      => throw new Error("Nil.reduceRight not defined")
    case x :: Nil => x
    case x :: xs  => op(x, xs.reduceRight(op))
  }

  def foldRight[U](z: U)(op: (T, U) => U): U = this match {
    case Nil     => z
    case x :: xs => op(x, (xs foldRight z)(op))
  }
```

## Difference between `FoldLeft` and `FoldRight`
For operators that are associative and commutative, `foldLeft` and `foldRight` are equivalent (even though there may be difference in efficiency).
But sometimes, only one of these is appropriate.

## Exercise
Here is a formulation of `concat`.

``` scala
def concat[T](xs: List[T], ys: List[T]): List[T] = (xs foldRight ys)(_ :: _)
```

**Here, it isn't possible to replace `foldRight` by `foldLeft`. Why?**
It throws an error that value `::` is not a member of type `T`.