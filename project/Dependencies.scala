import Versions._
import sbt._

object Versions {
  lazy val pureConfig = "0.17.1"
  lazy val decline = "2.2.0"
  lazy val spark = "3.3.0"

  lazy val catsCore = "2.8.0"
  lazy val catsEffect = "3.3.11"

  // Test
  val catsEffectScalaTest = "1.4.0"
  lazy val scalaTest = "3.2.13"
  lazy val scalacheck = "1.17.0"
}

object Dependencies {

  val sparkDependencies: Seq[ModuleID] =
    Seq(
      "org.apache.spark" %% "spark-core" % spark,
      "org.apache.spark" %% "spark-sql" % spark,
      "org.apache.spark" %% "spark-mllib" % spark
    )

  val miscDependencies: Seq[ModuleID] = Seq(
    "com.github.pureconfig" %% "pureconfig" % pureConfig,
    "com.monovore" %% "decline" % decline,
    "org.typelevel" %% "cats-core" % catsCore,
    "org.typelevel" %% "cats-effect-kernel" % catsEffect
  )

  val testDependencies: Seq[ModuleID] =
    Seq(
      "org.scalatest" %% "scalatest" % scalaTest,
      "org.scalacheck" %% "scalacheck" % scalacheck,
      "org.typelevel" %% "cats-effect-testing-scalatest" % catsEffectScalaTest
    )
      .map(_ % "it,test")

  val starBrightAnalytics: Seq[ModuleID] =
    sparkDependencies ++ miscDependencies ++ testDependencies

}
