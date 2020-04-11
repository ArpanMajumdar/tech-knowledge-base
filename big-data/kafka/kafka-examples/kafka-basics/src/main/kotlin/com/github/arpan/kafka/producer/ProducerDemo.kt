package com.github.arpan.kafka.producer

import org.apache.kafka.clients.producer.*
import org.apache.kafka.common.serialization.StringSerializer
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import java.lang.Exception
import java.util.*
import java.util.concurrent.Future

fun main() {

    // Kafka config
    val bootstrapServer = "localhost:9092"
    val topic = "test2"

    val kafkaConfig = ProducerDemo.getKafkaConfig(bootstrapServer)
    val producer = ProducerDemo.createKafkaProducer(kafkaConfig)

    repeat(10) { index ->
        // Records with same key are always produced to the same partition every time main() is re-run
        val record = ProducerDemo.createRecord(topic, "id_$index", "Message $index")
        ProducerDemo.sendRecordAsync(producer, record)
    }

    ProducerDemo.flushAndCloseProducer(producer)
}

object ProducerDemo {
    private val logger: Logger = LoggerFactory.getLogger(ProducerDemo::class.java)

    fun getKafkaConfig(bootstrapServer: String): Properties = Properties().apply {
        setProperty(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServer)
        setProperty(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer::class.java.name)
        setProperty(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer::class.java.name)
    }

    fun createKafkaProducer(kafkaConfig: Properties): KafkaProducer<String, String> =
        KafkaProducer<String, String>(kafkaConfig)

    fun createRecord(topic: String, key: String?, value: String) =
        if (key == null) ProducerRecord(topic, value) else ProducerRecord(topic, key, value)

    fun sendRecordAsync(
        producer: KafkaProducer<String, String>,
        record: ProducerRecord<String, String>
    ): Future<RecordMetadata> =
        producer.send(record) { metadata, exception: Exception? ->
            if (exception == null)
                logger.info("Produced record ${record.key()} -> ${record.value()} to Topic: ${metadata.topic()} on Partition: ${metadata.partition()} and Offset: ${metadata.offset()} on Timestamp: ${metadata.timestamp()}")
            else
                logger.error("Error producing record to topic ${metadata.topic()}: ", exception)
        }

    fun flushAndCloseProducer(producer: KafkaProducer<String, String>) {
        producer.flush()
        producer.close()
    }
}