# Chapter -2 : Data models and query languages

Most applications are built by layering one data model on top of another. For each layer, the key question is: how is it represented in terms of the next-lower layer? In a complex application there may be more intermediary levels, such as APIs built upon APIs, butthe basic idea is still the same: each layer hides the complexity of the layers below it by providing a clean data model.

## Relational vs document model

### Relational model

- In **relational model** proposed by Edgar Codd in 1970 , data is organized into relations (called tables in SQL), where each relation is an unordered collection of tuples (rows in SQL).
- As computers became vastly more powerful and networked, they started being used for increasingly diverse purposes. And remarkably, relational databases turned out to generalize very well, beyond their original scope of business data processing, to a broad variety of use cases.
- Much of what we see on the web today is still powered by relational databases

### NoSQL/Non-relational model

There are several driving forces behind the adoption of NoSQL databases, including:

- A need for greater scalability than relational databases can easily achieve, including very large datasets or very high write throughput
- A widespread preference for free and open source software over commercial database products
- Specialized query operations that are not well supported by the relational model
- Frustration with the restrictiveness of relational schemas, and a desire for a more dynamic and expressive data model

### Object relational mismatch

Most application development today is done in **object-oriented programming languages**, which leads to a common criticism of the SQL data model: if data is stored in relational tables, an awkward translation layer is required between the objects in the application code and the database model of tables, rows, and columns. The disconnect between the models is sometimes called an **impedance mismatch**.

If we have a nested data structure like this

``` json
{
  "user_id":     251,
  "first_name":  "Bill",
  "last_name":   "Gates",
  "summary":     "Co-chair of the Bill & Melinda Gates... Active blogger.",
  "region_id":   "us:91",
  "industry_id": 131,
  "photo_url":   "/p/7/000/253/05b/308dd6e.jpg",
  "positions": [
    {
        "job_title": "Co-chair",
        "organization": "Bill & Melinda Gates Foundation"
    },
    {
        "job_title": "Co-founder, Chairman",
        "organization": "Microsoft"
    }
  ],
  "education": [
    {
        "school_name": "Harvard University",
        "start": 1973,
        "end": 1975
    },
    {
        "school_name": "Lakeside School, Seattle",
        "start": null,
        "end": null
    }
  ],
  "contact_info": {
    "blog":    "http://thegatesnotes.com",
    "twitter": "http://twitter.com/BillGates"
  }
}
```

We have 3 options if we want to store this in relational DB -  

1. Use normalized representation to put **positions**, **education** and **contact** in separate tables with foreign key reference to **user** table.
2. Store the record as a single row in relational DBs which support XML or JSON format (like Postgres).
3. Store this as text and  let the application interpret its structure and content.

#### Document(JSON) model

**Pros**

- JSON model reduces the impedance mismatch between the application code and the storage layer.
- It has better locality than the multi-table schema.
- Very good for storing data having tree structure

**Cons**

- Sometimes normalization is necessary to avoid duplication of data. e.g. - Organization and School Name can be references instead of plain text.
- Joining is difficult in document based DBs. If DB doesn't support joins we have to emulate joins in application layer by making multiple queries to DB.
- Even if the initial version of an application fits well in a join-free document model, data has a tendency of becoming more interconnected as features are added to applications.

## Network model

- Various solutions were proposed to solve the limitations of the hierarchical model. The two most prominent were the relational model (which became SQL, and took over the world) and the network model.
- In the tree structure of the hierarchical model, every record has exactly one parent; in the network model, a record could have multiple parents. This allowed many-to-one and many-to-many relationships to be modeled.
- The links between records in the network model were not **foreign keys**, but more like **pointers** in a programming language (while still being stored on disk). The only way of accessing a record was to follow a path from a root record along these chains of links. This was called an **access path**.
- Although manual access path selection was able to make the most efficient, the problem was that they made the code for querying and updating the database complicated and in flexible.

## Relational model

- In relational model, all data is laid out in open. A relation (table) is simply a collection of tuples (rows), and that’s it.
- You can read a particular row by designating some columns as a key and matching on those. You can insert a new row into any table without worrying about foreign key relationships to and from other tables.
- In a relational database, the **query optimizer** automatically decides which parts of the query to execute in which order, and which indexes to use. Those choices are effectively the “access path,” but the big difference is that they are made automatically by the query optimizer, not by the application developer, so we rarely need to think about them.
- If you want to query your data in new ways, you can just declare a new index, and queries will automatically use whichever indexes are most appropriate.
- You only need to build a query optimizer once, and then all applications that use the database can benefit from it.

## Document model

- Document databases reverted back to the **hierarchical model** in one aspect: storing nested records within their parent record rather than in a separate table.
- However, when it comes to representing many-to-one and many-to-many relationships, relational and document databases are not fundamentally different: in both cases, the related item is referenced by a unique identifier, which is called a **foreign key** in the **relational model** and a **document reference** in the **document model**.

### Schema flexibility in document model

- Most document databases, and the JSON support in relational databases, do not enforce any schema on the data in documents. 
- No schema means that arbitrary keys and values can be added to a document, and when reading, clients have no guarantees as to what fields the documents may contain.
- Document databases are sometimes called **schemaless**, but that’s misleading, as the code that reads the data usually assumes some kind of structure—i.e., there is an implicit schema, but it is not enforced by the database.
- A more accurate term is **schema-on-read** (the structure of the data is implicit, and only interpreted when the data is read), in contrast with **schema-on-write** (the traditional approach of relational databases, where the schema is explicit and the database ensures all written data conforms to it).
- The schema-on-read approach is advantageous if the items in the collection don’t all have the same structure for some reason (i.e., the data is heterogeneous).

