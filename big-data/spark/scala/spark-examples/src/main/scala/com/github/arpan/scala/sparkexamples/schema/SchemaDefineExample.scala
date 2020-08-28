package com.github.arpan.scala.sparkexamples.schema

import com.typesafe.scalalogging.LazyLogging
import org.apache.spark.sql.SparkSession
import org.apache.spark.sql.types._

object SchemaDefineExample extends LazyLogging {

  def main(args: Array[String]) {
    val sparkSession = SparkSession
      .builder()
      .master("local[*]")
      .appName("SchemaDefineExample")
      .getOrCreate()

    val jsonFilePath = "data/json/blogs.json"

    val schema = StructType(Array(
      StructField("Id", IntegerType, false),
      StructField("First", StringType, false),
      StructField("Last", StringType, false),
      StructField("Url", StringType, false),
      StructField("Published", StringType, false),
      StructField("Hits", IntegerType, false),
      StructField("Campaigns", ArrayType(StringType), false)
    ))

    val schemaDDLStr =
      """
        | Id INT,
        | First STRING,
        | Last STRING,
        | Url STRING,
        | Published STRING,
        | Hits INT,
        | Campaigns ARRAY<STRING>
        |""".stripMargin

    val blogsDF = sparkSession.read.schema(schema).json(jsonFilePath)
    val blogs2DF = sparkSession.read.schema(schemaDDLStr).json(jsonFilePath)

    logger.info("Blogs DF using programmatically derived schema")
    blogsDF.show()
    blogsDF.printSchema()
    logger.info(blogsDF.schema.toString())

    logger.info("Blogs2 DF using schema string")
    blogs2DF.show()
    blogs2DF.printSchema()
    logger.info(schemaDDLStr)
  }
}
