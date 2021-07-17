package uk.org.aeb.sink

import org.apache.spark.sql.DataFrame
import uk.org.aeb.models.SinkConfig

object File {

  def write(dataFrame: DataFrame, config: SinkConfig): Unit =
    dataFrame.write
      .options(config.options)
      .format(config.format)
      .save(config.uri)

}
