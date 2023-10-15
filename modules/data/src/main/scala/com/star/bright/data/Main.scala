package com.star.bright.data

import com.star.bright.data.utils.SparkSuite
import org.slf4j.{Logger, LoggerFactory}

object Main {
  private val log: Logger = LoggerFactory.getLogger(this.getClass)

  val spark: SparkSuite = SparkSuite.sparkBuilder

  def main(args: Array[String]): Unit =
    log.info("Hello Apps")

}
