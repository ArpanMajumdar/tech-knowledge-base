# Chapter - 3: Storage and retrieval

- In order to efficiently find the value for a particular key in the database, we need a different data structure: an index. The general idea behind them is to keep some additional metadata on the side, which acts as a signpost and helps you to locate the data you want. If you want to search the same data in several different ways, you may need several different indexes on different parts of the data.
- An **index** is an additional structure that is derived from the primary data. Many databases allow you to add and remove indexes, and this doesn’t affect the contents of the database; it only affects the performance of queries.
- Maintaining additional structures incurs overhead, especially on writes. Any kind of index usually slows down writes, because the index also needs to be updated every time data is written.
- For this reason, databases don’t usually index everything by default,but require you—the application developer or database administrator—to choose indexesmanually, using your knowledge of the application’s typical query patterns. 

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

- **File format** - It’s faster and simpler to use a binary format that firstencodes the length of a string in bytes, followed by the raw string.
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
  1. **Efficient merging** - Merging segments is simple and efficient, even if the files are bigger than the available memory. The approach is like the one used in the mergesort algorithm. When multiple segments contain the same key, we can keep the value from the most recent segment and discard the values in older segments.
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