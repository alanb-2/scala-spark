package org.uk.aeb.transformations

import org.apache.spark.rdd.RDD

object Count {

  def execute(rdd: RDD[String]): RDD[(String, Long)] =
    rdd
      .map(word => (word, 1L))
      .reduceByKey((a, b) => a + b)

}
