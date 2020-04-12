# Lists

List is a fundamental data structure in functional programming. A list having `x1, ... , xn` elements is written as `List(x1, .. , xn)`.

**Example**

``` scala
val fruit = List("apples", "oranges", "pears")
val nums = List(1, 2, 3)
val diag3 = List(List(1, 0, 0),List(0 1, 0), List(1, 0, 1))
val empty = List()
```

There are two important differences between Lists and Arrays.
- Lists are immutable i.e. elements of a List cannot be changed.
- Lists are recursive while Arrays are flat.

## List type
Like arrays, lists are homogenous. The elements of a list must all have the same type. They type of a list with elements of type `T` is written as `scala.List[T]` or `List[T]`.

**Example**
``` scala
val fruit: List[String] = List("apples", "oranges", "pears")
val nums: List[Int] = List(1, 2, 3)
val diag3: List[List[Int]] = List(List(1, 0, 0),List(0 1, 0), List(1, 0, 1))
val empty: List[Nothing] = List()
```

## Constructors of lists
All lists are constructed from
- the empty list `Nil`
- the construction operation `::` (cons)
- `x :: xs` gives a new list with first element `x` followed by the elements of `xs`.

**Example**
``` scala
val fruit = "apples" :: ("oranges" :: ("pears" :: nil))
val nums = 1 :: (2 :: (3 :: Nil))
val empty = Nil
```

## Right associativity
Operators ending in `:` associate to right. 
`A :: B :: C` is treated as `A :: (B :: C)`
We can thus omit the parantheses in the definition above.

**Example**
``` scala
val nums = 1 :: 2 :: 3 :: Nil
```

## Operations on lists
All operations on lists can be expressed in terms of the following 3 operations.
1. `head` - First element of the list
2. `tail` - The list composed of all elements of the list except the first.
3. `isEmpty` - true if list is empty, false otherwise

## List patterns
It is also possible to decompose lists with pattern matching.
- `Nil` - The Nil constant
- `p :: ps` - A pattern that matches a list with head matching `p` and tail matching `ps`
- `List(p1, .. , pn)` - Same as `p1 :: ... :: pn`

**Example**
- `1 :: 2 :: xs` - Lists that start with 1 and then 2
- `x :: Nil` - Lists of length 1
- `List(x)` - Same as above
- `List()` - Empty list
- `List(2 :: xs)` - A list that contains as only element another list that starts with 2

## Sorting lists
Suppose we want to sort a list of numbers in ascending order.
- One way to sort the list `List(7, 3, 9, 2)` is to sort the tail list `List(3, 9, 2)` to obtain `List(2, 3, 9)`.
- The next step is to insert the head 7 at right place to get the list `List(2, 3, 7, 9)`.

This is **Insertion Sort**.

``` scala
def insertionSort(xs: List[Int]): List[Int] = xs match {
    case List() => List()
    case y: ys => insert(y,insertionSort(ys))
}

def insert(x: Int, xs: List[Int]): List[Int] = xs match {
    case List() => List()
    case y :: ys => if(x < y) x :: xs else y :: insert(x, ys)
}
```

