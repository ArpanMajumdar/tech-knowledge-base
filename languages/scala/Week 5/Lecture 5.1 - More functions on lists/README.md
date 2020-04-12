# More functions on lists

## List methods

### Sublists and element access
- `xs.length` - The number of elements of `xs`
- `xs.last` - List's last element,exception if list is empty
- `xs.init` - A list containing all elements of `xs` except the last one, exception if list is empty
- `xs take n` - A list containing first `n` elements  of `xs`, or `xs` itself if it is shorter than `n`
- `xs drop n` - The rest of collection after taking `n` elements
- `xs(n)` - The element of `xs` at index `n`

### Creating new lists
- `xs ++ ys`- The list containing all elements of `xs` followed by all elements of `ys`
- `xs.reverse` - The list containing elements of `xs` in reverse order
- `xs updated (n, x)` - The list containing same elements as xs, except at index `n` where it contains `x`

### Finding elements
- `xs indexOf x` - The index of first element in `xs` equal to `x`, or -1 if `x` does not appear in `xs`
- `xs contains x` - Same as `xs indexOf x >= 0`

## List method implementations

### Implementation of `last`
The complexity of head is constant time. What is the complexity of last?

``` scala
def last[T](xs: List[Int]): T = xs match {
    case List() => throw new Error("last of empty list is not defined")
    case List(x) => x
    case y :: ys => last(ys)
}
```
So, `last` takes steps proportional to the length of `n`.

### Implementation of `init`
``` scala
def last[T](xs: List[T]): List[T]  = xs match {
    case List() => throw new Error("init of empty list is not defined")
    case List(x) => List()
    case y :: ys => y :: init(ys)
}
```

### Implementation of list concatenation
``` scala
def concat[T](xs: List[T], ys: List[T]): List[T] = xs match {
    case List() => ys
    case z :: zs => z :: concat(zs, ys)
}
```

### Implementation of list reverse
``` scala
def reverse[T](xs: List[T]): List[T] = xs match {
    case List() => xs
    case y :: ys => reverse(ys) ++ y
}
```
Due to recursive calls of reverse and concat, time complexity of reverse is `n^2`.


