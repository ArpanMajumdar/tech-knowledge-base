# Chapter - 4: Encoding and evolution

- Relational databases generally assume that all data in the database conforms to one schema: although that schema can be changed (through schema migrations; i.e., ALTER statements), there is exactly one schema in force at any one point in time.
- Schema-on-read (“schemaless”) databases don’t enforce a schema, so the database can contain a mixture of older and newer data formats written at different times.
- However, in a large application, code changes often cannot happen instantaneously:
  - With server-side applications you may want to perform a rolling upgrade (also known as a staged rollout), deploying the new version to a few nodes at a time, checking whether the new version is running smoothly, and gradually working your way through all the nodes.
  - With client-side applications you’re at the mercy of the user, who may not install the update for some time.
  - In order for the system to continue running smoothly, we need to maintain compatibility in both directions:
    - **Backward compatibility:** Newer code can read data that was written by older code.
    - **Forward compatibility:** Older code can read data that was written by newer code.

## Formats for encoding data

- Programs usually work with data in (at least) two different representations:
1. **In memory**, data is kept in objects, **structs, lists, arrays, hash tables, trees,** and so on. These data structures are optimized for efficient access and manipulation by the CPU (typically using pointers).
2. When you want to **write data to a file or send it over the network**, you have to encode it as some kind of self-contained sequence of bytes (for example, a JSON document). Since a pointer wouldn’t make sense to any other process, this sequence-of-bytes representation looks quite different from the data structures that are normally used in memory.
- The translation from the in-memory representation to a byte sequence is called **encoding (also known as serialization or marshalling)**, and the reverse is called **decoding (parsing, deserialization, unmarshalling)**.

### Language specific formats

Many programming languages come with built-in support for encoding in-memory objects into byte sequences. For example,
- Java (`java.io.Serializable`)
- Ruby (`Marshal`)
- Python (`pickle`)

These language specific libraries are convenient but they have a number of problems:
- The encoding is often tied to a particular programming language, and reading the data in another language is very difficult.
- This is frequently a source of security problems.
- As they are intended for quick and easy encoding of data, they often neglect the inconvenient problems of forward and backward compatibility.
- Efficiency (CPU time taken to encode or decode, and the size of the encoded structure) is also often an afterthought.

### JSON, XML and Binary variants

JSON example record
``` json
{
    "userName": "Martin",
    "favoriteNumber": 1337,
    "interests": ["daydreaming", "hacking"]
}
```

- Moving to standardized encodings that can be written and read by many programming languages, **JSON** and **XML** are the obvious contenders.
- **CSV** is another popular language-independent format, albeit less powerful.
- Besides the superficial syntactic issues, they also have some subtle problems:
  - There is a lot of ambiguity around the **encoding of numbers**. In XML and CSV, you cannot distinguish between a number and a string that happens to consist of digits (except by referring to an external schema). JSON distinguishes strings and numbers, but it **doesn’t distinguish integers and floating-point numbers**, and it doesn’t specify a precision.
  - JSON and XML have good support for Unicode character strings (i.e., human-readable text), but they don’t support binary strings.
  - There is **optional schema support** for both XML and JSON. These schema languages are quite powerful, and thus quite complicated to learn and implement. CSV does not have any schema, so it is up to the application to define the meaning of each row and column.
- You could choose a format that is more compact or faster to parse. For a small dataset, the gains are negligible, but once you get into the terabytes, the choice of data format can have a big impact.
- JSON is less verbose than XML, but both still use a lot of space compared to binary formats. This observation led to the development of a profusion of **binary encodings for JSON (MessagePack, BSON, BJSON, UBJSON, BISON, and Smile, to name a few)** and for **XML (WBXML and Fast Infoset, for example)**. These formats have been adopted in various niches, but none of them are as widely adopted as the textual versions of JSON and XML.

### Thrift and protocol buffers

- **Apache Thrift** and **Protocol Buffers (protobuf)** are binary encoding libraries that are based on the same principle. Protocol Buffers was originally developed at **Google**, Thrift was originally developed at **Facebook**.
- Both Thrift and Protocol Buffers require a schema for any data that is encoded. To encode the data in Thrift/Protobuf, you would describe the schema in the Thrift interface definition language (IDL) like this:

