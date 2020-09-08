package com.github.arpan.kafka.consumer.helper

import org.apache.kafka.clients.consumer.ConsumerConfig
import org.apache.kafka.clients.consumer.ConsumerRecord
import org.apache.kafka.clients.consumer.ConsumerRecords
import org.apache.kafka.clients.consumer.KafkaConsumer
import org.apache.kafka.common.TopicPartition
import org.apache.kafka.common.serialization.StringDeserializer
import org.slf4j.LoggerFactory
import java.time.Duration
import java.util.*

object ConsumerHelper {
    private val logger = LoggerFactory.getLogger(ConsumerHelper::class.java)

    fun getKafkaConfig(bootstrapServer: String, consumerGroup: String): Properties = Properties().apply {
        setProperty(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServer)
        setProperty(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer::class.java.name)
        setProperty(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer::class.java.name)
        setProperty(ConsumerConfig.GROUP_ID_CONFIG, consumerGroup)
        setProperty(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest")
    }

    fun createConsumer(kafkaConfig: Properties) = KafkaConsumer<String, String>(kafkaConfig)

    fun subscribeToTopics(consumer: KafkaConsumer<String, String>, vararg topics: String) =
        consumer.subscribe(topics.toList())

    fun pollIndefinitely(consumer: KafkaConsumer<String, String>, duration: Duration) {
        while (true) {
            val records = consumer.poll(duration)
            records.logConsumerRecords()
        }
    }

    fun pollFixedNumberOfMessages(consumer: KafkaConsumer<String, String>, duration: Duration, numMessagesToRead: Int) {
        val records = consumer.poll(duration)
        val totalRecords = records.count()

        if (totalRecords > numMessagesToRead) {
            records.take(numMessagesToRead).logConsumerRecords()
        } else {
            var recordsLeft = numMessagesToRead - totalRecords
            records.logConsumerRecords()
            while (recordsLeft > 0) {
                val remainingRecords = consumer.poll(duration)
                if (remainingRecords.count() < recordsLeft) {
                    remainingRecords.take(recordsLeft).logConsumerRecords()
                } else {
                    remainingRecords.logConsumerRecords()
                    recordsLeft -= remainingRecords.count()
                }
            }
        }

    }

    fun assignPartition(consumer: KafkaConsumer<String, String>, partitions: List<TopicPartition>) =
        consumer.assign(partitions)

    fun seek(consumer: KafkaConsumer<String, String>, partition: TopicPartition, offset: Long) =
        consumer.seek(partition, offset)

    private fun ConsumerRecords<String, String>.logConsumerRecords() = this.forEach { record ->
        logger.info(
            getLogMessageFromConsumerRecord(
                record
            )
        )
    }

    private fun List<ConsumerRecord<String, String>>.logConsumerRecords() = this.forEach { record ->
        logger.info(
            getLogMessageFromConsumerRecord(
                record
            )
        )
    }

    private fun getLogMessageFromConsumerRecord(record: ConsumerRecord<String, String>) =
        "Received record ${record.key()} -> ${record.value()} from Topic ${record.topic()} from Partition: ${record.partition()} and Offset: ${record.offset()} at Timestamp: ${record.timestamp()}"
}