# Chapter - 3: Storage and retrieval

- In order to efficiently find the value for a particular key in the database, we need a different data structure: an index. The general idea behind them is to keep some additional metadata on the side, which acts as a signpost and helps you to locate the data you want. If you want to search the same data in several different ways, you may need several different indexes on different parts of the data.
- An **index** is an additional structure that is derived from the primary data. Many databases allow you to add and remove indexes, and this doesn’t affect the contents of the database; it only affects the performance of queries.
- Maintaining additional structures incurs overhead, especially on writes. Any kind of index usually slows down writes, because the index also needs to be updated every time data is written.
- For this reason, databases don’t usually index everything by default,but require you—the application developer or database administrator—to choose indexes manually, using your knowledge of the application’s typical query patterns. 

## Hash indexes

- Key-value stores are quite similar to the **dictionary** type that you can find in most programming languages, and which is usually implemented as a hash map (hash table). 
- Let’s say our data storage consists only of appending to a file, then the simplest possible indexing strategy is this: keep an in-memory hash map where every key is mapped to a byte offset in the data file—the location at which the value can be found. 
- Whenever you append a new key-value pair to the file, you also update the hash map to reflect the offset of the data you just wrote (this works both for inserting new keys and for updating existing keys). 
- When you want to look up a value, use the hash map to find the offset in the data file, seek to that location, and read the value.
- This is subject to requirement that all keys must fit in RAM since hash map is kept completely in-memory.
- This is well suited for situations where values of each key is updated frequently.

### Compaction

- If we only ever append to a file—so how do we avoid eventually running out of disk space? 
- A good solution is to break the log into segments of a certain size by closing a segment file when it reaches a certain size, and making subsequent writes to a new segment file. 
- We can then perform **compaction** on these segments. **Compaction** means throwing away duplicate keys in the log, and keeping only the most recent update for each key.
- Moreover, since compaction often makes segments much smaller (assuming that a key is overwritten several times on average within one segment), we can also merge several segments together at the same time as performing the compaction. 
- Segments are never modified after they have been written, so the merged segment is written to a new file. 
- The merging and compaction of segments can be done in a background thread, and while it is going on, we can still continue to serve read requests using the old segment files, and write requests to the latest segment file. 
- After the merging process is complete, we switch read requests to using the new merged segment instead of the old segments—and then the old segment files can simply be deleted.

### Implementation details

- **File format** - It’s faster and simpler to use a binary format that first encodes the length of a string in bytes, followed by the raw string.
- **Deleting records** - If you want to delete a key and its associated value, you have to append a special deletion record to the data file (sometimes called a tombstone). When log segments are merged, the tombstone tells the merging process to discard any previous values for the deleted key.
- **Crash recovery** - If the database is restarted, the in-memory hash maps are lost. In principle, you can restore each segment’s hash map by reading the entire segment file from beginning to end and noting the offset of the most recent value for every key as you go along but not very efficient. Better option is recovery by storing a snapshot of each segment’s hash map on disk, which can be loaded into memory more quickly.
- **Partially written records** - The database may crash at any time, including halfway through appending a record to the log. Checksums are included, allowing such corrupted parts of the log to be detected and ignored.
- **Concurrency control** - As writes are appended to the log in a strictly sequential order, a common implementation choice is to have only one writer thread.

### Limitations of hash table indexes

- The hash table must fit in memory, so if you have a very large number of keys, you’re out of luck. Storing the hash map on disk gives poor performance.
- Range queries are not efficient. You need to lookup each key individually.

## SSTables and LSM trees

- We can make a simple change to the format of our segment files: we require that the sequence of key-value pairs is sorted by key. We call this format **Sorted String Table**, or **SSTable** for short.
- SSTables have several big advantages over log segments with hash indexes:
  1. **Efficient merging** - Merging segments is simple and efficient, even if the files are bigger than the available memory. The approach is like the one used in the merge sort algorithm. When multiple segments contain the same key, we can keep the value from the most recent segment and discard the values in older segments.
  2. **Sparse keys** - In order to find a particular key in the file, you no longer need to keep an index of all the keys in memory. You still need an in-memory index to tell you the offsets for some of the keys, but it can be sparse: one key for every few kilobytes of segment file is sufficient, because a few kilobytes can be scanned very quickly.
  3. **Compression** - Since read requests need to scan over several key-value pairs in the requested range anyway, it is possible to group those records into a block and compress it before writing it to disk.  Each entry of the sparse in-memory index then points at the start of a compressed block. Besides saving disk space, compression also reduces the I/O bandwidth use.

