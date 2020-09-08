# spark-tutorial
Examples for Apache Spark

A spark application consists of 2 kinds of processes:
1. driver process
2. executor process

**Driver process**
1. Maintains information about the spark application
2. Responds to user's program or input
3. Analyzes, schedules and distributes the work across executors

**Executor process**
1. Execute code assigned by the driver
2. Report state of computation back to driver


## Spark session
- You can control the Spark Application through a driver process called the **Spark session**.
-  The SparkSession instance is the way Spark executes user-defined manipulations across the cluster. 
- There is a one-to-one correspondence between a SparkSession and a Spark Application.

## Dataframe

- A DataFrame is the most common Structured API and simply represents a table of data with rows and columns. 
- The list that defines the columns and the types within those columns is called the schema.
- DataFrame can span thousands of computers. 
- It’s quite easy to convert Pandas (Python) DataFrames to Spark DataFrames, and R DataFrames to Spark DataFrames.

## Partitions

- To allow every executor to perform work in parallel, Spark breaks up the data into chunks called partitions. 
- A partition is a collection of rows that sit on one physical machine in your cluster. 
- A DataFrame’s partitions represent how the data is physically distributed across the cluster of machines during execution.
- You do not (for the most part) manipulate partitions manually or individually. You simply specify high-level transformations of data in the physical partitions, and Spark determines how this work will actually execute on the cluster.

## Transformations
- In Spark, the core data structures are immutable, meaning they cannot be changed after they’re created. To “change” a DataFrame, you need to instruct Spark how you would like to modify it to do what you want. These instructions are called transformations.
- There are two types of transformations:
    - **Narrow transformations** - Transformations consisting of narrow dependencies are those for which each input partition will contribute to only one output partition.
    - **Wide transformations** - A wide dependency (or wide transformation) style transformation will have input partitions contributing to many output partitions. You will often hear this referred to as a shuffle whereby Spark will exchange partitions across the cluster.

## Lazy evaluation
- Lazy evaulation means that Spark will wait until the very last moment to execute the graph of computation instructions.
- In Spark, instead of modifying the data immediately when you express some operation, you build up a plan of transformations that you would like to apply to your source data. By waiting until the last minute to execute the code, Spark compiles this plan from your raw DataFrame transformations to a streamlined physical plan that will run as efficiently as possible across the cluster.

## Actions
- Actions are the operations that return a final value to the driver program or persist data to an external storage.
- To trigger the computation, we run an action.
- An action instructs Spark to compute a result from a series of transformations.
- There are 3 kinds of actions:
    - Actions to view data in the console
    - Actions to collect data to native objects in the respective language
    - Actions to write to output data sources

Examples of actions:
1. collect
2. count
3. countByValue
4. take
5. saveAsTextFile
6. reduce

**collect**

- Collect operation retrieves the entire RDD and returns it to the driver program in the form of regular collection or value. e.g.- If yu have a String RDD, you will get a list of Strings.
- This is quite useful if the spark program has filtered RDD down to a relatively smaller size and you want to deal with it locally.
- The entire dataset must fit in the memory of a single machine as it needs to be copied to the driver when collect is called.
- `collect` should not be used on large datasets. 

## Spark UI
- You can monitor the progress of a job through the Spark web UI.
- The Spark UI is available on port 4040 of the driver node.
- It’s very useful, especially for tuning and debugging.

## Running a spark application
`spark-submit` lets you send your application code to a cluster and launch it to execute there. Upon submission, the application will run until it exits (completes the task) or encounters an error. 

``` bash
./bin/spark-submit \
  --class org.apache.spark.examples.SparkPi \
  --master local \
  ./path/to/your/example.jar
```

By changing the master argument of spark-submit, we can also submit the same application to a cluster running Spark’s standalone cluster manager, Mesos or YARN.

## Datasets
- Datasets are type safe version of Spark's strcutured API.
- The Dataset API gives users the ability to assign a Java/Scala class to the records within a DataFrame and manipulate it as a collection of typed objects, similar to a Java ArrayList or Scala Seq. 
- The APIs available on Datasets are type-safe, meaning that you cannot accidentally view the objects in a Dataset as being of another class than the class you put in initially.
- The Dataset class is parameterized with the type of object contained inside: `Dataset<T>` in Java and `Dataset[T]` in Scala. 

## Spark structured APIs

These APIs refer to three core types of distributed collection APIs:
1. Datasets
2. DataFrames
3. SQL tables and views

## RDD

### How to create RDD

- Take an existing collection in your program and pass it to Spark Context's `parallelize` method. 
- All the elements in the collection will then be copied to form a distributed dataset that can be operated in parallel.
- It's very handy to create an RDD with little effort
- However, not very practical for working with large datasets.
- Practically, RDDs are created from external storage by calling `textFile` method on the spark context.
- The external storage is usually a distributed file system like HDFS.
- There are other data sources that can be integrated with Spark and used to create RDDs including JDBC, Cassandra, Elasticsearch etc.

## Dataframes

DataFrames and Datasets are (distributed) table-like collections with well-defined rows and columns. Each column must have the same number of rows as all the other columns (although you can use null to specify the absence of a value) and each column has type information that must be consistent for every row in the collection. To Spark, DataFrames and Datasets represent immutable, lazily evaluated plans that specify what operations to apply to data residing at a location to generate some output. When we perform an action on a DataFrame, we instruct Spark to perform the actual transformations and return the result. These represent plans of how to manipulate rows and columns to compute the user’s desired result.

### How spark code is actually executed across a cluster?
1. Write DataFrame/Dataset/SQL Code.
2. If valid code, Spark converts this to a Logical Plan.
3. Spark transforms this Logical Plan to a Physical Plan, checking for optimizations along the way.
4. Spark then executes this Physical Plan (RDD manipulations) on the cluster.

### Schema
A schema defines the column names and types of a DataFrame. We can either let a data source define the schema or we can define it explicitly ourselves.

 > When using Spark for production Extract, Transform, and Load (ETL), it is often a good idea to define your schemas manually, especially when working with untyped data sources like CSV and JSON because schema inference can vary depending on the type of data that you read in.

 ### Columns
 There are a lot of different ways to construct and refer to columns but the two simplest ways are by using the col or column functions. To use either of these functions, you pass in a column name:

 ``` scala
 // Scala
import org.apache.spark.sql.functions.{col, column}
col("someColumnName")
column("someColumnName")
```

 ``` python
 # Python
from pyspark.sql.functions import col, column
col("someColumnName")
column("someColumnName")
 ```

 ``` kotlin
 // Kotlin
 import org.apache.spark.sql.functions.*
 col("someColumnName")
 column("someColumnName")
 ```

 ### Expressions
 An expression is a set of transformations on one or more values in a record in a DataFrame. Think of it like a function that takes as input one or more column names, resolves them, and then potentially applies more expressions to create a single value for each record in the dataset. Importantly, this “single value” can actually be a complex type like a Map or Array. 

 ### DataFrame Transformations

- add rows or columns
- remove rows or columns
- transform a row into a column (or vice versa)
- change the order of rows based on the values in columns

### Literals

Sometimes, we need to pass explicit values into Spark that are just a value (rather than a new column). This might be a constant value or something we’ll need to compare to later on. The way we do this is through literals. This is basically a translation from a given programming language’s literal value to one that Spark understands.

