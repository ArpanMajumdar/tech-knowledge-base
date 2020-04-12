# Functions and data

## Example - Rational numbers

We want to design apakage for doing rational arithmatic. A rational number `x/y` is denoted by two integers.
- x - Numerator
- y - Denominator

Suppose we want to implement the addition of two rational numbers. It would be difficult to manage all the numerators and denominators independently. A better choice would be to combine the numerator and deenominator of a rational number in a data structure.

## Classes
In scala, we can acheive this using a `class`.

``` scala
class Rational(x: Int, y: Int){
    def numerator = x
    def denominator = y
}
```

This definition introduces two entities:
- A new `type` named `Rational`
- A `constructor` rational to create elements of this type

## Objects
We call the elements of class type objects. We create an object using `new` operator like in Java.
Example - 
``` scala
new Rational(1, 2)
```

## Members of a object
Objects of class `Rational` have 2 members: `numerator` and `denominator`. We select the members of an object using `.` operator (like in Java).
Example -
``` scala
val x = new Rational(1, 2)
x.numerator
x.denominator
```

## Methods
One can also package function operating on a data abstraction in the data abstraction itself. Such functions are called `methods`.
Example - 
Rational numbers now have in addition to numerator and denominator, the functions `add`, `subtract`, `multiply` ,`divide`, `equal` and `toString`.

## Data abstraction
Rational numbers are not always represented in their simplest form. One would expect the rational numbers to be simplified. Solution to this is to reduce them to their simplest form by dividing both with a divisor.

We can implement this in each rational operation but it would be easy to forget this division in an operation.

A better alternative is to simplify the representation in class when objects are constructed.

> `private` members of a class can only be accessed from withing the class.
> `val` keyword is used to compute the value of a variable immediately.

## Self reference
Note that a simple name `x`, which refers to another member of a class, is an abbreviation of `this.x`.

## Preconditions

### Require
Let's say our Rational class requires that a denominator must be positive and non zero. We can enforce this by calling the `require` function. It is a predefined function which takes a condition and an optional string message.

If condition is false, an `IllegalArgumentException` is thrown with the given message string.

### Assert
Besides require, there is also assert. Assert also takes a condition and optional error message as parameters. Like require, a failing assert also throws an exception but it throws `AssertionError`.

**What is the difference between `require` and `assert`?**
- `require` is used to enforce a precondition on the caller of the function.
- `assert` is used to check the code of a function itself.

## Overloaded constructors
Like in Java, contructors can also be overloaded. We can acheive the same using `this()` method.

Example -
Overloaded constructor for Rational number with one argument

``` scala
this (x: Int) = this (x, 1)
```

Here is the complete implementation of `Rational` class.
``` scala
class Rational(x: Int, y: Int) {
  require(y > 0, "Denominator must be positive")

  def this (x: Int) = this (x, 1)

  private def gcd(a: Int, b: Int): Int = if (b == 0) a else gcd(b, a % b)

  private val g = gcd(x, y)

  def numerator: Int = x / g

  def denominator: Int = y / g

  def add(that: Rational): Rational = new Rational(x * that.denominator + y * that.numerator, denominator * that.denominator)

  def neg: Rational = new Rational(-numerator, denominator)

  def subtract(that: Rational): Rational = this.add(that.neg)

  def less(that: Rational): Boolean = numerator * that.denominator < that.numerator * denominator

  def max(that: Rational): Rational = if (this.less(that)) that else this

  override def toString: String = numerator + "/" + denominator
}
```