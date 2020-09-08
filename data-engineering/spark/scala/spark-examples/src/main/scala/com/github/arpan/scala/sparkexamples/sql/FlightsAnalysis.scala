package com.github.arpan.scala.sparkexamples.sql

import com.typesafe.scalalogging.LazyLogging
import org.apache.log4j.{Level, Logger}
import org.apache.spark.sql.SparkSession
import org.apache.spark.sql.functions._

object FlightsAnalysis extends LazyLogging {

  def main(args: Array[String]): Unit = {
    Logger.getLogger("org.sparkproject").setLevel(Level.OFF)
    Logger.getLogger("akka").setLevel(Level.OFF)

    val sparkSession = SparkSession
      .builder
      .appName("flights-analysis")
      .master("local[*]")
      .getOrCreate()

    sparkSession.sparkContext.setLogLevel("ERROR")

    val flightsDataPath = "data/csv/departuredelays.csv"

    val flightsDf = sparkSession
      .read
      .format("csv")
      .option("inferSchema", "true")
      .option("header", "true")
      .load(flightsDataPath)

    flightsDf.createOrReplaceTempView("us_delay_flights_tbl")

    logger.info("Flights between SFO and ORD with delay more than 120 min using SQL")
    sparkSession.sql(
      """
        | SELECT date, delay, origin, destination
        | FROM us_delay_flights_tbl
        | WHERE delay > 120
        | AND origin = 'SFO'
        | AND destination = 'ORD'
        | ORDER BY delay DESC
        |""".stripMargin).show(10)

    logger.info("Flights between SFO and ORD with delay more than 120 min using Dataframe API")
    val flightsBwSfoOrd = flightsDf
      .select("date", "delay", "origin", "destination")
      .where(
        (col("delay") > 120)
          .and(col("origin") === "SFO")
          .and(col("destination") === "ORD"))
      .orderBy(desc("delay"))
    flightsBwSfoOrd.show(10)


    logger.info("Flight delays overview")
    sparkSession.sql(
      """
        | SELECT delay, origin, destination,
        | CASE
        |   WHEN delay > 360 THEN 'Very Long Delays'
        |   WHEN delay > 120 AND delay < 160 THEN 'Long Delays'
        |   WHEN delay > 60 AND delay < 120 THEN 'Short Delays'
        |   WHEN delay > 0 AND delay < 60 THEN 'Tolerable Delays'
        |   WHEN delay = 0 THEN 'No Delays'
        |   ELSE 'Early' END as flight_delays
        | FROM us_delay_flights_tbl
        | ORDER BY origin, delay DESC
        |""".stripMargin
    ).show(10)
  }
}
