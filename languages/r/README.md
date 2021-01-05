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

