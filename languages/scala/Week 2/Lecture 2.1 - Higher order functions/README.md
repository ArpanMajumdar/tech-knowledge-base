# Higher order functions

Functions that take other function as arguments or return functions as results are called `higher order functions`. Functions are treated as first class citizens in functional languages.

Let's consider an example

Take the sum of integers between `a` and `b`.

``` scala
def sumInts(a: Int,b: Int): Int = 
    if(a>b) 0 else a + sumInts(a+1,b)
```

Now, let's take sum of cubes of integers between `a` and `b`.
``` scala
def cube(x: Int):Int = x * x * x
```

``` scala
def sumCubes(a: Int,b: Int): Int = 
    if(a>b) 0 else cube(a) + sumCubes(a+1,b)
```

Now, let's take sum of factorials of all integers between `a` and `b`.
``` scala
def sumFactorials(a: Int,b: Int): Int = 
    if(a>b) 0 else fact(a) + sumFactorials(a+1,b)
```

## Summing with higher order functions

Let's define
```scala
def sum(f:Int => Int,a: Int, b: Int): Int = {
    if (a > b) 0
    else f(a) + sum(f,a+1,b)
}
```

We can also write
``` scala
def sumInts = sum(id,a,b)
def sumCubes = sum(cube,a,b)
def sumFactorials = sum(fact,a,b)
```
where
``` scala
def id(x: Int): Int = x
def cube(x: Int):Int = x * x * x
def fact(x: Int): Int = if(x = 0) 1 else x * fact(x-1)
```

## Function types

The type `A => B` is the type of `function` that takes the argument of type A and returns a result of type B.
So, `Int => Int` s the type of function that takes integer as arguments and returns integer as result.

## Anonymous functions

Passing functions as parameters can lead to creation of many small functions. Sometimes, it is tedious to define and name these functions using `def`. We would like to have functions as literals, which let us write functions without naming them. These are called `anonymous functions`.

**Example: A function that raises its argument to cube.**
```scala
(x: Int) => x * x * x
```
Here `(x: Int)` is the `parameter` of the function and `x * x * x` is the function body.
If there are several parameters, they are separated with commas.
```scala
(a: Int, b: Int) => a + b
```
Also, type of arguments can be omitted if compiler can infer type from the context.

> Anonymous functions are syntactic sugar.

An anonymous function  `(x1: T1, x2: T2, ..., xn: Tn) => E` can always be expressed using `def` as follows.
``` scala
{def f(x1: T1, x2: T2, ..., xn: Tn) = E;f}
```

Using anonymous functions, we can write sums in a shorter way.

``` scala
def sumInts(a: Int, b: Int) = sum(x => x, a, b)
def sumCubes(a: Int, b: Int) = sum(x => x * x * x, a, b)
```

**Example: Tail recursive version of sum of integers.**
``` scala
object IntSum {
    def sum(f: Int => Int, a: Int, b: Int) = {
        def loop(a: Int, acc: Int): Int = {
            if(a>b) acc
            else loop(a+1,acc + f(a))
        }
        loop(a,0)
    }
}
```

