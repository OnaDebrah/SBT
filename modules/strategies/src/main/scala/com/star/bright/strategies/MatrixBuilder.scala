package com.star.bright.strategies

import com.star.bright.data.core.clients.local.LocalHistoricalDataLoader
import org.apache.spark.rdd.RDD
import org.apache.spark.sql.{DataFrame, Dataset, SparkSession}

import java.io.File

trait MatrixBuilder {
  def build(spark: SparkSession, files: List[File]): DataFrame
}

object MatrixBuilder extends MatrixBuilder {

  def apply(spark: SparkSession, files: List[File]): DataFrame = build(spark, files)

  override def build(spark: SparkSession, files: List[File]): DataFrame = {
    import spark.implicits._

    def getTicker(file: File): String =
      file.getName
        .replaceFirst("[.][^.]+$", "")
        .toLowerCase()
        .substring(0, 3)

    val tickerLabels: Seq[String] = files.map(getTicker)

    def makeQuery(tickers: Seq[String], benchmark: String = "spy"): String = {
      val base = tickers
        .map { ticker =>
          s"$ticker.adjClose as ${ticker}close"
        }
        .mkString(", ")

      val select = s"SELECT $benchmark.date, " + base + " " + "from" + " " + tickers
        .map(ticker => if (ticker != benchmark) s"$ticker join $benchmark on $ticker.date = $benchmark.date, ")
        .mkString
        .dropRight(2)
      select
    }

    // import spark.sqlContext.implicits._
    import spark.implicits._
    println(makeQuery(tickerLabels))

    val filesDF: DataFrame = spark.createDataset(files.map(_.toString)).toDF()
    filesDF.show()
    var ddf = spark.emptyDataFrame
    // Join all stock closing prices in order to  compare
    val df: DataFrame = {
      filesDF.rdd.map { file =>
        ddf.join(LocalHistoricalDataLoader(spark, file.toString()))
        ddf.createOrReplaceTempView(getTicker(new File(file.toString())))
      }
      ddf
    }

    ddf.show()

    val joinedClose: DataFrame = spark.sqlContext
      .sql(makeQuery(tickerLabels))
      .cache

    joinedClose.show
    joinedClose.createOrReplaceTempView("joinedClose")

    joinedClose
  }

}
