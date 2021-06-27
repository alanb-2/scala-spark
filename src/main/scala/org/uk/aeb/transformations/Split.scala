package org.uk.aeb.transformations

import org.apache.spark.rdd.RDD

object Split {

  def execute(rdd: RDD[String]): RDD[String] = rdd.flatMap(line => line.split(" "))

}
