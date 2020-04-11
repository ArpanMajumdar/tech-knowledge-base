package com.arpan.spark.setoperations


import com.arpan.spark.util.FileUtils
import com.arpan.spark.util.SparkUtils
import org.apache.spark.SparkConf
import org.apache.spark.api.java.JavaSparkContext
import org.slf4j.LoggerFactory

data class Log(
    val host: String,
    val logName: String,
    val time: Long,
    val method: String,
    val url: String,
    val responseCode: Int,
    val bytes: Int
)

fun main() {
    val logger = LoggerFactory.getLogger("LogUnion")

    val sparkContext = SparkUtils.getSparkContext("log-union")

    // Read logs as RDD
    val logs19950701 = sparkContext.textFile("input/nasa_19950701.tsv")
    val logs19950801 = sparkContext.textFile("input/nasa_19950801.tsv")

    // Perform union and take a 10% sample
    val aggregatedLogs = logs19950701.union(logs19950801)

    fun isNotHeader(log: String) = !(log.startsWith("host") && log.contains("bytes"))
    val cleanedLogs = aggregatedLogs.filter { log -> isNotHeader(log) }
    val sampledLogs = cleanedLogs.sample(false, 0.1)

    // Get hosts from logs
    fun String.toLog(): Log {
        val tokens = this.split("\\t".toRegex())
        return Log(
            host = tokens[0],
            logName = tokens[1],
            time = tokens[2].toLong(),
            method = tokens[3],
            url = tokens[4],
            responseCode = tokens[5].toInt(),
            bytes = tokens[6].toInt()
        )
    }

    val hostsFor19950701 = logs19950701
        .filter { logLine -> isNotHeader(logLine) }
        .map { logLine -> logLine.toLog() }
        .map { log -> log.host }

    val hostsFor19950801 = logs19950801
        .filter { logLine -> isNotHeader(logLine) }
        .map { logLine -> logLine.toLog() }
        .map { log -> log.host }

    // Perform intersection to get hosts accessed on both days
    val hostsAccessedOnBothDays = hostsFor19950701.intersection(hostsFor19950801)

    // Write sampled logs
    FileUtils.recursivelyDeleteDirectory("output/nasaSampledLogs")
    FileUtils.recursivelyDeleteDirectory("output/hostsAccessedOnBothDays")
    sampledLogs.saveAsTextFile("output/nasaSampledLogs")
    hostsAccessedOnBothDays.saveAsTextFile("output/hostsAccessedOnBothDays")
}