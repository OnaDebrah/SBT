package com.star.bright.strategies

import com.star.bright.data.core.reader.LocalFileReader
import com.star.bright.data.core.utils.SparkSuite
import org.scalatest.funspec.AnyFunSpec
import org.scalatest.matchers.should.Matchers

import java.io.File

class StatisticsGathererSpec extends AnyFunSpec with Matchers {
  private[this] val spark: SparkSuite = SparkSuite.sparkBuilder

  it("should read multiple Historical Data csv files") {
    val path: String = "src/test/resources/"
    val files: List[File] = LocalFileReader.getListOfFiles(path)
    val result: List[Statistics] = files.map { file =>
      val stats = StatisticsGatherer(spark.sparkSession, file)
      stats.yearlyAverage.show()
      stats.monthlyAverage.show()
      stats.openCloseDifference.show()
      stats
    }
    result.size shouldBe 2
  }

}
