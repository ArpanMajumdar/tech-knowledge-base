package com.github.arpan.kafka.consumer.seekandassign

import com.github.arpan.kafka.consumer.helper.ConsumerHelper
import org.apache.kafka.common.TopicPartition
import java.time.Duration

fun main() {
    // Consumer config
    val bootstrapServer = "localhost:9092"
    val consumerGroup = "kafka-consumer-demo-app-cg-1"
    val topic = "test2"

    val kafkaConsumerConfig = ConsumerHelper.getKafkaConfig(
        bootstrapServer,
        consumerGroup
    )
    val consumer =
        ConsumerHelper.createConsumer(kafkaConsumerConfig)


    // Assign and seek are mostly used to replay data or fetch a specific message

    // Assign
    val topicAndPartitionToReadFrom = TopicPartition(topic, 0)
    ConsumerHelper.assignPartition(consumer, listOf(topicAndPartitionToReadFrom))

    // Seek
    val offsetToReadFrom = 15L
    ConsumerHelper.seek(consumer, topicAndPartitionToReadFrom, offsetToReadFrom)

    val numOfMessagesToRead = 5
    ConsumerHelper.pollFixedNumberOfMessages(consumer, Duration.ofMillis(500), numOfMessagesToRead)

}


