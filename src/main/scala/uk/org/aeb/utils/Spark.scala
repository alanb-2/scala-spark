package uk.org.aeb.utils

import org.apache.spark.sql.SparkSession

object Spark {

  def createSparkSession(): SparkSession =
    SparkSession
      .builder()
      .getOrCreate()

}
