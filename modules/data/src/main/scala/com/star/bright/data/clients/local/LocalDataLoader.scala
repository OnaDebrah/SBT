package com.star.bright.data.clients.local

import com.star.bright.data.reader.ClosingPrices
import org.apache.spark.sql.{DataFrame, Dataset, SparkSession}

object LocalDataLoader {

  def apply(spark: SparkSession, filePath: String): DataFrame = {
    import spark.implicits._

    def parseDataset(dataset: Dataset[String]): Dataset[ClosingPrices] = {
      val header = dataset.first
      dataset.filter(_(0) != header(0)).map(parseStock).cache()
    }
    parseDataset(spark.read.textFile(filePath)).toDF().cache()
  }

  private[this] def parseStock(str: String): ClosingPrices = {
    val line = str.split(",")
    ClosingPrices(
      line(0),
      line(1).toDouble,
      line(2).toDouble,
      line(3).toDouble,
      line(4).toDouble,
      line(5).toDouble,
      line(6).toDouble
    )
  }

}
