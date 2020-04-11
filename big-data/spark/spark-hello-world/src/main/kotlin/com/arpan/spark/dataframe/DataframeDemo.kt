package com.arpan.spark.dataframe

import com.arpan.spark.util.SparkUtils
import org.apache.spark.sql.RowFactory
import org.apache.spark.sql.types.DataTypes
import org.slf4j.LoggerFactory

fun main() {
    val logger = LoggerFactory.getLogger("DataframeDemo")

    // Create spark context
    val sparkContext = SparkUtils.getSparkContext("dataframe-demo")
    val sqlContext = SparkUtils.getSqlContext(sparkContext)

    // Create a DF
    val numbers = (1L..500L).toList()
    val rowRdd = sparkContext.parallelize(numbers).map { number -> RowFactory.create(number) }
    val schema = DataTypes.createStructType(listOf(DataTypes.createStructField("number", DataTypes.LongType, false)))
    val numbersDf1 = sqlContext.createDataFrame(rowRdd, schema).toDF()
//    numbersDf1.show()

    // Another way of creting DF
    val numbersDf2 = sqlContext.range(500L).toDF()
    numbersDf2.show()
}