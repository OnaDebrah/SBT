package com.star.bright.strategies

import com.star.bright.data.core.reader.LocalFileReader
import com.star.bright.data.core.utils.SparkSuite
import org.scalatest.funspec.AnyFunSpec
import org.scalatest.matchers.should.Matchers

import java.io.File

class MatrixBuilderSpec extends AnyFunSpec with Matchers {
  private[this] val spark: SparkSuite = SparkSuite.sparkBuilder

  it("should read multiple Historical Data csv files") {
    val path: String = "src/test/resources/"
    val files: List[File] = LocalFileReader.getListOfFiles(path)
    val matrixBuilderDF = MatrixBuilder(spark.sparkSession, files)
    matrixBuilderDF.show()
  }

}
