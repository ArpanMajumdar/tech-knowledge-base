# Start zookeeper
bin/zookeeper-server-start.sh config/zookeeper.properties

# Start kafka server
bin/kafka-server-start.sh config/server.properties

# Create a topic
bin/kafka-topics.sh --create --bootstrap-server localhost:9092 --replication-factor 1 --partitions 1 --topic test

# List topics 
bin/kafka-topics.sh --list --bootstrap-server localhost:9092

# Run console producer
bin/kafka-console-producer.sh --broker-list localhost:9092 --topic test

# Run console consumer
bin/kafka-console-consumer.sh --bootstrap-server localhost:9092 --topic test --from-beginning


bin/kafka-console-consumer.sh --bootstrap-server localhost:9092 --topic word-count-output \ 
--from-beginning \
--formatter kafka.tools.DefaultMessageFormatter \
--property print.key true \
--property print.value true \
--property key.deserializer=org.apache.kafka.common.serialization.StringDeserializer
--property value.deserializer=org.apache.kafka.common.serialization.LongDeserializer