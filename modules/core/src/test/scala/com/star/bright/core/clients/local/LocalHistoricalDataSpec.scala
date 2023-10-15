package com.star.bright.core.clients.local

import org.scalatest.funspec.AnyFunSpec
import org.scalatest.matchers.should.Matchers

class LocalHistoricalDataSpec extends AnyFunSpec with Matchers {
  private[this] val spark: SparkSuite = SparkSuite.sparkBuilder

  describe("HistoricalData") {
    it("should read multiple Historical Data csv files") {
      val path: String = "src/test/resources/"
      val files = LocalFileReader.getListOfFiles(path)
      val result = files.map(file => LocalDataLoader(spark.sparkSession, file.toString))
      result.head.show()
      result.size shouldBe 2
    }
    it("should read single Historical Data csv files") {
      val path: String = "src/test/resources/"
      val file = LocalFileReader.getListOfFiles(path).head
      val resultDF = LocalDataLoader(spark.sparkSession, file.toString)
      resultDF.count() shouldBe 1462
    }
  }

}
