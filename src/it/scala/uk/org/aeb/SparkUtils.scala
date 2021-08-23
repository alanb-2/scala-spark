package uk.org.aeb

import org.apache.spark.sql.SparkSession

object SparkUtils {

  def createSparkSession(name: String): SparkSession =
    SparkSession
      .builder()
      .master("local[2]")
      .appName(name)
      .getOrCreate()

}
