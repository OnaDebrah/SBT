package com.star.bright.core.utils

import org.apache.spark.sql.SparkSession
import org.apache.spark.streaming.{Seconds, StreamingContext}

case class SparkSuite(sparkSession: SparkSession, streamingContext: StreamingContext)

object SparkSuite {

  def sparkBuilder: SparkSuite = {
    val builder = SparkSession
      .builder()
      .master("local[*]")
      .appName("Star Bright Analytics")
      .getOrCreate()

    val ssc: StreamingContext = new StreamingContext(builder.sparkContext, Seconds(1))
    SparkSuite(builder, ssc)
  }

}
