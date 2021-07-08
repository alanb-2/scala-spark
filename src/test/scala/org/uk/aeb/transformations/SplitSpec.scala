package org.uk.aeb.transformations

import org.apache.spark.sql.SparkSession
import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should
import org.uk.aeb.SparkUtils

@SuppressWarnings(Array("org.wartremover.warts.NonUnitStatements"))
class SplitSpec extends AnyFlatSpec with should.Matchers {

  val DefaultSparkSession: SparkSession = SparkUtils.createSparkSession("split-test")

  it should "split lines by spaces" in {

    val input = Seq("how much wood would a wood chuck", "chuck if a wood", "chuck could chuck wood")

    val rdd = DefaultSparkSession.sparkContext.parallelize(input)

    val output = Split.execute(rdd)

    output.count shouldBe 15
    output.collect.sorted shouldEqual Array("a", "a", "chuck", "chuck", "chuck", "chuck", "could", "how", "if", "much", "wood", "wood", "wood", "wood", "would")

  }

}