### Constructing and maintaing SSTables

- There are plenty of well-known tree data structures that you can use, such as **red-black trees** or **AVL trees**. With these data structures, you can insert keys in any order and read them back in sorted order.
- We can now make our storage engine work as follows:
    - When a write comes in, add it to an **in-memory balanced tree data structure** (for example, a red-black tree). This in-memory tree is sometimes called a **memtable**.
    - When the memtable gets bigger than some threshold—typically a few megabytes—write it out to disk as an SSTable file. This can be done efficiently because the tree already maintains the key-value pairs sorted by key. The new SSTable file becomes the most recent segment of the database. While the SSTable is being written out to disk, writes can continue to a new memtable instance.
    - In order to serve a read request, first try to find the key in the memtable, then in the most recent on-disk segment, then in the next-older segment, etc.
    - From time to time, run a merging and compaction process in the background to combine segment files and to discard overwritten or deleted values.
    - If the database crashes, the most recent writes (which are in the memtable but not yet written out to disk) are lost. In order to avoid that problem, we can keep a separate log on disk to which every write is immediately appended. That log is not in sorted order, but that doesn’t matter, because its only purpose is to restore the memtable after a crash.
    -  Storage engines that are based on this principle of merging and compacting sorted files are often called **LSM(Log-Structured Merge) tree** storage engines.
    -  This storage engine is used in **LevelDB**, **RocksDB**, **Cassandra**, **HBase**, **Lucene** for storing its term dictionary etc.

### Performance optimizations

-  LSM-tree algorithm can be slow when looking up keys that do not exist in the database: you have to check the memtable, then the segments all the way back to the oldest (possibly having to read from disk for each one) before you can be sure that the key does not exist.
-  In order to optimize this kind of access, storage engines often use additional **Bloom filters**.
-  A **Bloom filter** is a memory-efficient data structure for approximating the contents of a set. It can tell you if a key does not appear in the database, and thus saves many unnecessary disk reads for nonexistent keys.
-  There are also different strategies to determine the order and timing of how SSTables are compacted and merged. The most common options are **size-tiered** and **leveled** compaction. 

## B-Trees

- The most widely used indexing structure is B-tree.
-  They remain the standard index implementation in almost all relational databases, and many nonrelational databases use them too.
-  Like SSTables, B-trees keep key-value pairs sorted by key, which allows efficient key-value lookups and range queries.
-  B-trees break the database down into fixed-size blocks or pages, traditionally 4 KB in size (sometimes bigger), and read or write one page at a time. This design corresponds more closely to the underlying hardware, as disks are also arranged in fixed-size blocks.
-  Each page can be identified using an address or location, which allows one page to refer to another—similar to a **pointer**, but on disk instead of in memory. 
-  One page is designated as the root of the B-tree; whenever you want to look up a key in the index, you start here. The page contains several keys and references to child pages. Each child is responsible for a continuous range of keys, and the keys between the references indicate where the boundaries between those ranges lie.
-  Eventually we get down to a page containing individual keys (a leaf page), which either contains the value for each key inline or contains references to the pages where the values can be found.
-  The number of references to child pages in one page of the B-tree is called the **branching factor**.
-  If you want to update the value for an existing key in a B-tree, you search for the leaf page containing that key, change the value in that page, and write the page back to disk (any references to that page remain valid). 
-  If you want to add a new key, you need to find the page whose range encompasses the new key and add it to that page. If there isn’t enough free space in the page to accommodate the new key, it is split into two half-full pages, and the parent page is updated to account for the new subdivision of key ranges.
-  B-tree with n keys always has a depth of **O(log n)**. Most databases can fit into a B-tree that is three or four levels deep, so you don’t need to follow many page references to find the page you are looking for. (A four-level tree of 4 KB pages with a branching factor of 500 can store up to 256 TB.)

