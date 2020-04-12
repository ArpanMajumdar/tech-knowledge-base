# Finding fixed point of a function

A number is called fixed point of a function if 

``` 
f(x) = x
```

Some some functions `f` we can find fixed point by starting with an initial estimate and then repeatedly applying `f`.
```
x, f(x), f(f(x)), f(f(f(x))) ...
```

until the value does not vary anymore.

Fixed point of a function can be found like this

``` scala
  def fixedPoint(f: Double => Double)(firstGuess: Double): Double = {
    val tolerance = 0.0001

    def isCloseEnough(x: Double, y: Double) = {
      abs((y - x) / x) / x < tolerance
    }

    def iterate(guess: Double): Double = {
      println("Guess: " + guess)
      val next = f(guess)
      if (isCloseEnough(guess, next)) next
      else iterate(next)
    }

    iterate(firstGuess)
  }
```

## Square root function
Here is the specification for sqrt function.
```
sqrt(x) = y such that y * y = x
```
Dividing both sides by y, we get
```
sqrt(x) = y such that y = x / y
```

So, sqrt function can be written as
``` scala
def sqrt(x: Double) = fixedPoint(y => x / y)(1)
```
But unfortunately, this does not converge as the successive values keep on oscillating. One way to control the oscillations is to prevent the estimation from varying too much. This can be done by averaging the values of the original sequence.
``` scala
def sqrt(x: Double) = fixedPoint(y => (y + x / y)/2)(1)
```
We can also introduce a new function `averageDamp` which damps the input value.

``` scala
  def sqrt(x: Double) = {
    def averageDamp(f: Double => Double)(x: Double) = (x + f(x)) / 2
    fixedPoint(averageDamp(y => x / y))(1)
  }
```
