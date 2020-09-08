package com.tgt.kafka.streams

import java.util.Properties
import java.util.concurrent.{CountDownLatch, TimeUnit}

import org.apache.kafka.clients.consumer.ConsumerConfig
import org.apache.kafka.streams.scala.ImplicitConversions._
import org.apache.kafka.streams.{KafkaStreams, StreamsConfig}
import org.apache.kafka.streams.scala.StreamsBuilder
import org.apache.kafka.streams.scala.kstream.{KStream, KTable, Materialized}

object WordCountApp extends App {
  import org.apache.kafka.streams.scala.Serdes._

  val inputTopic = "word-count-input"
  val outputTopic = "word-count-output"

  // Define kafka properties
  val props: Properties = {
    val props = new Properties()
    props.put(StreamsConfig.APPLICATION_ID_CONFIG, "wordcount-app")
    props.put(StreamsConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092")
    props.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest")
    props
  }

  val builder: StreamsBuilder = new StreamsBuilder
  val textLines: KStream[String, String] =
    builder.stream[String, String](inputTopic)

  val wordCounts: KTable[String, Long] = textLines
    .flatMapValues(_.toLowerCase.split("\\W+"))
    .groupBy((_, word) => word)
    .count()
  wordCounts.toStream.to(outputTopic)

  val streams: KafkaStreams = new KafkaStreams(builder.build(), props)
  val countDownLatch = new CountDownLatch(1)

  // Add shutdown hook for graceful shutdown
  Runtime.getRuntime.addShutdownHook(
    new Thread("streams-wordcount-shutdown-hook") {
      override def run(): Unit = {
        streams.close()
        countDownLatch.countDown()
      }
    })

  // Start kafka streams
  try {
    streams.start()
    countDownLatch.await()
  } catch {
    case _: Throwable => System.exit(-1)
  }
  System.exit(0)
}
