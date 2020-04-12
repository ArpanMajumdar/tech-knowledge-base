# Polymorphism

## Cons-lists
A fundamental data structure in many functional languagesis the immutable linked list. It is constructed from two building blocks:
1. Nil - the empty list
2. Cons - a cell containing an element and remainder of the list

![Cons lists](https://cdn-images-1.medium.com/max/1600/1*oiP6mB5of_JHxktUYbkPbQ.png)

### Cons-lists in scala
Here is a outline of a class hierarchy that reprsents lists of integer in this fashion.

``` scala
trait IntList ...
class Cons(val head: Int, val tail: IntList) extends IntList ...
class Nil extends IntList ...
```

## Value parameters
Note the abbreviation `(val head: Int, val tail: IntList)` in the definition of `Cons`. This defines at the same time parameters and fields of a class.

It is equivalent to:

``` scala
class Cons(_head: Int, _tail: IntList) extends IntList {
    val head = _head
    val _tail = tail
}
```
where `_head` and `_tail` are otherwise unused names.

## Type parameters
It seems too narrow to define only lists with `Int` elements. We will need another class hierarchy for each possible element type.

We can generalize this definition using a type parameter.

```scala
trait List[T]
class Cons[T](val head: T, val tail: List[T]) extends List ...
class Nil[T] extends List ...
```

> Type parameters are written in square brackets

## Complete definition of list
```scala
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

  override def head: T = 
    throw new NoSuchElementException("Nil.head")

  override def tail: List[T] = 
    throw new NoSuchElementException("Nil.tail")
}
```

## Generic functions
Like classes, functions can have type parameters. For instance, here is a function that creates a list consisting of a single element.

```scala
def singleton[T](elem: T) = new Cons[T](elem,new Nil[T])
```

We can then write
```scala
singleton[Boolean](true)
singleton[Int](1)
```

## Type inference
Scala compiler can usually deduce the correct type of parameters from the value arguments of the function call. So, in most cases type parameters can be left out. You could also write:
```scala
singleton(true)
singleton(1)
```

## Polymorphism
Polymorphism means a function type comes in many forms. In programming, it means that 
- function can be applied to arguments of many types
- the type can have instances of many types

We have seen 2 priniple forms of polymorphism:
1. **Subtyping** - Instances of a subclass can be passed to a base class
2. **Generics** - Instances of function or class are created by type parameterization

## Exercise
Write a function `nthelement` that takes an index `n` and selects n'th element of the list.

```scala
  def nthelement(index: Int, list: List[T]) = {
    if(list.isEmpty) 
        throw new IndexOutOfBoundsException("Array index out of bounds")
    else if (index == 0) list.head
    else nthelement(index - 1, list.tail)
  }
```