## Comparison of B-Trees and LSM trees

- **Faster writes vs faster reads** - As a rule of thumb, LSM-trees are typically faster for writes, whereas B-trees are thought to be faster for reads. Reads are typically slower on LSM-trees because they have to check several different data structures and SSTables at different stages of compaction.
- **Compression** - LSM-trees can be compressed better, and thus often produce smaller files on disk than B-trees. B-tree storage engines leave some disk space unused due to fragmentation: when a page is split or when a row cannot fit into an existing page, some space in a page remains unused. Since LSM-trees are not page-oriented and periodically rewrite SSTables to remove fragmentation, they have lower storage overheads, especially when using leveled compaction.
- **Performance** - A downside of log-structured storage is that the compaction process can sometimes interfere with the performance of ongoing reads and writes. 
-  **High throughput** - At high write throughput: the disk’s finite write bandwidth needs to be shared between the initial write (logging and flushing a memtable to disk) and the compaction threads running in the background. When writing to an empty database, the full disk bandwidth can be used for the initial write, but the bigger the database gets, the more disk bandwidth is required for compaction.
-  **Transactional** - An advantage of B-trees is that each key exists in exactly one place in the index, whereas a log-structured storage engine may have multiple copies of the same key in different segments. This aspect makes B-trees attractive in databases that want to offer strong transactional semantics: in many relational databases, transaction isolation is implemented using locks on ranges of keys, and in a B-tree index, those locks can be directly attached to the tree.

## Secondary indexes

- It is also very common to have **secondary indexes**. In relational databases, you can create several secondary indexes on the same table using the CREATE INDEX command, and they are often crucial for performing joins efficiently.
- A secondary index can easily be constructed from a key-value index. The main difference is that in a secondary index, the indexed values are not necessarily unique; that is,there might be many rows (documents, vertices) under the same index entry. 
- This can be solved in two ways: either by making each value in the index a list of matching row identifiers (like a postings list in a full-text index) or by making each entry unique by appending a row identifier to it.

## Storing values within index

- The key in an index is the thing that queries search for, but the value can be one of two things:
  1. it could be the actual row (document, vertex) in question
  2. it could be a reference to the row stored elsewhere.
- In the latter case, the place where rows are stored is known as a **heap file**, and it stores data in no particular order.
- In some situations, the extra hop from the index to the heap file is too much of a performance penalty for reads, so it can be desirable to store the indexed row directly within an index. This is known as a **clustered index**. 
- As with any kind of duplication of data, clustered and covering indexes can speed up reads, but they require additional storage and can add overhead on writes. 

## Full text search and fuzzy indexes

- The indexes discussed above don’t allow you to do is search for similar keys, such as misspelled words. Such fuzzy querying requires different techniques.
- Full-text search engines commonly allow a search for one word to be expanded to include synonyms of the word, to ignore grammatical variations of words, and to search for occurrences of words near each other in the same document, and support various other features that depend on linguistic analysis of the text.
- To cope with typos in documents or queries, **Lucene** is able to search text for words within a certain **edit distance** (an edit distance of 1 means that one letter has been added, removed, or replaced).
- Lucene uses a SSTable-like structure for its term dictionary. This structure requires a small in-memory index that tells queries at which offset in the sorted file they need to look for a key.  

## In-memory databases

- As RAM becomes cheaper, the cost-per-gigabyte argument is eroded. Many datasets are simply not that big, so it’s quite feasible to keep them entirely in memory, potentially distributed across several machines. This has led to the development of **in-memory databases**.
- Some in-memory key-value stores, such as Memcached, are intended for caching use only, where it’s acceptable for data to be lost if a machine is restarted. But other in-memory databases aim for durability, which can be achieved with special hardware (such as battery-powered RAM), by writing a log of changes to disk, by writing periodic snapshots to disk, or by replicating the in-memory state to other machines.
- When an in-memory database is restarted, it needs to reload its state, either from disk or over the network from a replica (unless special hardware is used). Despite writing to disk, it’s still an in-memory database, because the disk is merely used as an append-only log for durability, and reads are served entirely from memory. 
- They can be faster because they can avoid the overheads of encoding in-memory data structures in a form that can be written to disk.
- In-memory databases can provide data models that are difficult to implement with disk-based indexes. For example, **Redis** offers a database-like interface to various data structures such as priority queues and sets. 

