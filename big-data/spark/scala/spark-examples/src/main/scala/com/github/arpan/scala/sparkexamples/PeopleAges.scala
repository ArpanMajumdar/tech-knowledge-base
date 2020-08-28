package com.github.arpan.scala.sparkexamples

import com.typesafe.scalalogging.LazyLogging
import org.apache.spark.sql.SparkSession
import org.apache.spark.sql.functions._

object PeopleAges extends LazyLogging{

  def main(args: Array[String]): Unit = {
    val sparkSession = SparkSession
      .builder
      .appName("PeopleAges")
      .master("local[*]")
      .getOrCreate()

    val dataDF = sparkSession.createDataFrame(Seq(
      ("Brooke", 20),
      ("Brooke", 25),
      ("Denny", 31),
      ("Jules", 30),
      ("TD", 35))
    ).toDF("name", "age")

    val avgDf = dataDF.groupBy("name").agg(avg("age"))
    avgDf.show()
  }
}
