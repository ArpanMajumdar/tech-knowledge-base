package com.arpan.spark.util

import org.apache.spark.SparkConf
import org.apache.spark.api.java.JavaSparkContext
import org.apache.spark.sql.SQLContext

object SparkUtils {

    fun getSparkContext(appName: String, masterUrl: String = "local[*]"): JavaSparkContext {
        val sparkConf = SparkConf().setAppName(appName).setMaster(masterUrl)
        return JavaSparkContext(sparkConf)
    }

    fun getSqlContext(sparkContext: JavaSparkContext) = SQLContext(sparkContext)
}