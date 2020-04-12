# Currying

## Motivation

``` scala
def sumInts(a: Int, b: Int) = sum(x => x, a, b)
def sumCubes(a: Int, b: Int) = sum(x => x * x * x, a, b)
```

Note that arguments a and b get passed unchanged from `sumInts` and `sumCubes` into `sum`. Can we be even shorter by getting rid of these parameters?

Let's rewrite sum as follows
``` scala
def sum(f: Int => Int): (Int, Int) => Int = {
    def sumF(a: Int, b: Int): Int = {
        if(a>b) 0
        else f(a) + sumF(a+1,b)
    }
    sumF
}
```

`sum` is now a function that returns another function.

We can then define
``` scala
def sumInts = sum(x => x)
def sumCubes = sum(x => x * x * x)
```

These functions can in turn be applied like any other function.
``` scala
sumInts(1,10) + sumCubes(10,20)
```

## Consecutive stepwise applications
``` scala
sum(cube)(1,10)
```

**Explanation**
- `sum(cube)` applies sum to cube and returns sum of cubes function.
- `sum(cube)` is thus equivalent to `sumCubes` function.
- This function is then applied to argument `(1,10)`.

## Multiple parameter lists
The definition of functions that return functions is so useful in functional programming that there is a different syntax for it in scala.

``` scala
def sum(f: Int => Int)(a: Int, b: Int): Int = 
    if(a>b) 0 else f(a) + sum(f)(a+1,b)
```

## Expansion of multiple parameter lists
In general, a definition of function with multiple parameter lists
``` scala
def f(args 1)(args 2)...(args n) = E
```
where n>1 is equivalent to
``` scala
def f(args 1)(args 2)...(args n-1) = 
    {def g(args n) = E;g}
```

or in short
``` scala
def f(args 1)(args 2)...(args n-1) = 
    {def (args n) => E}
```

By repeating the process n times
``` scala
def f(args 1)(args 2)...(args n) = E
```
is shown to be equivalent to
``` scala
def f = (args 1 => (args 2 => ... (args n => E)...))
```

## More function types
**Question:** Given
``` scala
def sum(f: Int => Int)(a: Int, b: Int) => Int = ...
```
What is the type of sum?

**Answer:**
``` scala
(Int => Int) => (Int => Int) => Int
```
> Note that functional types associate to the right