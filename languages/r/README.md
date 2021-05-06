# R programming language

## Working with files

| Command                  | Function                                         |
| ------------------------ | ------------------------------------------------ |
| getwd()                  | Get current working directory                    |
| read.csv("path/to/file") | Read a csv                                       |
| ls()                     | Shows variables in workspace                       |
| source("/path/to/file")  | Sources the contents of an R file in the console |
| list.files()/dir() | List all the files in the current working directory
| args() | Shows the what arguments a function takes
| file.create("mytest.R") | Creates an empty file
| file.exists("mytest.R") | Check if a file exists
| file.info("mytest.R") | Show file information
| file.rename("mytest.R") | Rename a file
| file.remove("mytest.R") | Delete a file
| file.copy("mytest.R") | Copy a file
| file.path("folder1", "folder2") | Create a platform independent filepath
| dir.create() | Create directories


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

## Data types

### Objects

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

### Numbers
- Numbers in R are treated as numeric objects(i.e. double precision real numbers).
- If you explicitly want an integer, you need to specify the `L` suffix. e.g. - Entering 1 gives you numeric object, entering 1L explicitly gives you an integer.
- There is also a special number `Inf` which represents infinity. e.g. - 1/0. `Inf` can be used in ordinary calculations. 
- The value `NaN` represents an undefined value ("not a number"). e.g. - 0/0. `NaN` can also be thought of as a missing value.

### Attributes
- R objects have attributes
  - names, dimnames
  - dimensions (matrices, arrays)
  - class
  - length
  - other user-defined attributes/metadata
- Attributes of an object can be accessed using the `attributes()` function.

### Vectors
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

#### Mixing vectors
When different objects are mixed in a vector, type coercion occurs so that every element in the vector is of the same class.
```R
y <- c(1.7, "a") # character
y <- c(TRUE, 2) # numeric TRUE: 1 and FALSE: 0
y <- c("a", TRUE) # character 
```
#### Explicit coercion
- Objects can be explicitly coerced from one class to another using the `as.*` functions.
- The types that cannot be coerced lead to NA values.
```R
x <- 0:6
class(x) # "integer"
as.numeric(x) # 0 1 2 3 4 5 6
as.logical(x) # FALSE TRUE TRUE TRUE TRUE TRUE TRUE
as.character(x) # "0" "1" "2" "3" "4" "5" "6"
```  

### Lists

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

### Matrices

- Matrices are vectors with a dimension attribute. The dimension attribute is itself an integer vector of length 2 (nrow, ncol).

```R
m <- matrix(nrow = 2, ncol = 3) # Creating a matrix of size (2,3)
# > m
#      [,1] [,2] [,3]
# [1,]   NA   NA   NA
# [2,]   NA   NA   NA  
dim(m)
# [1] 2 3
```
- Matrices are constructed column-wise, so entries can be thought of starting in the "upper left" corner and running down the columns.
```R
m <- matrix(1:6, nrow = 2, ncol = 3)
# > m
#      [,1] [,2] [,3]
# [1,]    1    3    5
# [2,]    2    4    6
```
- Matrices can also be created directly from vectors by adding a dimension attribute.
```R
m <- 1:10
# > m
#  [1]  1  2  3  4  5  6  7  8  9 10
dim(m) <- c(2,5)
# > m
#      [,1] [,2] [,3] [,4] [,5]
# [1,]    1    3    5    7    9
# [2,]    2    4    6    8   10
```
- Matrices can also be created by **column-binding** or **row-binding** with `cbind()` and `rbind()`.
```R
x <- 1:3
y <- 10:12
cbind(x,y)
#      x  y
# [1,] 1 10
# [2,] 2 11
# [3,] 3 12
rbind(x,y)
#   [,1] [,2] [,3]
# x    1    2    3
# y   10   11   12
```

### Factors

- Factors are used to represent **categorical** data. 
- They can be ordered or unordered.
- One can think of a factor as an integer vector where each integer has a label.
- They are treated specially by modeling functions like `lm()` and `glm()`.
- Using factors with labels is better than using integers because factors are self describing. e.g.- Having a variable that has values male and female is better than a variable that has values 1 and 2.

```R
x <- factor(c("yes", "yes", "no", "yes","no"))
# > x
# [1] yes yes no  yes no 
# Levels: no yes
table(x) # Frequency counts of each categorical variable
# x
#  no yes 
#   2   3 
unclass(x)
# [1] 2 2 1 2 1
# attr(,"levels")
# [1] "no"  "yes"
```
- Order of the levels can be set using the `levels` argument to `factor()`.
- This can be important in linear modeling because first level is used as a baseline level.

