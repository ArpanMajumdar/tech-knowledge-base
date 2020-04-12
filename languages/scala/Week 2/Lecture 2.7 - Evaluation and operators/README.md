# Evaluation and operators

## Opeartors
In principle, the rational numbers defined by `Rational` class are as natural as integers. But for user of these abstractions there is a noticeable difference. We write `x + y` if `x` and `y` are integers but `r.add(s)` if `r` and `s` are rational numbers.

In scala, we can eliminate this difference.

### Step 1: Infix notation
Any method with a parameter can be used as infix operator. It is therefore possible to write 
``` scala
r add s             r.add(s)
r less s            r.less(s)
r max s             r.max(s)
```

