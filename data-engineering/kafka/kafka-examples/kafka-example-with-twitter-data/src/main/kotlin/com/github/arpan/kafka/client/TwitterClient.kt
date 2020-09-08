package com.github.arpan.kafka.client

import com.github.arpan.kafka.producer.ProducerHelper
import com.twitter.hbc.ClientBuilder
import com.twitter.hbc.core.Constants
import com.twitter.hbc.core.HttpHosts
import com.twitter.hbc.core.endpoint.StatusesFilterEndpoint
import com.twitter.hbc.core.processor.StringDelimitedProcessor
import com.twitter.hbc.httpclient.auth.OAuth1
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonConfiguration
import org.apache.kafka.clients.producer.KafkaProducer
import org.slf4j.LoggerFactory
import java.util.concurrent.BlockingQueue
import java.util.concurrent.TimeUnit


data class TwitterAuth(
        val consumerKey: String,
        val consumerSecret: String,
        val accessToken: String,
        val accessTokenSecret: String
)

class TwitterClient(
        private val msgQueue: BlockingQueue<String>,
        twitterAuth: TwitterAuth,
        private val terms: List<String>
) {
    private val logger = LoggerFactory.getLogger(TwitterClient::class.java)
    private val json = Json(JsonConfiguration.Stable)

    private val hosebirdHosts = HttpHosts(Constants.STREAM_HOST)
    private val hosebirdAuth = OAuth1(
            twitterAuth.consumerKey,
            twitterAuth.consumerSecret,
            twitterAuth.accessToken,
            twitterAuth.accessTokenSecret
    )
    private val hosebirdEndpoint = StatusesFilterEndpoint().apply {
        trackTerms(terms)
    }
    private val client = ClientBuilder()
            .name("Hosebird-Client-01") // optional: mainly for the logs
            .hosts(hosebirdHosts)
            .authentication(hosebirdAuth)
            .endpoint(hosebirdEndpoint)
            .processor(StringDelimitedProcessor(msgQueue))
            .build()

    fun connect() = client.connect()

    fun isDone() = client.isDone

    fun stop() = client.stop()

    fun pollAndSendToKafka(
            producer: KafkaProducer<String, String>, topic: String, timeout: Long, timeUnit:
            TimeUnit
    ) {
        while (!client.isDone) {
            val message = try {
                msgQueue.poll(timeout, timeUnit)
            } catch (exception: InterruptedException) {
                logger.info("Client interrupted. ", exception)
                client.stop()
                ""
            }

            if (message.isNotBlank()) {
                val key = extractKey(message)
                ProducerHelper.sendRecord(
                        producer = producer,
                        topic = topic,
                        key = key,
                        message = message
                )
            }
        }
    }

    private fun extractKey(message: String): String {
        return json.parseJson(message).jsonObject["id"].toString()
    }
}