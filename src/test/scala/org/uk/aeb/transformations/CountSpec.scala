package org.uk.aeb.transformations

import org.apache.spark.sql.SparkSession
import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should
import org.uk.aeb.SparkUtils

@SuppressWarnings(Array("org.wartremover.warts.NonUnitStatements"))
class CountSpec extends AnyFlatSpec with should.Matchers {

  val DefaultSparkSession: SparkSession = SparkUtils.createSparkSession("count-test")

  it should "count the number of words in an RDD" in {

    val input = Seq("how", "much", "wood", "would", "a", "wood", "chuck", "chuck", "if", "a", "wood", "chuck", "could", "chuck", "wood")

    val rdd = DefaultSparkSession.sparkContext.parallelize(input)

    val output = Count.execute(rdd)

    output.count shouldEqual 8
    output.collect.sorted shouldEqual Array(("a",2), ("chuck",4), ("could",1), ("how",1), ("if",1), ("much",1), ("wood",4), ("would",1))

  }

}
