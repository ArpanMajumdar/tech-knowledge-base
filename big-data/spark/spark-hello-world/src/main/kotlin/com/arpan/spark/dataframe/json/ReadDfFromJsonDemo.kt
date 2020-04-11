package com.arpan.spark.dataframe.json

import com.arpan.spark.util.SparkUtils
import org.apache.spark.sql.functions.*
import org.apache.spark.sql.types.DataTypes
import org.slf4j.LoggerFactory


fun main() {
    val logger = LoggerFactory.getLogger("DataframeDemo")

    // Create spark context
    val sparkContext = SparkUtils.getSparkContext("dataframe-demo")
    val sqlContext = SparkUtils.getSqlContext(sparkContext)

    val inputFilePath = "input/flight-data/json/2015-summary.json"
    val dfWithInferredSchema = sqlContext.read().format("json").load(inputFilePath)

    logger.info("Printing inferred schema for flight data")
    dfWithInferredSchema.printSchema()

    // User defined schema
    val userDefinedSchema = DataTypes.createStructType(
        arrayOf(
            DataTypes.createStructField("ORIGIN_COUNTRY_NAME", DataTypes.StringType,true),
            DataTypes.createStructField("DEST_COUNTRY_NAME", DataTypes.StringType, true),
            DataTypes.createStructField("count", DataTypes.LongType, true)
        )
    )

    logger.info("Printing user defined schema for flight data")
    val dfWithUserDefinedSchema = sqlContext.read().format("json").schema(userDefinedSchema).load(inputFilePath)
    dfWithUserDefinedSchema.printSchema()

    dfWithUserDefinedSchema.select(col("count")).show()

    logger.info("Showing first record")
    dfWithUserDefinedSchema.show(1)

    // Literals
    logger.info("Dataframe with an added column of constants")
    dfWithUserDefinedSchema.select(expr("*"), lit(1).alias("Ones")).show()

    // Getting unique rows
    logger.info("Distinct origin and destination countries")
    dfWithUserDefinedSchema.select("ORIGIN_COUNTRY_NAME", "DEST_COUNTRY_NAME").distinct().show()
}