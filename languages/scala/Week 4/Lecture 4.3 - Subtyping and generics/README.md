# Subtyping and generics

## Polymorphism
Two principal forms of polymorphism
1. Subtyping
2. Generics

Two main areas:
1. Bounds
2. Variance

## Type bounds

Consider method `assertAllPos` which
- takes an `IntSet`
- returns the `IntSet` itself if all its elements are positive
- throws exception otherwise

What would be the best type we can give to `assertAllPos`?
``` scala
def assertAllPos(s: IntSet): IntSet = ...
```
In most situations, this is fine. But can we be more precise?

Function definition can be broken down into 2 parts:
1. `assertAllPos(EmptyIntSet)` returns `EmptyIntSet`
2. `assertAllPos(NonEmptyIntSet)` returns either `NonEmptyIntSet` if all elements are positive, throws exception otherwise.

## Type bounds

### Lower bounds
One might want to express that when `assertAllPos` takes `EmptyIntSet` returns `EmptyIntSet` and when it takes `NonEmptyIntSet` returns `NonEmptyIntSet`.
A way to express this is
``` scala
def assertAllPos[S <: IntSet](r: S): S = ...
```
Here, `<:IntSet` is an **upper bound** of the type parameter S. It means that S can be instantiated only to types that conform to `IntSet`.

Generally, the notation
- `S <: T` means `S` is a subtype of `T`
- `S >: T` means `S` is a supertype of `T` or `T` is subtype of `S`

### Lower bounds
We can also use a lower bound for a type variable.
Example - `[S >: NonEmpty]`
So, `S` could be any one of `NonEmptyIntSet`, `IntSet`, `AnyRef` or `Any`.

### Mixed bounds
Finally, it is also mix an upper bound with lower bound. For instance
``` scala
[S >: NonEmptyIntSet <: IntSet]
```
would restrict `S` any type on the interval between `NonEmptyIntSet` and `IntSet`.

## Covariance
There is another interaction between subtyping and type parameters we need to consider. Given
``` scala
NonEmptyIntSet <: IntSet
```
does
``` scala
List[NonEmptyIntSet] <: List[IntSet]
```
holds true?

Intuitively, this makes sense. A list of non-empty sets is a special case of a list of arbitrary sets.
We call types for which this relationship holds **covariant** because their subtyping relationship varies with type parameter.

### Arrays
Arrays in Java are covariant, so one would have
``` java
NonEmptyIntSet[] <: IntSet[]
```

#### Array typing problem
Consider java code below
``` java
NonEmptyIntSet[] a = new NonEmptyIntSet[]{new NonEmptyIntSet(1, new Empty(), new Empty())}
IntSet[] b = a
b[0] = new Empty()
NonEmptyIntSet s = a[0]
```

Looks like in the last line, we assigned an Empty set to a variable if type non empty. 

**What went wrong?**
In java, third line will give `ArrayStoreException`.

### Liskov substitution principle

The following principle , stated by Barbara Liskov, tells us when a type can be subtype of another.

> If `A <: B`, then everything one can do with the value of type `B` one should also be able to do with the value of type `A`.

The actual definition is a bit more formal
> Let `q(x)` be a property provable about objects `x` of type `B`. Then `q(y)` should be provable for objects `y` of type `A` where `A <: B`.

### Problematic array example in scala
``` scala
val a: Array[NonEmptyIntSet] = Array(new NonEmptyIntSet(1, Empty, Empty))
val b: Array[IntSet] = a
b(0) = Empty
val s: NonEmptyIntSet = a(0)
```
This gives type error at line 2 as the assumption of covariant arrays in scala does not hold true.

