package com.github.arpan.kafka.producer

import org.apache.kafka.clients.producer.*
import org.apache.kafka.common.serialization.StringSerializer
import org.slf4j.LoggerFactory
import java.util.*
import java.util.concurrent.Future

object ProducerFactory {
    fun create(bootstrapServer: String): KafkaProducer<String, String> {
        val kafkaConfig = Properties().apply {
            setProperty(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServer)
            setProperty(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer::class.java.name)
            setProperty(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer::class.java.name)

            // Enable idempotence
            setProperty(ProducerConfig.ENABLE_IDEMPOTENCE_CONFIG, "true")
            setProperty(ProducerConfig.ACKS_CONFIG, "all")
            setProperty(ProducerConfig.MAX_IN_FLIGHT_REQUESTS_PER_CONNECTION, "5")
            setProperty(ProducerConfig.RETRIES_CONFIG, Int.MAX_VALUE.toString())

            // High throughput producer at the expense of latency and CPU usage
            setProperty(ProducerConfig.COMPRESSION_TYPE_CONFIG, "gzip")
            setProperty(ProducerConfig.LINGER_MS_CONFIG, "20")
            setProperty(ProducerConfig.BATCH_SIZE_CONFIG, "${64 * 1024}") // 64KB
        }
        return KafkaProducer<String, String>(kafkaConfig)
    }
}

object ProducerHelper {
    private val logger = LoggerFactory.getLogger(ProducerHelper::class.java)

    fun sendRecord(producer: Producer<String, String>, topic: String, key: String? = null, message: String):
            Future<RecordMetadata> {
        val record = if (key == null) ProducerRecord(topic, message)
        else ProducerRecord(topic, key, message)
        return producer.send(record) { metadata, exception: Exception? ->
            if (exception == null)
                logger.info("Produced record ${record.key()} -> ${record.value()} to Topic: ${metadata.topic()} on Partition: ${metadata.partition()} and Offset: ${metadata.offset()} on Timestamp: ${metadata.timestamp()}")
            else
                logger.error("Error producing record to topic ${metadata.topic()}: ", exception)
        }
    }
}