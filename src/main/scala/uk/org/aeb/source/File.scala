package uk.org.aeb.source

import org.apache.spark.sql.{Dataset, SparkSession}
import uk.org.aeb.models.SourceConfig

object File {

  def read(config: SourceConfig)(implicit spark: SparkSession): Dataset[String] =
    spark.read.textFile(config.uri)

}
