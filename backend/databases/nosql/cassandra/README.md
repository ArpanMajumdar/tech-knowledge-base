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

## Cassanra data types
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
