package com.github.arpan.kafka.consumer

import org.apache.kafka.clients.consumer.ConsumerConfig
import org.apache.kafka.clients.consumer.KafkaConsumer
import org.apache.kafka.common.serialization.StringDeserializer
import java.util.*

class KafkaConsumerHelper(private val bootstrapServer: String, private val consumerGroup: String) {
    private val kafkaConfig: Properties = Properties().apply {
        setProperty(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServer)
        setProperty(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer::class.java.name)
        setProperty(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer::class.java.name)
        setProperty(ConsumerConfig.GROUP_ID_CONFIG, consumerGroup)
        setProperty(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest")
        setProperty(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, "false") // Disable auto-commit of offsets
        setProperty(ConsumerConfig.MAX_POLL_RECORDS_CONFIG, "50")
    }

    private fun createConsumer(kafkaConfig: Properties) = KafkaConsumer<String, String>(kafkaConfig)

    val consumer: KafkaConsumer<String, String> = createConsumer(kafkaConfig)
}