```R
x <- factor(c("yes", "yes", "no", "yes", "no"), levels = c("yes", "no")) # Force to use yes as first level and no as second
# > x
# [1] yes yes no  yes no 
# Levels: yes no
```

### Missing values

- Missing values are denoted by **NA** or **NaN** for undefined mathematical operations.
- `is.na()` is used to test objects if they are NA.
- `is.nan()` is used to test for NaN.
- NA values have a class also like integer NA, character NA etc.
- A NaN value is also NA but converse is not true.
- `my_data == NA` doesn't work as NA is not an actual value but a placeholder for a quantity that is not available. So, if we check equality of a vector with NA, it gives a vector of same length that contains all NAs.

```R
x <- c(1, 2, NA, 10, 3)

is.na(x)
# [1] FALSE FALSE  TRUE FALSE FALSE
is.nan(x)
# [1] FALSE FALSE FALSE FALSE FALSE
x <- c(1, 2, NA, NaN, 3)

is.na(x)
# [1] FALSE FALSE  TRUE  TRUE FALSE
is.nan(x)
# [1] FALSE FALSE FALSE  TRUE FALSE
```

### Dataframes

- Dataframes are used to store data
- They are represented as a special type of list where every element of the list has to have the same length.
- Each element of the list can be thought of as a column and the length of each element of the list is the number of rows.
- Unlike matrices, dataframes can store different classes of objects in each column(just like lists). Matrices must have every element of the same class.
- Dataframes have a special attribute called `row.names`.
- Dataframes are usually created by calling `read.table()` or `read.csv()`.
- Can be converted to a matrix by calling `data.matrix()`.
- They can also be created using `data.frame()` function.

```R
x <- data.frame(foo = 1:4, bar = c(T, T, F, F))
# > x
#   foo   bar
# 1   1  TRUE
# 2   2  TRUE
# 3   3 FALSE
# 4   4 FALSE
nrow(x)
# [1] 4
ncol(x)
# [1] 2
```

## Names 

- R objects can also have names, which is very useful for writing readable code and self-describing objects.
```R
x <- 1:3
names(x)
# NULL
names(x) <- c("a", "b", "c")
# > names(x)
# [1] "a" "b" "c"
# > x
# a b c 
# 1 2 3  
```
- Lists can also have names.
```R
x <- list(a = 1, b = 2, c = 3)
# > x
# $a
# [1] 1

# $b
# [1] 2

# $c
# [1] 3
```
- Matrices can have dimnames
```R
m <- matrix(1:4, nrow = 2, ncol = 2)
dimnames(m) <- list(c("a", "b"), c("c", "d"))
# > m
#   c d
# a 1 3
# b 2 4
```

## Reading data

### Reading tabular data

There are few principal functions reading data into R

| Function                 | Description                                   |
| ------------------------ | --------------------------------------------- |
| `read.table`, `read.csv` | Reading tabular data                          |
| `readLines`              | Reading lines of a text file                  |
| `source`                 | For reading R code files (inverse of dump)    |
| `dget`                   | For reading in R code files (inverse of dput) |
| `load`                   | For reading in saved workspaces               |
| `unserialize`            | For reading single R objects in binary form   |

There are analogous functions for writing data to files

| Function      | Description |
| ------------- | ----------- |
| `write.table` |
| `writeLines`  |
| `dump`        |
| `dput`        |
| `save`        |
| `serialize`   |

### Reading data files with read.table

The `read.table` function is one of the most commonly used functions for reading data. It has few important arguments.

| Argument         | Desc                                                                  |
| ---------------- | --------------------------------------------------------------------- |
| file             | name of file or connection                                            |
| header           | logical indicating if the file has a header line                      |
| sep              | a string indicating how columns are separated                         |
| colClasses       | a character vector indicating the class of each column in the dataset |
| nrows            | number of rows in the dataset                                         |
| comment.char     | a character string indicating the comment character                   |
| skip             | number of lines to skip from the beginning                            |
| stringsAsFactors | should character variables be coded as factors?                       |

- `read.csv` is identical to `read.table` except that the default separator is `,` in the former and space in the latter.

### Reading larger datasets with read.table

