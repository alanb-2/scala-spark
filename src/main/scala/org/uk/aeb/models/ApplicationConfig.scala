package org.uk.aeb.models

import com.typesafe.config.{Config, ConfigObject, ConfigValue}

import java.util.Map.Entry

import scala.collection.JavaConverters._

case class SourceConfig(uri: String)

case class SinkConfig(
    uri: String,
    options: Map[String, String],
    format: String
)

case class DataConfig(source: SourceConfig, sink: SinkConfig)

case class ApplicationConfig(data: DataConfig)

object ApplicationConfig {

  private def configObjectsToMap(configObjects: Seq[ConfigObject]): Map[String, String] = {
    for {
      item: ConfigObject <- configObjects
      entry: Entry[String, ConfigValue] <- item.entrySet().asScala
      key = entry.getKey
      value = entry.getValue.unwrapped().toString
    } yield (key, value)
  }.toMap

  def apply(conf: Config): ApplicationConfig = {

    val sourceConfig = SourceConfig(
        uri = conf.getString("data.source.uri")
    )

    val sinkConfig = SinkConfig(
        uri = conf.getString("data.sink.uri"),
        options = configObjectsToMap(conf.getObjectList("data.sink.options").asScala),
        format = conf.getString("format")
    )

    val data = DataConfig(
        source = sourceConfig,
        sink = sinkConfig
    )

    ApplicationConfig(data)

  }

}