Thrift schema example
``` thrift
struct Person {
  1: required string       userName,
  2: optional i64          favoriteNumber,
  3: optional list<string> interests
}
```

Protocol buffer schema example
``` protobuf
message Person {
    required string user_name       = 1;
    optional int64  favorite_number = 2;
    repeated string interests       = 3;
}
```

- Thrift and Protocol Buffers each come with a code generation tool that takes a schema definition like the ones shown here, and produces classes that implement the schema in various programming languages. Your application code can call this generated code to encode or decode records of the schema.
- Each field has a type annotation (to indicate whether it is a string, integer, list, etc.) and, where required, a length indication (length of a string, number of items in a list).
- The big difference between binary JSON format and this is that there are no field names (userName, favoriteNumber, interests). Instead, the encoded data contains field tags, which are numbers (1, 2, and 3). Those are the numbers that appear in the schema definition. Field tags are like aliases for fields—they are a compact way of saying what field we’re talking about, without having to spell out the field name.
- Each field was marked either **required** or **optional**, but this makes no difference to how the field is encoded (nothing in the binary data indicates whether a field was required). The difference is simply that required enables a runtime check that fails if the field is not set, which can be useful for catching bugs.
- Protocol Buffers does not have a list or array datatype, but instead has a **repeated** marker for fields (which is a third option alongside required and optional).
- Thrift has a dedicated list datatype, which is parameterized with the datatype of the list elements. This does not allow the same evolution from single-valued to multi-valued as ProtocolBuffers does, but it has the advantage of **supporting nested lists**.

#### Schema evolution in Thrift and Protocol buffers

- Schemas inevitably need to change over time. We call this **schema evolution**.
- An encoded record is just the concatenation of its encoded fields.Each field is identified by its **tag number** (the numbers 1, 2, 3 in the sample schemas) and annotated with a **datatype** (e.g., string or integer). If a field value is not set, it is simply omitted from the encoded record. From this you can see that field tags are critical to the meaning of the encoded data. You can change the name of a field in the schema, since the encoded data never refers to field names, but you cannot change a field’s tag, since that would make all existing encoded data invalid.
- You can add new fields to the schema, provided that you give each field a new tag number. 
- If old code tries to read data written by new code, including a new field with a tag number it doesn’t recognize, it can simply ignore that field. This maintains **forward compatibility**.
- As long as each field has a unique tag number, new code can always read old data, because the tag numbers still have the same meaning. The only detail is that if you add a new field, you cannot make it required. Therefore, to maintain **backward compatibility**, every field you add after the initial deployment of the schema must be optional or have a default value.

### Avro

- Apache Avro is another binary encoding format. 
- Avro also uses a schema to specify the structure of the data being encoded. It has two schema languages: one (**Avro IDL**) intended for **human editing**, and one (based on **JSON**) that is more easily **machine-readable**.

**Avro IDL example**
``` avro
record Person {
    string               userName;
    union { null, long } favoriteNumber = null;
    array<string>        interests;
}
```

**Avro schema JSON**
```json
{
    "type": "record",
    "name": "Person",
    "fields": [
        {"name": "userName",       "type": "string"},
        {"name": "favoriteNumber", "type": ["null", "long"], "default": null},
        {"name": "interests",      "type": {"type": "array", "items": "string"}}
    ]
}
```

- There are no tag numbers in the schema.
- Avro binary encoding is the most compact of all the encodings.
- If you examine the byte sequence, you can see that there is nothing to identify fields or their data types. The encoding simply consists of values concatenated together.
- To parse the binary data, you go through the fields in the order that they appear in the schema and use the schema to tell you the datatype of each field. This means that the binary data can only be decoded correctly if the code reading the data is using the exact same schema as the code that wrote the data. Any mismatch in the schema between the reader and the writer would mean incorrectly decoded data.
- Avro is friendlier to **dynamically generated schemas**. If you use Avro, you can fairly easily generate an Avro schema (in the JSON representation we saw earlier) from the relational schema and encode the database contents using that schema, dumping it all to an Avro object container file.
- Avro provides **optional code generation** for statically typed programming languages, but it can be used just as well without any code generation. If you have an object container file (which embeds the writer’s schema), you can simply open it using the Avro library and look at the data in the same way as you could look at a JSON file. The file is self-describing since it includes all the necessary metadata.