## Transaction processing or analytics

- The term **transaction** refers to a group of reads and writes that form a logical unit.
- An application typically looks up a small number of records by some key, using an index. Records are inserted or updated based on the user’s input. Because these applications are interactive, the access pattern became known as **online transaction processing (OLTP)**.
- However, databases also started being increasingly used for **data analytics**, which has very different access patterns. Usually an analytic query needs to scan over a huge number of records, only reading a few columns per record, and calculates aggregate statistics (such as count, sum, or average) rather than returning the raw data to the user. These queries are often written by business analysts, and feed into reports that help the management of a company make better decisions (business intelligence). In order to differentiate this pattern of using databases from transaction processing, it has been called **online analytic processing (OLAP)**.

Comparison of transaction vs analytics systems

| Property             | Transaction processing systems (OLTP)             | Analytic systems (OLAP)                   |
| -------------------- | ------------------------------------------------- | ----------------------------------------- |
| Main read pattern    | Small number of records per query, fetched by key | Aggregate over large number of records    |
| Main write pattern   | Random-access, low-latency writes from user input | Bulk import (ETL) or event stream         |
| Primarily used by    | End user/customer, via web application            | Internal analyst, for decision support    |
| What data represents | Latest state of data (current point in time)      | History of events that happened over time |
| Dataset size         | Gigabytes to terabytes                            | Terabytes to petabytes                    |

## Data warehouse

-  **OLTP systems** are usually expected to be highly available and to process transactions with low latency, since they are often critical to the operation of the business. Database administrators therefore closely guard their OLTP databases. They are usually reluctant to let business analysts run ad hoc analytic queries on an OLTP database, since those queries are often expensive, scanning large parts of the dataset, which can harm the performance of concurrently executing transactions.
-  A **data warehouse**, by contrast, is a separate database that analysts can query to their hearts’ content, without affecting OLTP operations. The data warehouse contains a read-only copy of the data in all the various OLTP systems in the company. Data is extracted from OLTP databases (using either a periodic data dump or a continuous stream of updates), transformed into an analysis-friendly schema, cleaned up, and then loaded into the data warehouse. This process of getting data into the warehouse is known as **Extract–Transform–Load (ETL)**.
- A big advantage of using a separate data warehouse, rather than querying OLTP systems directly for analytics, is that the data warehouse can be optimized for analytic access patterns.
- Examples of data warehouses are
  - **Commercial** 
    - Teradata
    - Vertica
    - SAP HANA
    - ParAccel
    - Amazon Redshift
  - **Open source (SQL on hadoop)**
    - Apache Hive
    - Spark SQL
    - Cloudera Impala
    - Facebook Presto
    - Apache Tajo
    - Apache Drill
    - Google’s Dremel

## Star and snowflakes: Schema for analytics

- Many data warehouses are used in a fairly formulaic style, known as a **star schema** (also known as dimensional modeling).
- At the center of the schema is a so-called **fact table**. Each row of the fact table represents an event that occurred at a particular time. If we were analyzing website traffic, each row might represent a page view or a click by a user.
- Usually, facts are captured as individual events, because this allows maximum flexibility of analysis later. However, this means that the fact table can become extremely large.(tens of Petabytes)
- Some of the columns in the fact table are **attributes**, such as the price at which the product was sold and the cost of buying it from the supplier.
- Other columns in the fact table are foreign key references to other tables, called dimension tables.  As each row in the fact table represents an event, the dimensions represent the **who, what, where, when, how, and why** of the event.
- The name **star schema** comes from the fact that when the table relationships are visualized, the fact table is in the middle, surrounded by its dimension tables; the connections to these tables are like the rays of a star.
- A variation of this template is known as the **snowflake schema**, where dimensions are further broken down into sub-dimensions.