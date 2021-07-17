package uk.org.aeb

import com.typesafe.config.ConfigFactory
import org.apache.log4j.Logger
import org.apache.spark.sql.SparkSession
import uk.org.aeb.models.{ApplicationConfig, DataConfig}
import uk.org.aeb.transformations.{Count, Split}
import uk.org.aeb.utils.Spark

object Main {

  val logger: Logger = Logger.getLogger(getClass.getName)

  def main(args: Array[String]): Unit = {

    val conf = ConfigFactory.load()
    val applicationConfig = ApplicationConfig(conf)

    implicit val spark: SparkSession = Spark.createSparkSession()

    job(applicationConfig.data)

    spark.stop()

  }

  def job(config: DataConfig)(implicit spark: SparkSession): Unit = {

    import spark.implicits._

    val raw = source.File.read(config.source).rdd

    val words = Split.execute(raw)

    val countPerWord = Count.execute(words).toDF("word", "count")

    sink.File.write(countPerWord, config.sink)

  }

}
