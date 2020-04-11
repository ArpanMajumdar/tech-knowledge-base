package com.github.arpan.kafka.consumer.simple

import com.github.arpan.kafka.consumer.helper.ConsumerHelper
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
    ConsumerHelper.subscribeToTopics(consumer, topic)

    ConsumerHelper.pollIndefinitely(consumer, Duration.ofMillis(500))
}
