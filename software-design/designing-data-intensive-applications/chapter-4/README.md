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