#### Writer's schema and reader's schema

- With Avro, when an application wants to encode some data, it encodes the data using whatever version of the schema it knows about—for example, that schema may be compiled into the application. This is known as the **writer’s schema**.
- When an application wants to decode some data , it is expecting the data to be in some schema, which is known as the **reader’s schema**.
- The key idea with Avro is that the writer’s schema and the reader’s schema don’t have to be the same—they only need to be compatible. When data is decoded (read), the Avro library resolves the differences by looking at the writer’s schema and the reader’s schema side by side and translating the data from the writer’s schema into the reader’s schema.

#### Schema evolution

- To maintain compatibility, you may only add or remove a field that has a default value.
- If you were to add a field that has no default value, new readers wouldn’t be able to read data written by old writers, so you would break backward compatibility. If you were to remove a field that has no default value, old readers wouldn’t be able to read data written by new writers, so you would break forward compatibility.
- In Avro, if you want to allow a field to be null, you have to use a union type. For example, `union { null, long}`.
- Changing the datatype of a field is possible, provided that Avro can convert the type. 

#### Where avro can be used?

- Storing a large file with lots of records(like in Hadoop)
- Database with individually written records(include a version number at the beginning of every encoded record)
- Sending records over a network connection(negotiate the schema version on connection setup)

### Merits of schemas

Binary encodings based on schemas have a number of nice properties -

- They can be much more compact than the various “binary JSON” variants, since they can **omit field names from the encoded data**.
- The schema is a valuable form of documentation, and because the schema is required for decoding, you can be sure that it is up to date (whereas manually maintained documentation may easily diverge from reality).
- Keeping a database of schemas allows you to **check forward and backward compatibility** of schema changes, before anything is deployed.
- For users of statically typed programming languages, the **ability to generate code** from the schema is useful, since it enables type checking at compile time.

## Modes of dataflow

There can be number of ways data can flow between processes -
- via databases
- via service calls(REST or RPC)
- via asynchronous message passing

### Dataflow through databases

1. **Backward compatibility** - There may just be a single process accessing the database, in which case the reader is simply a later version of the same process—in that case you can think of storing something in the database as sending a message to your future self. Backward compatibility is clearly necessary here; otherwise your future self won’t be able to decode what you previously wrote.
2. **Forward compatibility** -  It’s common for several different processes to be accessing a database at the same time. Those processes might be several different applications or services, or they may simply be several instances of the same service. Either way, in an environment where the application is changing, it is likely that some processes accessing the database will be running newer code and some will be running older code—for example because a new version is currently being deployed in a rolling upgrade, so some instances have been updated while others haven’t yet. This means that a value in the database may be written by a newer version of the code, and subsequently read by an older version of the code that is still running. Thus, forward compatibility is also often required for databases.

- If a newer code writes a new value and read by the older code, updates and writes it back often new field is lost in the translation process. Desirable behavior is usually for the old code to keep the new field intact, even though it couldn’t be interpreted.
- The encoding formats discussed previously support such preservation of unknown fields, but sometimes you need to take care at an application level.
- When you deploy a new version of your application (of a server-side application, at least), you may entirely replace the old version with the new version within a few minutes. The same is not true of database contents: the five-year-old data will still be there, in the original encoding, unless you have explicitly rewritten it since then.
- Rewriting (migrating) data into a new schema is certainly possible, but it’s an expensive thing to do on a large dataset, so most databases avoid it if possible. 
- Schema evolution thus allows the entire database to appear as if it was encoded with a single schema, even though the underlying storage may contain records encoded with various historical versions of the schema.
- Perhaps you take a snapshot of your database from time to time, say for backup purposes or for loading into a data warehouse.
- As the data dump is written in one go and is thereafter immutable, formats like Avro object container files are a good fit. This is also a good opportunity to encode the data in an analytics-friendly column-oriented format such as **Parquet**.

### Dataflow through services: REST and RPC

