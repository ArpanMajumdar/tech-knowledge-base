package com.arpan.spark.wordcount

import com.arpan.spark.util.SparkUtils
import org.apache.spark.SparkConf
import org.apache.spark.api.java.JavaRDD
import org.apache.spark.api.java.JavaSparkContext
import org.slf4j.LoggerFactory

fun main() {

    val logger = LoggerFactory.getLogger("WordCount")

    val sparkContext = SparkUtils.getSparkContext("word-count")

    val lines: JavaRDD<String> = sparkContext.textFile("input/word_count.text", 4)
    val words = lines.flatMap { line ->
        line.split(" ").listIterator()
    }
    val wordCounts: Map<String, Long> = words.countByValue()

    wordCounts.forEach { (word, count) ->
        logger.info("$word => $count")
    }
}