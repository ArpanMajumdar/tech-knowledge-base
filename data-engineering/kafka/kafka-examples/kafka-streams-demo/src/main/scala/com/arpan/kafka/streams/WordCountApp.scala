package com.arpan.kafka.streams

import java.util
import java.util.Properties
import java.util.concurrent.CountDownLatch

import org.apache.kafka.clients.consumer.ConsumerConfig
import org.apache.kafka.common.serialization.Serdes
import org.apache.kafka.streams.kstream.{KStream, KTable, Produced}
import org.apache.kafka.streams.{KafkaStreams, StreamsBuilder, StreamsConfig}
import org.apache.logging.log4j.scala.Logging

object WordCountApp extends App with Logging {

  val kafkaProps = {
    val props = new Properties
    props.put(StreamsConfig.APPLICATION_ID_CONFIG, "wordcount-app")
    props.put(StreamsConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092")
    props.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest")
    props
      .put(StreamsConfig.DEFAULT_KEY_SERDE_CLASS_CONFIG, Serdes.String().getClass.getCanonicalName)
    props.put(StreamsConfig.DEFAULT_VALUE_SERDE_CLASS_CONFIG,
              Serdes.String().getClass.getCanonicalName)
    props
  }

  val builder                            = new StreamsBuilder()
  val textLines: KStream[String, String] = builder.stream[String, String]("streams-plaintext-input")
  val wordCounts: KTable[String, java.lang.Long] = textLines
    .flatMapValues(textLine => util.Arrays.asList(textLine.toLowerCase.split("\\W+")))
    .groupBy((_, word) => word)
    .count()

  wordCounts.toStream().to("word-count-output", Produced.`with`(Serdes.String(), Serdes.Long()))

  val streams        = new KafkaStreams(builder.build(), kafkaProps)
  val countDownLatch = new CountDownLatch(1)

  Runtime.getRuntime.addShutdownHook(new Thread("streams-wordcount-countdown-hook") {
    override def run(): Unit = {
      streams.close()
      countDownLatch.countDown()
    }
  })

  try {
    streams.start()
    countDownLatch.await()
  } catch {
    case _: Throwable => println("Error occurred")
  }

}