- When you have processes that need to communicate over a network, there are a few different ways of arranging that communication. The most common arrangement is to have two roles: **clients** and **servers**. The servers expose an API over the network, and the clients can connect to the servers to make requests to that **API**. The API exposed by the server is known as a **service**.
- The server’s response is typically not HTML for displaying to a human, but rather data in an encoding that is convenient for further processing by the client-side application code (such as JSON). Although HTTP may be used as the transport protocol, the API implemented on top is application-specific, and the client and server need to agree on the details of that API.
- Some examples of clients are
  - **Web browsers** - Make **GET** requests to download HTML, CSS, JavaScript, images, etc., and making **POST** requests to submit data to the server.
  - Native app running on a mobile device or desktop
  - A server can itself be a client to another service or database
- Services are similar to databases: they typically allow clients to submit and query data. However, while databases allow arbitrary queries using the query languages, services expose an application-specific API that only allows inputs and outputs that are predetermined by the business logic. This restriction provides a degree of encapsulation: services can impose fine-grained restrictions on what clients can and cannot do.
- A key design goal of a service-oriented/microservices architecture is to make the application easier to change and maintain by making services independently deployable and evolvable.

#### Web services

- There are two popular approaches to web services: **REST** and **SOAP**. They are almost diametrically opposed in terms of philosophy

**REST**
- REST is not a protocol, but rather a design philosophy that builds upon the principles of HTTP. 
- It emphasizes simple data formats, using URLs for identifying resources and using HTTP features for cache control, authentication, and content type negotiation. 
- REST has been gaining popularity compared to SOAP, at least in the context of cross-organizational service integration, and is often associated with microservices. 
- An API designed according to the principles of REST is called **RESTful**.
- A definition format such as **OpenAPI**, also known as Swagger, can be used to describe RESTful APIs and produce documentation.

**SOAP**
- SOAP is an **XML-based protocol** for making network API requests.
- Although it is most commonly used over HTTP, it aims to be independent from HTTP and avoids using most HTTP features. Instead, it comes with a sprawling and complex multitude of related standards(the web service framework, known as WS-*) that add various features.
- The API of a SOAP web service is described using an XML-based language called the **Web Services Description Language, or WSDL**. 
- WSDL enables code generation so that a client can access a remote service using local classes and method calls.
- As WSDL is not designed to be human-readable, and as SOAP messages are often too complex to construct manually, users of SOAP rely heavily on tool support, code generation, and IDEs.
- Although SOAP is still used in many large enterprises, it has fallen out of favor in most smaller companies.

#### Remote Procedure Calls (RPCs)

- The RPC model tries to make a request to a remote network service look the same as **calling a function or method in your programming language**, within the same process (this abstraction is called location transparency).
- Although RPC seems convenient at first, the approach is fundamentally flawed. A network request is very different from a local function call:
  - A local function call is predictable and either succeeds or fails, depending only on parameters that are under your control. A network request is unpredictable: the request or response may be lost due to a network problem, or the remote machine may be slow or unavailable, and such problems are entirely outside of your control.
  - A local function call either returns a result, or throws an exception, or never returns (because it goes into an infinite loop or the process crashes). A network request has another possible outcome: it may return without a result, due to a timeout.
  - Every time you call a local function, it normally takes about the same time to execute. A network request is much slower than a function call, and its latency is also wildly variable.
  - When you call a local function, you can efficiently pass it references (pointers) to objects in local memory. When you make a network request, all those parameters need to be encoded into a sequence of bytes that can be sent over the network. That’s okay if the parameters are primitives like numbers or strings, but quickly becomes problematic with larger objects.
  - The client and the service may be implemented in different programming languages, so the RPC framework must translate datatypes from one language into another.
-  New generation of RPC frameworks is more explicit about the fact that a remote request is different from a local function call.
-  **gRPC** supports streams, where a call consists of not just one request and one response, but a series of requests and responses over time.
- Some of these frameworks also provide **service discovery**—that is, allowing a client to find out at which IP address and port number it can find a particular service.
- Custom RPC protocols with a binary encoding format can achieve better performance than something generic like JSON over REST. However, a RESTful API has other significant advantages: it is good for experimentation and debugging and it is supported by all mainstream programming languages and platforms.

