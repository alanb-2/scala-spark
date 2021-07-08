package org.uk.aeb.models

import com.typesafe.config.{ConfigException, ConfigFactory}
import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should

import java.io.File

class ApplicationConfigSpec extends AnyFlatSpec with should.Matchers {

  val TestRootDirectory: String = System.getProperty("user.dir")

  it should "parse a valid conf file" in {

    val config = ConfigFactory.parseFile(new File(s"$TestRootDirectory/src/test/resources/config/valid.conf"))

    val expectedApplicationConfig = ApplicationConfig(
        DataConfig(
            SourceConfig(
              uri = "s3a://my-bucket/word-count/input"
            ),
          SinkConfig(
            format = "csv",
            options = Map("mergeSchema" -> "true"),
            uri = "s3a://my-bucket/word-count/output"
          )
        )
    )

    ApplicationConfig(config) shouldBe expectedApplicationConfig

  }

  it should "throw an exception for a conf file with invalid type" in {

    val config = ConfigFactory.parseFile(new File(s"$TestRootDirectory/src/test/resources/config/invalid-type.conf"))

    val thrown = intercept[ConfigException] {
      ApplicationConfig(config)
    }

    thrown.getMessage should include("5: data.sink.format has type LIST rather than STRING")

  }

  it should "throw an exception for a conf file with missing configuration" in {

    val config = ConfigFactory.parseFile(new File(s"$TestRootDirectory/src/test/resources/config/invalid-missing.conf"))

    val thrown = intercept[ConfigException] {
      ApplicationConfig(config)
    }

    thrown.getMessage should include("4: No configuration setting found for key 'data.sink.format'")

  }

}
