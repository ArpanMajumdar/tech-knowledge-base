# R programming language

## Basic commands

| Command                  | Function                                         |
| ------------------------ | ------------------------------------------------ |
| getwd()                  | Get current working directory                    |
| read.csv("path/to/file") | Read a csv                                       |
| ls()                     | Shows objects in workspace                       |
| source("/path/to/file")  | Sources the contents of an R file in the console |

## Functions

Examples of functions

``` R
myfunction <- function(x){
    y <- rnorm(100)
    mean(y)
}

second <- function(x){
    x + rnorm(length(x))
}
```

## Assignment operator

`<-` symbol is the assignment operator.

``` R
x <- 1
print(x) # Prints: [1] "hello"
msg <- "hello"
```
[1] indicates that this is the first element of the vector

```R
x <- 1:20 # Creates a vector from 1 to 20
```

## Objects

R has 5 basic or atomic classes of objects:
- character
- numeric (real numbers)
- integer
- complex
- logical (True/False)

The most basic object is a vector.
- A vector can contain objects of the same class
- However, the one exception is a **list**, which is represented as a vector but can contain objects of different classes. 
- Empty vectors can be created with the `vector()` function.

## Numbers
- Numbers in R are treated as numeric objects(i.e. double precision real numbers).
- If you explicitly want an integer, you need to specify the `L` suffix. e.g. - Entering 1 gives you numeric object, entering 1L explicitly gives you an integer.
- There is also a special number `Inf` which represents infinity. e.g. - 1/0. `Inf` can be used in ordinary calculations. 
- The value `NaN` represents an undefined value ("not a number"). e.g. - 0/0. `NaN` can also be thought of as a missing value.

## Attributes
- R objects have attributes
  - names, dimnames
  - dimensions (matrices, arrays)
  - class
  - length
  - other user-defined attributes/metadata
- Attributes of an object can be accessed using the `attributes()` function.

## Vectors
Vectors can be created using following methods 
- The `c()` or concatenate function can be used to create vectors of objects.
- Using the `vector()` function
```R
# Using c()
x <- c(0.5, 0.6) # numeric
x <- c(TRUE, FALSE) # boolean
x <- c(T, F) # logical
x <- c("a", "b", "c") # character
x <- 1:20 # integer
x <- c(1+0i, 2+4i) # complex

# Using vector
x <- vector("numeric", length=10)
```

### Mixing vectors
When different objects are mixed in a vector, type coercion occurs so that every element in the vector is of the same class.
```R
y <- c(1.7, "a") # character
y <- c(TRUE, 2) # numeric TRUE: 1 and FALSE: 0
y <- c("a", TRUE) # character 
```
### Explicit coercion
- Objects can be explicitly coerced from one class to another using the `as.*` functions.
- The types that cannot be coerced lead to NA values.
```R
x <- 0:6
class(x) # "integer"
as.numeric(x) # 0 1 2 3 4 5 6
as.logical(x) # FALSE TRUE TRUE TRUE TRUE TRUE TRUE
as.character(x) # "0" "1" "2" "3" "4" "5" "6"
```  

## Lists
- Lists are vectors that can contain elements of different classes.

```R
x <- list(1, "a", TRUE, 1+4i)
# [[1]]
# [1] 1

# [[2]]
# [1] "a"

# [[3]]
# [1] TRUE

# [[4]]
# [1] 1+4i
```