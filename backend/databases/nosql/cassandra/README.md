# Cassandra

## Data models

- Query language is a lot like relational databases.
- Tables should be narrow (should not have too many columns) but can be arbitrarily tall (add as many records as you want).
- Doesn't support joins between tables (leads to de-normalization which is required for operating at scale as joins become more and more expensive)
- No foreign key constraints
- Tables are always contained in a **keyspace**. This is where we define replication.

## Cassandra keys
- Keys are super important in cassandra.
- All table accesses are by keys.
- Primary keys identify rows. They are composed of **Partitioning Key** and **Clustering Key**.
- It's typical to have multiple columns in the primay key.

``` sql
CREATE TABLE movies_by_actor (
  actor TEXT,
  release_year INT,
  movie_id UUID,
  title TEXT,
  genres SET<TEXT>.
  rating FLOAT,
  PRIMARY KEY ((actor), release_year, movie_id)
) WITH CLUSTERING ORDER BY (release_year DESC, movie_id ASC);
```

- Here, `actor` is the partitioning key and `release_year` , `movie_id` are clustering keys. Cassandra makes sure to keep records with the same partitioning key in the same node locally.
- Ordering the keys allows us to perform range queries.

## Cassandra data types
- text, int, bigint, float, double, boolean
- decimal, varint
- uuid, timeuuid(if we want unique IDs but also like to order by time)
- inet
- tuple (a typed fixed length array of records)
- timestamp
- counter

### Collections
- set
- list
- map

## Secondary indexes
- These are not like relational secondary indexes.
- They are good for large partitions.
- SASI (SSTable Attached Secondary Index)

**What if we want to do a like query on one the columns that is not indexed?**

We can create a SASI index on the secondary column.
``` sql
CREATE CUSTOM INDEX title ON movies_by_actor (title) 
USING 'org.apache.cassandra.index.sasi.SASIIndex'
WITH OPTIONS = {'mode' : 'contains'};
```
Now, we can perform queries like
``` sql
select * from movies_by_actor 
where actor = 'Emma Stone' 
and title like '%man';
```

## Query-first modeling
- We are used to modeling domains.
- Now we model queries as well.
- This is dictated by key design and index behaviour.

## Materialized views

- If we want to add a new column, due to de-normalization we have to make changes to multiple tables as cassandra doesn't support joins.
- It is always difficult to migrate from one schema version to other. Materialized views are of help here.
- This helps to have a relatively wide main table to which we can keep on adding columns and write and then create materialized views on the top of it for read.
- The main advantages of materialized views is that these queries are performant unlike secondary tables.
- One key limitation of materiaized views is that every row in main table has one-to-one correspondense with materialized view i.e. we cannot create aggregations in the materialized view.

A materialized view can be created like this.
``` sql
CREATE MATERIALIZED VIEW movies_mv AS
SELECT title, release_year, movie_id, genres, actor
FROM movies_by_actor
WHERE title IS NOT NULL 
AND release_year IS NOT NULL
AND movie_id IS NOT NULL
AND actor IS NOT NULL
PRIMARY KEY ((title, release_year), actor, movie_id);
```
You can query the materialized view like a normal table using its primary key.
``` sql
select * from movies_mv 
where title = 'Birdman' 
and release_year = 2011;
```

## Nodes and clusters

- Cassandra hashes the partition key and uses ring token structure to assign to which node the record goes.
- We can elastically grow or shrink the cassandra cluster without taking it down for maintenance.

## Replication
- Only keeping one copy of data(especially in production) is unacceptable as hardware or network failure can happen.
- Replication factor
- Replica placement (If replication factor is N, then records are also written to the next N-1 node along with the node determined by token hashing)

## Consistency
- Since there are multiple replicas of the data, updating the data can lead to consistency problems.
- How many replicas are enough when we are:
  1. Writing
  2. Reading
- Tunable consistency levels
- How do we know that a write succeeded?
  - There are different consistency levels from which we can choose according to our use case.
    1. **CL = ALL** (Write is considered successful only after data is written to all the replicas, useful if we want very high consistency)
    2. **CL = QUORUM** (Write is considered successful after data is written to the number of replicas specified in the quorum < N>)
    3. **CL = ONE** (Write is considered successful after data is written to one node, useful if we want very low latency)
- When an application writes a record, it randomly picks a node and it acts as a coordinator for that context.
- Coordinator role is not specific to a node and any node can act as coordinator.
- Coordinator decides which nodes in the replica are supposed to perform read or write.
- Coordinator also keeps track of latency of nodes and chooses the node with the lowest latency for read and write operations.
- For reads and writes to be consistent, we have to follow this formula - `R + W > N`. 
- If we choose CL < ALL, we may have stale data. Cassandra gives a **repair operation** which syncs data across replicas for a table or a node. This is an administrative operation and must be performed atleast once a week.

## Multiple data centers
- It is required to have multiple data centers to keep data availale.

## Virtual nodes
- If there are less number of nodes, the token range is divided between them and the sectors are very big.
- So, if a node goes down or a new node is coming up, all the data is streamed from one node.
- To cope up with this problem, the token range is divided into a large number of sectors via virtual nodes.
- At any given time, all the virtual nodes are distributed equally and uniformly among all actual nodes.
- Now if a new node comes up or a node goes down, these virtual nodes are distributed uniformly again.

## Gossip
- As the number of nodes grow, it becomes difficult to perform health checks and know the state of all the nodes.
- **Gossip** is an epidemic protocol where it randomly selects some other nodes to talk to. It then exhanges information about their state and about the state of the nodes they know. (That's why it's called a gossip protocol)
- In this way, everybody in the cluster knows about everybody.
- Every node can raise a suspicion about other nodes if something is abnormal like their disk or memory utilization is high.
- Thus when we connect to any 1 node of the cluster, it can give a good picture of the entire cluster at any point of time.

## Write path
- Writes in cassandra are implemented using **Log-structured merge tree**.
- Writes are like log i.e writes are performed using an append only fashion and they are immutable. You cannot go back and update a log. You can only append to a log.
- Whenever a write comes to cassandra, it writes it into a commit log.
- There is 1 commit log per node.
- These logs are then buffered to an in-memory data structure called **memtable**.
- There is one memtable per table.
- This memtable is then flushed sequentially to immutable **SSTables** (Sorted String tables). 
- Due to this sequential flushing, cassandra provides great write speeds. In general, write speed is almost double that of read speed.
- For deleting a record, cassandra places a tombstone in the mem-table.

## Read path
- Read path is a process of assembling the rows and columns we need.
- While reading query first looks at the mem-table. If all rows that are required are there in the mem-table its wel and good because by definition mem-tables have the most recent data and its going to be really fast as we are not touching the disk.
- Generally this is not the case and we may need to scan the SSTables to get the data we need.
- Every SSTable has something called a **Bloom Filter**. It is a way of telling us if we should look at that table for reading.
- Bloom filter gives a probability whether a key is present in the SSTable or not.
- There is also a **Key Cache** in each SSTable which stores the offsets of all the keys. If there is a cache miss, then the key is populated after scanning all the indexes of the SSTable.

# Compaction

- We keep writing data to memtables and they are periodically flushed to SSTables.
- But we caannot keep writing forever to SSTables.
- While compaction the deleted data which is marked using tombstones is discarded.
- Since compaction is a background process it doesn't affect reads and old SSTables are used for scanning. As soon as compaction is done, it flips the swtch and the reads are now handled by the compated SSTable.
- Compaction strategies
  1. Size-based (SSTables with least size are compacted)
  2. Time-based
