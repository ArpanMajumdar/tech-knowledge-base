package com.arpan.kafka;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.KafkaStreams;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.StreamsConfig;
import org.apache.kafka.streams.kstream.KStream;
import org.apache.kafka.streams.kstream.KTable;
import org.apache.kafka.streams.kstream.Produced;

import java.util.Arrays;
import java.util.Properties;
import java.util.concurrent.CountDownLatch;

public class WordCountDemo {

  public static void main(String[] args) {
    //

    String inputTopic = "word-count-input";
    String outputTopic = "word-count-output";

    Properties kafkaProps = new Properties();
    kafkaProps.put(StreamsConfig.APPLICATION_ID_CONFIG, "wordcount-app");
    kafkaProps.put(StreamsConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
    kafkaProps.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");
    kafkaProps.put(StreamsConfig.DEFAULT_KEY_SERDE_CLASS_CONFIG, Serdes.String().getClass());
    kafkaProps.put(StreamsConfig.DEFAULT_VALUE_SERDE_CLASS_CONFIG, Serdes.String().getClass());

    StreamsBuilder builder = new StreamsBuilder();
    KStream<String, String> source = builder.stream(inputTopic);

    KTable<String, Long> wordCounts =
        source
            .flatMapValues(value -> Arrays.asList(value.toLowerCase().split("\\W+")))
            .groupBy((key, value) -> value)
            .count();

    wordCounts.toStream().to(outputTopic, Produced.with(Serdes.String(), Serdes.Long()));

    KafkaStreams kafkaStreams = new KafkaStreams(builder.build(), kafkaProps);
    CountDownLatch countDownLatch = new CountDownLatch(1);

    Runtime.getRuntime()
        .addShutdownHook(
            new Thread("streams-wordcount-shutdown-hook") {
              @Override
              public void run() {
                kafkaStreams.close();
                countDownLatch.countDown();
              }
            });

    try {
      kafkaStreams.start();
      countDownLatch.await();
    } catch (Throwable e) {
      System.out.println("Error occurred");
      System.exit(-1);
    }
    System.exit(0);
  }
}
