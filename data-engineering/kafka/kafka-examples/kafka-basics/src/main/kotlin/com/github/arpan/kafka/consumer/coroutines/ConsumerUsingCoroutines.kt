package com.github.arpan.kafka.consumer.coroutines

import com.github.arpan.kafka.consumer.helper.ConsumerHelper
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import org.apache.kafka.common.errors.WakeupException
import org.slf4j.LoggerFactory
import java.time.Duration

fun main() = runBlocking {
    val logger = LoggerFactory.getLogger("ConsumerUsingCoroutines")

    // Consumer config
    val bootstrapServer = "localhost:9092"
    val consumerGroup = "kafka-consumer-demo-app-cg-1"
    val topic = "test2"

    val kafkaConsumerConfig = ConsumerHelper.getKafkaConfig(
        bootstrapServer,
        consumerGroup
    )
    val consumer = ConsumerHelper.createConsumer(kafkaConsumerConfig)
    ConsumerHelper.subscribeToTopics(consumer, topic)

    val job = launch(Dispatchers.Default) {
        try {
            ConsumerHelper.pollIndefinitely(consumer, Duration.ofMillis(500))
        } catch (exception: WakeupException) {
            logger.info("Received shutdown signal")
        } finally {
            logger.info("Exiting application ...")
            consumer.close()
            logger.info("Application has exited")
        }
    }

    Runtime.getRuntime().addShutdownHook(Thread {
        runBlocking {
            logger.info("Caught shutdown hook")
            consumer.wakeup()
            job.join()
        }
    })
}