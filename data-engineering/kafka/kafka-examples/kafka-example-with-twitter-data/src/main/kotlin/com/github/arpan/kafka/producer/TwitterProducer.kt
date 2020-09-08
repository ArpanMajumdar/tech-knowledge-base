package com.github.arpan.kafka.producer

import com.github.arpan.kafka.client.TwitterAuth
import com.github.arpan.kafka.client.TwitterClient
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import org.slf4j.LoggerFactory
import java.util.concurrent.LinkedBlockingQueue
import java.util.concurrent.TimeUnit

fun main() = runBlocking {

    /**
     * Create `twitter-tweets` topic if not exists by running this command
     *
     * bin/kafka-topics.sh --create \
            --bootstrap-server localhost:9092 \
            --replication-factor 1 \
            --partitions 6 \
            --topic twitter-tweets
     */

    val logger = LoggerFactory.getLogger("com.github.arpan.kafka.producer.TwitterProducer")

    val consumerKey: String = System.getenv("CONSUMER_KEY")
        ?: throw IllegalArgumentException("CONSUMER_KEY is required.")
    val consumerSecret: String = System.getenv("CONSUMER_SECRET")
        ?: throw IllegalArgumentException("CONSUMER_SECRET is required.")
    val accessToken: String = System.getenv("ACCESS_TOKEN")
        ?: throw IllegalArgumentException("ACCESS_TOKEN is required.")
    val accessTokenSecret: String = System.getenv("ACCESS_TOKEN_SECRET")
        ?: throw IllegalArgumentException("ACCESS_TOKEN_SECRET is required.")

    val msgQueue = LinkedBlockingQueue<String>(1000)
    val twitterAuth = TwitterAuth(
        consumerKey,
        consumerSecret,
        accessToken,
        accessTokenSecret
    )
    val terms = listOf("corona")

    val bootstrapServer = "localhost:9092"
    val topic = "twitter-tweets"
    val producer = ProducerFactory.create(bootstrapServer)
    val timeout = 5L
    val timeUnit = TimeUnit.SECONDS

    val twitterClient = TwitterClient(msgQueue, twitterAuth, terms)
    launch {
        twitterClient.apply {
            connect()
            pollAndSendToKafka(producer, topic, timeout, timeUnit)
        }
    }

    Runtime.getRuntime().addShutdownHook(Thread {
        runBlocking {
            logger.info("Stopping application ...")
            logger.info("Stopping twitter client ...")
            twitterClient.stop()
            logger.info("Twitter client stopped.")
            logger.info("Closing producer ...")
            producer.close()
            logger.info("Producer closed.")
            logger.info("Application exited.")
        }
    })
}