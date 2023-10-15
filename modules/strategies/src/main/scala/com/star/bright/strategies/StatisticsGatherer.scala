package com.star.bright.strategies

import com.star.bright.data.core.clients.local.LocalHistoricalDataLoader
import org.apache.spark.sql.functions.{avg, desc, month, year}
import org.apache.spark.sql.{DataFrame, Dataset, Row, SparkSession}

import java.io.File

case class Statistics(yearlyAverage: Dataset[Row], monthlyAverage: DataFrame, openCloseDifference: DataFrame)

trait StatisticsGatherer {
  def stats(spark: SparkSession, df: DataFrame, file: File): Statistics
}

object StatisticsGatherer extends StatisticsGatherer {

  def apply(spark: SparkSession, file: File): Statistics = {
    val historicalDataDF = LocalHistoricalDataLoader(spark, file.toString)
    stats(spark, historicalDataDF, file)
  }

  override def stats(spark: SparkSession, df: DataFrame, file: File): Statistics = {
    import spark.implicits._

    def yearlyAverages(df: DataFrame): Dataset[Row] =
      df.select(
        year($"date")
          .alias("yr"),
        $"adjClose"
      ).groupBy("yr")
        .avg("adjClose")
        .orderBy(desc("yr"))

    def monthlyAverages(df: DataFrame): Dataset[Row] =
      df.select(year($"date").alias("yr"), month($"date").alias("mo"), $"adjClose")
        .groupBy("yr", "mo")
        .agg(avg("adjClose"))
        .orderBy(desc("yr"), desc("mo"))

    val fileName = file.getName
      .replaceFirst("[.][^.]+$", "")
      .toLowerCase()
      .substring(0, 3)

    df.createOrReplaceTempView(fileName)

    def openCloseDifferences(spark: SparkSession): DataFrame = {

      val query = s"""SELECT 
        |$fileName.date, 
        |$fileName.open, $fileName.close, 
        |abs($fileName.close - $fileName.open) as 
        |${fileName}dif FROM $fileName WHERE abs($fileName.close - $fileName.open) > 4 
        |""".stripMargin
      spark.sqlContext.sql(query)
    }

    Statistics(yearlyAverages(df), monthlyAverages(df), openCloseDifferences(spark))
  }

}
