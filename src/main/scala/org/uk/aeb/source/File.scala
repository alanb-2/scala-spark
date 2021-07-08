package org.uk.aeb.source

import org.apache.spark.sql.{Dataset, SparkSession}
import org.uk.aeb.models.SourceConfig

object File {

  def read(config: SourceConfig)(implicit spark: SparkSession): Dataset[String] =
    spark.read.textFile(config.uri)

}