- With much larger datasets, doing these things will make life easier and prevent R from choking.
  - Read the help page for `read.table` which contains many hints.
  - Make a rough calculation of the memory required to store the dataset. If dataset is larger than the amount of RAM on your computer, you can probably stop right there.
  - Set `comment.char = ""` if there are no commented lines in your file.
  - Use the `colClasses` argument. Specifying this option instead of using the default can make `read.table` run much faster, often twice as fast. In order to use this ption, you have to know the class of each column in your dataframe. A quick and dirty way to figure out the classes of each column is to read the first 100 or 1000 rows.
  ```R
  initial <- read.table("datatable.txt", nrows = 100)
  classes <- sapply(initial, class)
  tabAll <- read.table("datatable.txt", colClasses = classes)
  ```
  - Set `nrows`. This doesn't run faster but it helps with memory usage. You can use unix tool `wc` to calculate the number of lines in the file.
  
### Calculating memory requirements

Suppose we have a dataframe with 1.5M rows and 120 columns, all of which is numeric data. Roughly how much memory is required to store this dataframe?
```
1.5M x 120 x 8 bytes/numeric
  = 1440M bytes
  = 1440M/ 2^20 bytes/MB
  = 1373.29 MB
  = 1.34 GB
```
1.34 GB is the raw memory required to store this data. However, due to storage overhead as a rule of thumb, we should allocate twice the amount of memory for the dataset.

## Textual data formats

- **dumping** and **dputing** are useful because the resulting textual format is editable and in case of corruption, potentially recoverable.
- Unlike writing out a table or CSV file, dump and dput preserve the metadata, so that another user doesn't have to specify it all over again.
- Textual formats can work much better with VCS like git which can only track changes meaningfully in text files.
- Textual formats can be longer lived i.e. if there is a corruption in the file, it can be easier to fix.
- Downsize is that it is not space efficient.

### dput function

Another way to pass data around is by deparsing the R object with `dput` and reading it back using `dget`.

```R
y <- data.frame(a = 1, b = "a")
# > dput(y)
# structure(
#   list(a = 1, b = "a"), 
#   class = "data.frame", 
#   row.names = c(NA, -1L)
# )
dput(y, file = "y.R") # Writes to a file names y.R
y_new <- dget("y.R") # Reading from file
# > y_new
#   a b
# 1 1 a
```

### dump function

- Difference between dput and dump is that multiple objects can be serialized using dump. 
- Object names should be passed as a character vector.

```R
x <- "foo"
y <- data.frame(a = 1, b = "a")
dump(c("x", "y"), file = "data.R") # Write to data.R
rm(x,y)
source("data.R") # Read from data.R
# > x
# [1] "foo"
# > y
#   a b
# 1 1 a
```

## Connections

- Data are read in using **connection** interfaces. Connections can be made to files(most common) or to other more exotic things.
  - `file` - opens a connection to a file
  - `gzfile` - opens a connection to a file compressed with gzip
  - `bzfile` - opens a connection to a file compressed with bzip2
  - `url` - opens a connection to a webpage
- In practice, we don't need to deal with the connection interface directly.

### File connections
Function definition
```R
file(description = "", open = "", blocking = TRUE,
     encoding = getOption("encoding"), raw = FALSE,
     method = getOption("url.method", "default"))
```

- `description` is the name of the file.
- `open` is a code indicating
  - r - read only
  - w - writing(and initializing a new file)
  - a - appending
  - rb, wb, ab - Reading, writing or appending in binary mode
- To read specified lines from a text file, pass the connection object to `readLines` method.

```R
con <- gzfile("words.gz")
x <- readLines(con, 10)
```

### URL connections

- `readLines` can be useful for reading in lines of webpages.

```R
con <- url("https://google.com")
x <- readLines(con)
```

## Subsetting

