package com.github.arpan.kafka.consumer

import com.github.arpan.kafka.client.ElasticSearchAuth
import com.github.arpan.kafka.client.TwitterElasticSearchClient
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import org.apache.kafka.clients.consumer.ConsumerRecords
import org.apache.kafka.common.errors.WakeupException
import org.slf4j.LoggerFactory
import java.time.Duration
import kotlin.math.log

fun main() = runBlocking {
    /**
     * If while running, you receive the following error:
     * Limit of total fields [1000] in index [twitter] has been exceeded
     *
     * To solve this increase the index size to a large value (like 10000)
     *
     * curl --location --request PUT 'localhost:9200/twitter/_settings' \
    --header 'Content-Type: application/json' \
    --data-raw '{
    "index.mapping.total_fields.limit": 10000
    }'
     */

    val logger = LoggerFactory.getLogger("com.github.arpan.kafka.consumer.TwitterConsumer")

    // ElasticSearch details
    val esHostName = "localhost"
    val index = "twitter"
    val esClient = TwitterElasticSearchClient(ElasticSearchAuth(hostName = esHostName))

    // Kafka details
    val bootstrapServer = "localhost:9092" 
    val topic = "twitter-tweets"
    val kafkaConsumerHelper = KafkaConsumerHelper(bootstrapServer, topic)
    val consumer = kafkaConsumerHelper.consumer

    // Subscribe to topics
    consumer.subscribe(listOf(topic))

    // Poll for tweets and dump them to ES
    val job = launch(Dispatchers.Default) {
        try {
            while (true){
                val records: ConsumerRecords<String, String> = consumer.poll(Duration.ofMillis(500))
                if (!records.isEmpty) {
                    logger.info("Received ${records.count()} messages")
                    esClient.dumpKafkaRecordsToEs(index, records)

                    // Commit offsets after this batch is dumped to ES
                    logger.info("Committing offsets ...")
                    consumer.commitSync()
                    logger.info("Offsets have been committed.")
                }
            }
        } catch (exception: WakeupException) {
            logger.info("Received shutdown signal")
            logger.info("Exiting application ...")
            consumer.close()
            logger.info("Application has exited")
        }
    }

    // Close kafka consumer and ES client for graceful shutdown
    Runtime.getRuntime().addShutdownHook(Thread {
        runBlocking {
            logger.info("Caught shutdown hook")
            consumer.wakeup()
            job.join()

            logger.info("Closing ES client ...")
            esClient.close()
            logger.info("ES client closed.")
        }
    })

    job.join()
}