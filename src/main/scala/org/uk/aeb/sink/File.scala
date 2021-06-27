package org.uk.aeb.sink

import org.apache.spark.sql.DataFrame
import org.uk.aeb.models.SinkConfig

object File {

  def write(dataFrame: DataFrame, config: SinkConfig): Unit =
    dataFrame.write
      .options(config.options)
      .format(config.format)
      .save(config.uri)

}
