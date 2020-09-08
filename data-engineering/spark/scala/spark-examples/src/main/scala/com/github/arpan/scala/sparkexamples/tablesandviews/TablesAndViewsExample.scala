package com.github.arpan.scala.sparkexamples.tablesandviews

import org.apache.spark.sql.SparkSession
import org.apache.spark.sql.types.{IntegerType, StringType, StructField, StructType}

object TablesAndViewsExample {
  def main(args: Array[String]): Unit = {
    val sparkSession = SparkSession
      .builder
      .appName("tables-and-views-example")
      .master("local[*]")
      .getOrCreate()

    sparkSession.sql("DROP DATABASE IF EXISTS learn_spark_db CASCADE")
    sparkSession.sql("CREATE DATABASE learn_spark_db")
    sparkSession.sql("USE learn_spark_db")

    val csvFilePath = "data/csv/departuredelays.csv"
    val schema = StructType(
      Array(
        StructField("date", StringType, false),
        StructField("delay", IntegerType, false),
        StructField("distance", IntegerType, false),
        StructField("origin", StringType, false),
        StructField("destination", StringType, false)
      )
    )

    // Creating a managed table
    val flightsDf = sparkSession
      .read
      .schema(schema)
      .csv(csvFilePath)

    flightsDf.write.saveAsTable("managed_us_delay_flights_tbl")

    // Creating an unmanaged table
    flightsDf.write.option("path", "tmp/data/us_flights_delay").saveAsTable("us_delay_flights_tbl")
  }
}