### Message-passing dataflow

- Asynchronous message-passing systems are somewhere between RPC and databases. They are similar to RPC in that a client’s request (usually called a message) is delivered to another process with low latency. They are similar to databases in that the message is not sent via a direct network connection, but goes via an intermediary called a **message broker** (also called a message queue or message-oriented middleware), which stores the message temporarily.
- Using a message broker has several advantages compared to direct RPC:
  - It can act as a buffer if the recipient is unavailable or overloaded, and thus improve system reliability.
  - It can automatically redeliver messages to a process that has crashed, and thus prevent messages from being lost.
  - It avoids the sender needing to know the IP address and port number of the recipient (which is particularly useful in a cloud deployment where virtual machines often come and go).
  - It allows one message to be sent to several recipients.
  - It logically decouples the sender from the recipient (the sender just publishes messages and doesn’t care who consumes them).
- This communication pattern is asynchronous: the sender doesn’t wait for the message to be delivered, but simply sends it and then forgets about it.

#### Message brokers

- In general, **message brokers** are used as follows: one process sends a message to a named queue or topic, and the broker ensures that the message is delivered to one or more consumers of or subscribers to that queue or topic. 
- There can be many producers and many consumers on the same topic.
- A topic provides only one-way dataflow. However, a consumer may itself publish messages to another topic.
- Message brokers typically don’t enforce any particular data model—**a message is just a sequence of bytes with some metadata**, so you can use any encoding format. If the encoding is backward and forward compatible, you have the greatest flexibility to change publishers and consumers independently and deploy them in any order.

#### Distributed actor frameworks

- The actor model is a programming model for concurrency in a single process. 
- Rather than dealing directly with threads (and the associated problems of race conditions, locking, and deadlock), logic is encapsulated in **actors**. 
- Each actor typically represents one client or entity, it may have some local state (which is not shared with any other actor), and it communicates with other actors by sending and receiving asynchronous messages. Message delivery is not guaranteed. In certain error scenarios, messages will be lost. 
- Since each actor processes only one message at a time, it doesn’t need to worry about threads, and each actor can be scheduled independently by the framework.
- In **distributed actor frameworks**, this programming model is used to scale an application across multiple nodes. 
- The same message-passing mechanism is used, no matter whether the sender and recipient are on the same node or different nodes. 
- If they are on different nodes, the message is transparently encoded into a byte sequence, sent over the network, and decoded on the other side.
- A distributed actor framework essentially integrates a message broker and the actor programming model into a single framework.
- Some popular distributed actor frameworks handle message encoding as follows:
  - Akka
  - Orleans
  - Erlang OTP

## Summary

- Many services need to support rolling upgrades, where a new version of a service is gradually deployed to a few nodes at a time, rather than deploying to all nodes simultaneously. Rolling upgrades allow new versions of a service to be released without downtime (thus encouraging frequent small releases over rare big releases) and make deployments less risky (allowing faulty releases to be detected and rolled back before they affect a large number of users). These properties are hugely beneficial for evolvability, the ease of making changes to an application.
- It is important that all data flowing around the system is encoded in a way that provides backward compatibility (new code can read old data) and forward compatibility (old code can read new data).
- There are several data encoding formats and their compatibility properties:
  - **Programming language–specific encodings** are restricted to a single programming language and often fail to provide forward and backward compatibility.
  - Textual formats like **JSON, XML, and CSV** are widespread, and their compatibility depends on how you use them. 
  - Binary schema–driven formats like **Thrift, Protocol Buffers, and Avro** allow compact, efficient encoding with clearly defined forward and backward compatibility semantics. The schemas can be useful for documentation and code generation in statically typed languages.
- There are several modes of dataflow:
  - **Databases**, where the process writing to the database encodes the data and the process reading from the database decodes it.
  - **RPC** and **REST APIs**, where the client encodes a request, the server decodes the request and encodes a response, and the client finally decodes the response.
  - **Asynchronous message passing (using message brokers or actors)**, where nodes communicate by sending each other messages that are encoded by the sender and decoded by the recipient.



