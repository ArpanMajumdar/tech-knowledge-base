# kafka-streams-demo-java
Kafka streams demo application

## Internal topics

Running kafka streams may create internal intermediary topics. They are of 2 types
    1. **Repartitioning topics** - In case you start transforming the key of your stream, a repartitioning will happen at some processor.
    2. **Changelog topics** - In case you perform aggregations, Kafka streams will save compacted data in these topics.
    
Internal topics are 
- managed by Kafka streams
- are used by kafka streams to save/restore state and repartition data
- are prefixed by `application.id` parameter. 
- should never be deleted, altered or published to. They are internal to kafka streams.

## KStreams
- All inserts
- Similar to a log
- Infinite
- Unbounded data streams