### Data locality

- A document is usually stored as a single continuous string, encoded as JSON, XML, or a binary variant thereof (such as MongoDB’s BSON).
- If your application often needs to access the entire document (for example, to render it on a web page), there is a performance advantage to this storage locality.
- If data is split across multiple tables, multiple index lookups are required to retrieve it all, which may require more disk seeks and take more time.
- Some of the databases that enforce data locality are
  - Google’s Spanner
  - Oracle (multi-table index cluster tables)
  - Cassandra and HBase (column-family concept)

## Query languages for data

- SQL is a declarative query language.
- An imperative language tells the computer to perform certain operations in a certain order. You can imagine stepping through the code line by line, evaluating conditions, updating variables, and deciding whether to go around the loop one more time.
- In a declarative query language, like SQL or relational algebra, you just specify the pattern of the data you want—what conditions the results must meet, and how you want the data to be transformed (e.g., sorted, grouped, and aggregated)—but not how to achieve that goal. It is up to the database system’s query optimizer to decide.

### Map reduce querying

- MapReduce is a programming model for processing large amounts of data in bulk across manymachines, popularized by Google.
- MapReduce is neither a declarative query language nor a fully imperative query API, but somewherein between: the logic of the query is expressed with snippets of code, which are called repeatedlyby the processing framework. 
- It is based on the **map** (also known as collect) and **reduce** (alsoknown as fold or inject) functions that exist in many functional programming languages.
- The map and reduce functions must be pure functions, which means they only use the data that is passed to them as input, they cannot perform additional database queries, and they must not have any side effects.
- These restrictions allow the database to run the functions anywhere, in any order, and rerun them on failure.
- A usability problem with MapReduce is that you have to write two carefully coordinated functions, which is often harder than writing a single query. Moreover, a declarative query language offers more opportunities for a query optimizer to improve the performance of a query. e.g.- MongoDB 2.2 added support for a declarative query language called the aggregation pipeline.

## Graph data models

- If **many-to-many relationships** are very common in your data, the relational model can handle simple cases of many-to-many relationships, but as the connections within your data become more complex, it becomes more natural to start modeling your data as a **graph**.
- A graph consists of two kinds of objects: **vertices** (also known as nodes or entities) and **edges** (also known as relationships or arcs).
- Typical examples include:
  - Social graphs
  - The web graph
  - Road or rail networks

### Property graphs

In the property graph model, each vertex consists of:
1. A unique identifier
2. A set of outgoing edges
3. A set of incoming edges
4. A collection of properties (key-value pairs)

Each edge consists of:

1. A unique identifier
2. The vertex at which the edge starts (the tail vertex)
3. The vertex at which the edge ends (the head vertex)
4. A label to describe the kind of relationship between the two vertices
5. A collection of properties (key-value pairs)

Here's how we can represent property graph using relational schema.

```sql
CREATE TABLE vertices (
    vertex_id   integer PRIMARY KEY,
    properties  json
);

CREATE TABLE edges (
    edge_id     integer PRIMARY KEY,
    tail_vertex integer REFERENCES vertices (vertex_id),
    head_vertex integer REFERENCES vertices (vertex_id),
    label       text,
    properties  json
);

CREATE INDEX edges_tails ON edges (tail_vertex);
CREATE INDEX edges_heads ON edges (head_vertex);
```

Some important aspects of this model are:

1. Any vertex can have an edge connecting it with any other vertex. There is no schema that restricts which kinds of things can or cannot be associated.
2. Given any vertex, you can efficiently find both its incoming and its outgoing edges, and thus traverse the graph—i.e., follow a path through a chain of vertices—both forward and backward.
3. By using different labels for different kinds of relationships, you can store several different kinds of information in a single graph, while still maintaining a clean data model.

### Cypher query language

- **Cypher** is a declarative query language for property graphs, created for the **Neo4j** graph database.

A sample cypher ddl
```sql
CREATE
  (NAmerica:Location {name:'North America', type:'continent'}),
  (USA:Location      {name:'United States', type:'country'  }),
  (Idaho:Location    {name:'Idaho',         type:'state'    }),
  (Lucy:Person       {name:'Lucy' }),
  (Idaho) -[:WITHIN]->  (USA)  -[:WITHIN]-> (NAmerica),
  (Lucy)  -[:BORN_IN]-> (Idaho)
```

Cypher query to find people who emigrated from the US to Europe
```sql
MATCH
  (person) -[:BORN_IN]->  () -[:WITHIN*0..]-> (us:Location {name:'United States'}),
  (person) -[:LIVES_IN]-> () -[:WITHIN*0..]-> (eu:Location {name:'Europe'})
RETURN person.name
```

## Summary

- Data started out being represented as one big tree (the hierarchical model), but that wasn’t good for representing many-to-many relationships, so the relational model was invented to solve that problem.
- More recently, developers found that some applications don’t fit well in the relational model either. New nonrelational “NoSQL” datastores have diverged in two main directions:
  1. **Document databases** target use cases where data comes in self-contained documents and relationships between one document and another are rare.
  2. **Graph databases** go in the opposite direction, targeting use cases where anything is potentially related to everything.
- One thing that document and graph databases have in common is that they typically don’t enforce aschema for the data they store, which can make it easier to adapt applications to changing requirements.