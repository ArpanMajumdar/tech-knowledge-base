# Class hierarchies

## Abstract classes
Consider the task of writing a class for set of integers with the following operations.

``` scala
abstract class IntSet {

  def incl(x: Int): IntSet

  def contains(x: Int): IntSet
}
```

Here `IntSet` is an abstract class. Abstract classes can contain members which are missing an implementation. Consequently, no instances of abstract classes can be created with operator `new`.

## Class extensions
Let's consider implementing sets as binary trees. There are 2 possible types of trees - 
1. A tree for Empty set
2. A tree consisting of an integer and two sub-trees

Here are there implementations

```scala
class EmptySet extends IntSet {
  override def incl(x: Int): IntSet = 
    new NonEmptySet(x, new EmptySet, new EmptySet)

  override def contains(x: Int): Boolean = false
}
```

``` scala
class NonEmptySet(elem: Int, left: IntSet, right: IntSet) extends IntSet {
  override def incl(x: Int): IntSet =
    if (x < elem) new NonEmptySet(elem, left incl x, right)
    else if (x > elem) new NonEmptySet(elem, left, right incl x)
    else this

  override def contains(x: Int): Boolean =
    if (x < elem) left contains x
    else if (x > elem) right contains x
    else true
}
```

These are also called **Persistent Data Structures** because even if we change these data structures, old version of the data structure is still maintained.

## Terminology
`EmptySet` and `NonEmptySet` both extend `IntSet`. This implies that both types conform to the type `IntSet`. It also means that an object of `EmptySet` and `NonEmptySet` can be used wherever an object of type `IntSet` is required.

### Base classes and subclasses
- `IntSet` is called the **superclass** of `EmptySet` and `NonEmptySet`.
- `EmptySet` and `NonEmptySet` are **subclasses** of `IntSet`.
- If no superclass is given, the standard class `Object` in Java package `java.lang` is assumed.

### Implementation and overriding
The definitions of `contains` and `incl` in the classes `EmptySet` and `NonEmptySet` **implement** the abstract methods in the base class `IntSet`.

It is also possible to redefine an existing non-abstract definition the the base class using `override`.

Example - 
``` scala
abstract class Base {
    def foo = 1
    def bar: Int
}
```

``` scala
class Sub extends Base {
    override def foo = 2
    def bar = 3
}
```

## Object definitions
In the `IntSet` example one could argue that there is only a single instance of `EmptySet`. So, it seems overkill to have the user create multiple instances of it. 

We can express this using `object` definition.
``` scala
object EmptySet extends IntSet {
  override def incl(x: Int): IntSet = ???

  override def contains(x: Int): Boolean = false
}
```

This defines a **singleton object** named `EmptySet`. No other `EmptySet` instances can be created. Singletn objects are values, so empty evaluates to itself.

## Exercise
Write a method union for forming union of 2 sets. You should implement the following abstract class.

``` scala
abstract class IntSet {

  def incl(x: Int): IntSet

  def contains(x: Int): IntSet

  def union(other: IntSet): IntSet
}
```

### Solution
```scala
class EmptySet extends IntSet {
  override def incl(x: Int): IntSet = 
    new NonEmptySet(x, new EmptySet, new EmptySet)

  override def contains(x: Int): Boolean = false

  override def union(other: IntSet): IntSet = other

}
```

``` scala
class NonEmptySet(elem: Int, left: IntSet, right: IntSet) extends IntSet {
  override def incl(x: Int): IntSet =
    if (x < elem) new NonEmptySet(elem, left incl x, right)
    else if (x > elem) new NonEmptySet(elem, left, right incl x)
    else this

  override def contains(x: Int): Boolean =
    if (x < elem) left contains x
    else if (x > elem) right contains x
    else true

  override def union(other: IntSet): IntSet = 
    ((left union right) union other) incl x

}
```

## Dynamic binding
Object oriented langunages(including Scala) implement **dynamic method dispatch**. This means that code invoked by a method call depends upon the runtime of the object that contains the method.

