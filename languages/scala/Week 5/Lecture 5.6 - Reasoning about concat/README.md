# Reasoning about concat

## Laws of concat
Recall the concatenation operation `++` on lists.

We would like to verify that concatenation is associative, and that it admits the empty list `Nil` as neutral element to the left and to the right.

``` scala
(xs ++ ys) ++ zs = xs ++ (ys ++ zs)
xs ++ Nil = xs
Nil ++ xs = xs
```

**How can we prove properties like these?**
By **structural induction** on lists

## Natural induction
Recall the principle of proof by **natural induction**. To show a property `P(n)` for all the integers `n>=b`,
- Show that we have `P(b)` (base case)
- For all integers `n>=b` show the induction step:
    if one has `P(n)`, then one also has `P(n+1)`

### Example 
Given
``` scala
  def factorial(n: Int): Int = {
    if (n == 0) 1
    else n * factorial(n - 1)
  }
```
Show that, for all `n>=4`, `factorial(n) >= power(2, n)`

**Base case**

This case is established by simple calculations.
```
factorial(4) = 24 >= 16 = power(2, 4)
```

**Induction step**

We have for `n>=4`
```
factorial(n + 1)    >= (n + 1) * factorial(n)   // By second clause in factorial
                    > 2 * factorial(n)          // by calculating
                    >= 2 * power(2, n)          // by induction hypothesis
                    = power(2, n+1)
```

## Referential transparency
Note that a proof can freely apply reduction steps as equalities to some part of a term. That works because pure functional programs don't have side effects; so that a term is quivalent to the term to which it reduces. This principle is called **Referential Transparency**.

## Structural induction
The principle of structural induction is analogous to natural induction.
To prove a property `P(xs)` for all lists `xs`
- show that `P(Nil)` holds (base case)
- for a list `xs` and some element `x`, show the induction step: if `P(xs)` holds, then `P(x :: xs)` also holds.

### Example
Let's show that for lists xs, ys and zs, concatenation is associative i.e.
```
(xs ++ ys) ++ zs = xs ++ (ys ++ zs)
```

To do this, use structural induction on `xs`. From the previuos implementation of `concat`.
``` scala
  def concat[T](xs: List[T], ys: List[T]): List[T] = xs match {
    case List()   => ys
    case x :: xs1 => x :: concat(xs1, ys)
  }
```
Two defining clauses of ++
- `Nill ++ ys = ys`                         // 1st clause
- `(x :: xs1) ++ ys = x :: (xs1 ++ ys)`     // 2nd clause

### Base Case: Nil
For the left hand side, we have
```
(Nil ++ ys) ++ zs = ys ++ zs        // By 1st clause
```

For the right hand side, we have
```
Nil ++ (ys ++ zs) = ys ++ zs        // By 1st clause
```

### Induction Step: x :: xs
For the left hand side, we have
```
((x :: xs) ++ ys) ++ zs = (x :: (xs ++ ys)) ++ zs       // By 2nd clause
                        = x :: ((xs ++ ys) ++ zs)       // By 2nd clause
                        = x :: (xs ++ (ys ++ zs))       // By induction hypothesis
```

For the right hand side, we have
```
(x :: xs) ++ (ys ++ zs) = x :: (xs ++ (ys ++ zs))       // By 2nd clause
```

So, the case is established.