There are a number of operators that can be used to extract subsets of R objects.
- **[** always returns an object of the same class as the original; can be used to select more than one element.
- **[[** is used to extract elements of a list or a dataframe; it can only be used to extract a single element and class of the returned object will not be necessarily the same.
- **$** is used to extract elements of a list or dataframe by name; semantics are similar to hat of **[[**.
```R
x <- c("a", "b", "c", "c", "d", "a")
x[1]
# [1] "a"
x[2]
# [1] "b"
x[1:5]
# [1] "a" "b" "c" "c" "d"
x[x > "a"] # Logical indexing
# [1] "b" "c" "c" "d"
u <- x > "a"
# > u
# [1] FALSE  TRUE  TRUE  TRUE  TRUE FALSE

# Select individual indices from vector
x(c(3, 5, 7))

# R doesn't give any error if we try to select an index outside the range of the vector. You should always make sure that indices are within the bound.
x[0] # numeric(0)
x[3000] # NA

# Select all elements expect few
x(c(-2, -10)) # Returns all elements except 2 and 10
```

### Subsetting lists

```R
x <- list(foo = 1:4, bar = 0.6)

x[1] # Result is a vector
# $foo
# [1] 1 2 3 4

x[[1]] 
# [1] 1 2 3 4

x$bar
# [1] 0.6

x[["bar"]]
# [1] 0.6

x["bar"]
# $bar
# [1] 0.6

x <- list(foo = 1:4, bar = 0.6, baz = "hello")
x[c(1,3)] # Extract multiple elements from list
# $foo
# [1] 1 2 3 4

# $baz
# [1] "hello"
```

- The advantage with [[ operator is that it can be used with computed indexes whereas $ can only be used with literal names.

```R
x <- list(foo = 1:4, bar = 0.6, baz = "hello")
name <- "foo"
x[[name]]
# [1] 1 2 3 4
x$name # This can't be used as this doesn't exist
# NULL
```
- [[ can also be used to extract nested elements of a list.

```R
x <- list(a = list(10, 12, 14), b = c(3.14, 2.81))
x[[c(1,3)]]
# [1] 14
x[[1]][[3]] # This expression is equivalent to the above
# [1] 14
x[[c(2,1)]]
# [1] 3.14
```

### Subsetting a matrix

```R
x <- matrix(1:6, 2, 3)
# > x
#      [,1] [,2] [,3]
# [1,]    1    3    5
# [2,]    2    4    6

x[1,2]
# [1] 3

x[2,1]
# [1] 2

x[1,]
# [1] 1 3 5

x[,2]
# [1] 3 4
```

- By default , when a single element of a matrix is retrieved, it is returned as a vector of length 1 rather than a 1x1 matrix. This behavior can be turned off by setting `drop = FALSE`.

```R
x[1,2] # Returns a vector of length 1
# [1] 3

x[1,2, drop = FALSE] # Returns a 1x1 matrix
#      [,1]
# [1,]    3

x[1,] # Returns a vector of size 3
[1] 1 3 5

x[1, ,drop = FALSE] # Returns a matrix of size 1x3
#      [,1] [,2] [,3]
# [1,]    1    3    5
```

## Partial matching

Partial matching of names is allowed with [[ and $.

```R
x <- list(aardvark = 1:5)

x$a # Matches partially to the closest name
# [1] 1 2 3 4 5

x[["a"]] # Requires an exact match unless you specify flag exact = FALSE
# NULL

x[["a", exact = FALSE]]
# [1] 1 2 3 4 5
```

## Removing NA values

A common task in data cleaning is to remove missing values (NA).

```R
x <- c(1, 2, NA, 4, NA, 5)
# > x
# [1]  1  2 NA  4 NA  5

bad <- is.na(x)
x[!bad]
# [1] 1 2 4 5
```

If there are multiple things and you want to take the subset with no missing values?

```R
x <- c(1,2,NA,4,NA,5)
y <- c("a", "b", "c", "d", NA, "f")
good <- complete.cases(x,y) # TRUE when all the corresponding elements are not NA
# > good
# [1]  TRUE  TRUE FALSE  TRUE FALSE  TRUE

x[good]
# [1] 1 2 4 5

y[good]
# [1] "a" "b" "d" "f"
```

## Vectorized operations

Many operations in R are vectorized making the code more efficient, concise and easier to read.

```R
x <- 1:4; y<- 6:9

x + y
# [1]  7  9 11 13

x > 2
# [1] FALSE FALSE  TRUE  TRUE

x >= 2
# [1] FALSE  TRUE  TRUE  TRUE

y == 8
# [1] FALSE FALSE  TRUE FALSE

x * y
# [1]  6 14 24 36

x / y
# [1] 0.1666667 0.2857143 0.3750000 0.4444444
```

Vectorized operations also work for matrices.

```R
x <- matrix(1:4, 2, 2); y <- matrix(rep(10, 4), 2, 2)
# > x
#      [,1] [,2]
# [1,]    1    3
# [2,]    2    4
# > y
#      [,1] [,2]
# [1,]   10   10
# [2,]   10   10

x * y # Element-wise multiplication of matrix elements
#      [,1] [,2]
# [1,]   10   30
# [2,]   20   40

x / y # Element-wise division of matrix elements
#      [,1] [,2]
# [1,]  0.1  0.3
# [2,]  0.2  0.4

x %*% y # True matrix multiplication
#      [,1] [,2]
# [1,]   40   40
# [2,]   60   60
```

## Sequences

- Sequences can be created using the `:` operator or `seq()` function.
- `1:10` created a numeric sequence with increment of 1.
- Sequences can also be created for any real number (not only integers). e.g.- `pi:10`.
- To generate sequence with a given increment. `seq(0, 10, by=0.5)`
- To generate a sequence between two numbers of a specified length. `seq(5,30, length = 30)`
- Use `length()` function to get the length of a sequence.
- To create a sequence starting from 1 of same length of a given sequence, use `seq(along.with = my_seq)` or `seq_along(my_seq)` or `1:length(my_seq)`.
- To create a sequence by replicating a number, use `rep()` function. e.g.- `rep(0, times = 40)` or `rep(c(0,1,2), times = 10)`.
```R
# To replicate a number n times

rep(0, times = 40)
# [1] 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0

# To repeat a vector n times
rep(c(0,1,2), times = 10)
# [1] 0 1 2 0 1 2 0 1 2 0 1 2 0 1 2 0 1 2 0 1 2 0 1 2 0 1 2 0 1 2

# To repeat each element of the vector n times
rep(c(0,1,2), each = 10)
# [1] 0 0 0 0 0 0 0 0 0 0 1 1 1 1 1 1 1 1 1 1 2 2 2 2 2 2 2 2 2 2
```

## Character operations

| Function | Description
| --- | ---
| paste(char_vec, collapse = "") | Concatenates elements of character vector with given delimiter

## Indexing vectors

- To select a subset of the vector, you can place an **index vector** in the square brackets immediately after the vector. e.g. - `x[1:10]`
- Index vectors come in four different flavors - **logical vectors**, **vectors of positive integers**, **vectors of negative integers**, and **vectors of character strings**.
- Logical indexing can be done by passing a logical vector between the square brackets. e.g. - `x[!is.na(x)]`
- Logical vectors can be combined using logical operators like `&` and `|`. e.g. - `x[!is.na(x) & x > 0]`
- To subset non-consecutive indices, we can pass a vector of indices to select the desired elements. e.g. - `x[c(3,5,7)]`
- R doesn't throw an error if we try to get an index which is not in bounds of the vector and can return unexpected results. You should always make sure that index is within the bounds.
- You can also use negative integer indexing to return all the elements except the elements specified by the negative index. e.g. - `x[c(-2, -10)] or x[-c(2,10)]` Returns all the elements of the vector except those 2nd and 10th elements.
- You can also create named indices.
```R
vect <- c(foo = 11, bar = 2, norf = NA)
# > vect
#  foo  bar norf 
#   11    2   NA
```
- We can get the names of a named vector by passing the vector to the `names()` function. Alternatively, we can also create a named vector from unnamed one. We can index a named vector by passing the name.
```R
names(vect)
# [1] "foo"  "bar"  "norf"

vect2 <- c(11,2,NA)
names(vect2) <- c("foo", "bar", "norf")
# > vect2
#  foo  bar norf 
#   11    2   NA 

identical(vect, vect2) # We can check that vect and vect2 are identical
# [1] TRUE

vect["bar"]
# bar 
#   2 

vect[c("foo", "bar")]
# foo bar 
#  11   2 
```

## Indexing matrices and dataframes

### Matrices

- Vectors don't have a `dim()` attribute. To get the length of a vector, we need to use the `length()` function.
- To convert a vector to a matrix, we can set the dim attribute to the desired dimension.
```R
my_vector <- 1:20
dim(my_vector)
# NULL

dim(my_vector) <- c(4,5)
dim(my_vector)
# [1] 4 5
```
- A matrix is simply a vector with a dimension attribute. 
- A matrix can also be created using the `matrix()` function.
```R
matrix(1:20, nrow = 4, ncol = 5)
```
- Matrices can only contain ONE class of data.

### Dataframes

- Dataframe can be created using the `data.frame()` function.
- Column names can be changed by assigning a names vector to the `colnames` attribute.
```R
patients <- c("Bill", "Gina", "Kelly", "Sean")
my_matrix <- matrix(1:20, nrow = 4, ncol = 5)
my_data <- data.frame(patients, my_matrix)
# > my_data
#   patients X1 X2 X3 X4 X5
# 1     Bill  1  5  9 13 17
# 2     Gina  2  6 10 14 18
# 3    Kelly  3  7 11 15 19
# 4     Sean  4  8 12 16 20

cnames <- c("patient", "age", "bp", "rating", "test")
colnames(my_data) <- cnames
# > my_data
#   patient age weight bp rating test
# 1    Bill   1      5  9     13   17
# 2    Gina   2      6 10     14   18
# 3   Kelly   3      7 11     15   19
# 4    Sean   4      8 12     16   20
```

## Control structures

- Control structures in R allow you to control the flow of execution of a program, depending on runtime conditions. Common structures are
  - `if, else` - testing a condition
  - `for` - execute a loop a fixed number of times
  - `while` - execute a loop while a condition is true
  - `repeat` - execute an infinite loop
  - `break` - break the execution of a loop
  - `next` - skip an iteration of a loop
  - `return` - exit a function

### if
**Syntax**
```R
if(<condition>){
  # do something
} else {
  # do something else
}

if(<condition>){
  # do something
} else if(<condition>){
  # do something
} else {
  # do something
}
```

**Examples**
```R
if(x > 3){
  y <- 10
} else {
  y <- 0
}

# If else can also be used in a functional style
y <- if(x > 3){
  10
} else {
  0
}
```

### for-loop

- `for` loops take an interior variable and assign it successive values from a sequence or a vector. For loops are most commonly used for iterating over elements of an object (list, vector, etc.).
```R
for(i in 1:10){
  print(i)
}
```
- These 3 loops have the same behavior.

**For loop examples**
```R
x <- c("a", "b", "c", "d")

for(i in 1:4){
  print(x[i])
}

for(i in seq_along(x)){
  print(x[i])
}

for(letter in x){
  print(letter)
}

for(i in 1:4) print(x[i]) # You can omit curly braces if for loop contains only one statement
```
- For loops can be nested.
```R
x <- matrix(1:6, 2, 3)

for(i in seq_len(nrow(x))){
  for(j in seq_len(ncol(x))){
    print(x[i,j])
  }
}
```

### while-loop

- While loop begins by testing a condition. If it is true, then they execute teh loop body. Once the loop body is executed, the condition is tested again, and so forth.
- While loops can potentially result in infinite loops if not written properly.

**Example**
```R
count <- 0

while(count < 10){
  print(count)
  count <- count + 1
}
```

### repeat-loop

- Repeat initiates an infinite loop. 
- The only way to exit a `repeat` loop is to call `break`.

```R
x0 <- 1
tol <- 1e-8

repeat{
  x1 <- computeEstimate()

  if(abs(x1 - x0) < tol){
    break
  } else {
    x0 <- x1
  }
}
```
- This is a bit dangerous as there's no guarantee that it will stop. Better to set a hard limit on the number of iterations using a for loop.

### next and return
- `next` is used to skip an iteration of a loop
```R
for(i in 1:100){
  if(i <= 20){
    # Skip the first 20 iterations
    next
  }
  # Do something
}
```
- `return` signals that a function should exit and return a given value.


## Functions

- Functions are created using the `function()` directive and are stored as R objects just like anything else.
- They are objects of class **function**.
```R
f <- function(<arguments>){
  # Do something
}
```
- Functions in R are **first class objects**, which means that they can be treated much like any other object.
- Functions can be passed as arguments to other functions.
- Functions can be nested, so that you can define a function inside of another function.
- The return value of a function is the last expression in the function body to be evaluated.

```R
add2 <- function(x, y){
  x + y
}

above10 <- function(x){
  x[x > 10]
}
above10(1:20)
#  [1] 11 12 13 14 15 16 17 18 19 20

above <- function(x, n){
  x[x > n]
}
above(1:20, 15)
# [1] 16 17 18 19 20

# We can also give default parameters to functions
above <- function(x, n = 10){
  x[x > n]
}
above(1:20)
#  [1] 11 12 13 14 15 16 17 18 19 20

columnmean <- function(y, removeNA = TRUE){
  nc <- ncol(y)
  means <- numeric(nc)
  
  for(i in 1:nc){
    means[i] <- mean(y[,i], na.rm = removeNA)
  }
  means
}
x <- matrix(1:20, nrow = 4, ncol = 5)
columnmean(x)
# [1]  2.5  6.5 10.5 14.5 18.5
```

### Function arguments

- Functions have named arguments which potentially have default values.
- **Formal arguments** are the arguments included in the function definition.
- The `formals` function returns a list of all the formal arguments of the function.
- Not every function call in R makes use of all formal arguments.
- Function arguments can be missing or might have default values.

### Argument matching

- R functions can be matched positionally or by name. So, the following calls to `sd` are equivalent.
```R
mydata <- rnorm(100)
sd(mydata)
sd(x = mydata)
sd(x = mydata, na.rm = FALSE)
sd(na.rm = FALSE, x = mydata)
sd(na.rm = FALSE, mydata)
```
- Even though it's legal, it is not recommended to mess around with the order of the arguments too much, since it can lead to confusion.
- You can mix positional matching with matching by name. When an argument is matched by name, it is taken out of the argument list and the remaining unnamed arguments are matched in order that they are listed in the function definition.
```R
args(lm)
# function (formula, data, subset, weights, na.action, method = "qr", 
#     model = TRUE, x = FALSE, y = FALSE, qr = TRUE, singular.ok = TRUE, 
#     contrasts = NULL, offset, ...) 

# Following two are equivalent
lm(data = mydata, y ~ x, model = FALSE, 1:100)
lm(y ~ x, mydata, 1:100, model = FALSE)
```
- Named arguments are useful on command line when you have long argument list and you want to use defaults for everything except few.
- Named arguments can also help if you can remember the name of the argument and not its position on the argument list(e.g. - plot).
- Functional arguments can also be **partially** matched, which is used for interactive work. The order of operations when given an argument is
  1. Check for exact match for a named argument
  2. Check for partial match
  3. Check for a positional match 

### Defining a function

```R
f <- function(a, b = 1, c = 2, d = NULL){

}
```
- In addition to not specifying a value, you can also set an argument value to NULL.
- 

### Lazy evaluation

- Arguments are evaluated lazily, so they are evaluated as needed.

```R
f <- function(a, b){
  a ^ 2
}

f(2)
# [1] 4
```
- This function never actually uses `b`, so calling `f(2)` will not  produce an error because 2 get positionally matched to `a`.
```R
f <- function(a, b){
  print(a)
  print(b)
}
f(2)
# [1] 2
# Error in print(b) : argument "b" is missing, with no default
```
- 2 got printed first before the error was triggered. This is because `b` did not have to be evaluated util after `print(a)`. Once the function  tried to evaluate `print(b)` it had to throw an error.

### The "..." argument

- The `...` argument indicates a variable number of arguments that are usually passed on to other functions.
- `...` is often used when extending another function and you don't want to copy the entire argument list of the original function.

```R
myplot <- function(x, y, type = "l", ...){
  plot(x, y, type = type, ...)
}
```
- Generic functions use ... so that extra arguments can be passed to methods.
- The arguments is also necessary when the number of arguments passed to the function cannot be known in advance.

```R
args(paste)
function (..., sep = " ", collapse = NULL, recycle0 = FALSE) 

args(cat)
function (..., file = "", sep = " ", fill = FALSE, labels = NULL, append = FALSE) 
```
- Any arguments that come after `...` on the argument list must be named explicitly and cannot be partially matched.

## Scoping rules

- How does R know which value to assign to which symbol?
```R
lm <- function(x) {x * x}
# > lm
# function(x) {x * x}
```
- Why doesn't R give it the value of `lm` that is in the stats package?
- When R tries to bind a value to a variable, it searches through a series of environments to find the appropriate value. 
- When you are working on the command line and need to retrieve the value of an R object, the order is roughly
  1. Search the global environment for a symbol name matching the one requested.
  2. Search the namespaces of each of the packages on the search list.
  3. The search list can be found using the `search()` function.

```R
search()
#  [1] ".GlobalEnv"        "tools:rstudio"     "package:stats"     "package:graphics" 
#  [5] "package:grDevices" "package:utils"     "package:datasets"  "package:methods"  
#  [9] "Autoloads"         "package:base"
```

### Binding values to symbol

- The global environment or the user's workspace is always the first element of the search list and base package is always the last.
- The order of the packages in the list matters. R searches the symbol starting from beginning of the list to the end.
- Users can configure which packages get loaded on the startup, so you cannot assume that there will be a set list of packages available.
- When a user loads a package with `library` function, the namespace of that package gets put into Position 2 of the search list(by default) and everything else gets shifted down the list.
- Note that R has separate namespaces for functions and non-functions, so it's possible to have an object named c and a function named c. 

### R scoping rules

- The scoping rules determine how a value is associated with a free variable in a function.
- R uses **lexical scoping** or **static scoping** which is a common alternative to dynamic scoping.
- Related to the scoping rules is how R uses the search list to bind a value to a symbol.
- Lexical scoping turns out to be particularly useful for simplifying statistical computations.

### Environment

What is an environment?

- An environment is a collection of (symbol, value) pairs. e.g.- x is a symbol and 3.14 might be its value.
- Every environment has a parent environment; it is possible to have multiple "children".
- The only environment without a parent is the empty environment.
- A function + an environment = a closure or function closure.

### Lexical scoping

Consider the following function.

```R
f <- function(x, y){
  x^2 + y/z
}
```
This function has 2 arguments - x and y. In the body of the function is another symbol z. In this case z is a **free variable**. The scoping rules of a language determine how values are assigned to free variables. Free variables are not local variables and are not local variables.

- Lexical scoping in R means that the values of free variables are searched for in the environment in which the function was defined.
- Searching the value of a free variable
  - If the value of a symbol is not found in the environment in which function was defined, then search is continued in the **parent environment**.
  - The search continues down the sequence of parent environments until we hit the **top-level environment**; this usually is the global environment(workspace) or the namespace of the package.
  - After the top-level environment, the search continues down the list until we hit the **empty environment**. If a value for a given symbol cannot be found once empty environment is arrived at, then an error is thrown.

```R
make.power <- function(n){
  pow <- function(x){
    x^n
  }
  pow
}

cube <- make.power(3)
square <- make.power(2)

cube(3)
# [1] 27
square(3)
# [1] 9
```

What's in a function's environment?

```R
ls(environment(cube))
# [1] "n"   "pow"
get("n", environment(cube))
# [1] 3
ls(environment(square))
# [1] "n"   "pow"
get("n", environment(square))
# [1] 2
```

### Consequences of lexical scoping

- In R, all objects must be stored in memory.
- All functions must carry a pointer to their respective defining environments, which could be anywhere.

## Coding standards for R

- Always use text files/text editor.
- Indent your code (4 or 8 spaces)
- Limit the width of your code (80 characters)
- Limit the length of individual functions

## Dates and times in R

### Date in R

- Dates are represented by the `Date` class.
- Times are represented by the `POSIXct` or `POSIXlt` class.
- Dates are stored internally as the number of days since epoch(1970-01-01).
- Times are stored internally as the number of seconds since epoch(1970-01-01).

```R
x <- as.Date("1970-01-01")
# > x
# [1] "1970-01-01"
unclass(x)
# [1] 0
x <- as.Date("1970-01-02")
unclass(x)
# [1] 1
```

### Time in R

Times are represented using the POSIXct or the POSIXlt class.

- POSIXct is just a very large integer under the hood. It is a useful class when you want to store times in something like a dataframe.
- POSIXlt is a list underneath and it stores a bunch of other useful information like the day of week, day of year, day of the month etc.
- There are a number of generic functions that work on dates and times.
  - **weekdays** - gives the day of the week
  - **months** - gives the month name
  - **quarters** - give the quarter number(Q1, Q2, Q3, Q4)

**POSIXlt**
```R
x <- Sys.time()
# > x
# [1] "2021-01-14 15:40:45 IST"
class(x)
# [1] "POSIXct" "POSIXt" 
p <- as.POSIXlt(x)
# > p
# [1] "2021-01-14 15:40:45 IST"
names(unclass(p))
#  [1] "sec"    "min"    "hour"   "mday"   "mon"    "year"   "wday"   "yday"   "isdst"  "zone"  
# [11] "gmtoff"
p$sec
# [1] 45.62063
```

**POSIXct**
```R
x <- Sys.time()
# > x
# [1] "2021-01-14 15:44:10 IST"

# Time is stored as long value that is seconds after epoch
unclass(x)
# [1] 1610619251

# There is no attribute sec in POSIXct class
x$sec
# Error in x$sec : $ operator is invalid for atomic vectors
```

Finally there is strptime function in case your dates are written in different format.
```R
datestr <- c("January 10, 2020 10:40", "December 9, 2021 9:10")
x <- strptime(datestr, "%B %d, %Y %H:%M")
# > x
# [1] "2020-01-10 10:40:00 IST" "2021-12-09 09:10:00 IST"
class(x)
# [1] "POSIXlt" "POSIXt" 
```

### Operations on date and time

- You can use mathematical operators on dates and times like add or subtract. You can do comparisons too (i.e. ==, <=)
- Advantage with date and time classes is that it even keeps track of leap years, leap seconds, daylight savings and time zones.
- Plotting function also recognize date-time classes and format them accordingly

```R
x <- as.Date("2021-01-01")
y <- strptime("9 Jan 2021 11:34:21", "%d %b %Y %H:%M:%S")

# Mathematical operations can be performed only if date formats are same
x - y
# Error in x - y : non-numeric argument to binary operator
# In addition: Warning message:
# Incompatible methods ("-.Date", "-.POSIXt") for "-" 

x <- as.POSIXlt(x)
x - y
# Time difference of -8.253021 days

# Time operations in different timezones
x <- as.POSIXct("2020-10-25 01:00:00")
y <- as.POSIXct("2020-10-25 06:00:00", tz = "GMT")
y - x
# Time difference of 10.5 hours
```