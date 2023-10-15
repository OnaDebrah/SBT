lazy val data = (project in file("modules/data"))
  .configs(IntegrationTest)
  .settings(Defaults.itSettings)
  .settings(
    organization := "com.star.bright.analytics",
    name := "data",
    version := "0.1",
    scalaVersion := "2.13.6",
    mainClass := Some("com.star.bright.analytics.Main.scala"),
    scalacOptions := Seq("-deprecation", "-feature")
  )
  .settings(libraryDependencies ++= Dependencies.starBrightAnalytics)

lazy val core = (project in file("modules/core"))
  .configs(IntegrationTest)
  .settings(Defaults.itSettings)
  .settings(
    organization := "com.star.bright.analytics",
    name := "core"
  )
  .settings(libraryDependencies ++= Dependencies.starBrightAnalytics)

lazy val strategies = (project in file("modules/strategies"))
  .configs(IntegrationTest)
  .settings(Defaults.itSettings)
  .settings(
    organization := "com.star.bright.analytics",
    name := "strategies"
  )
  .settings(libraryDependencies ++= Dependencies.starBrightAnalytics)

lazy val analytics = (project in file("modules/analytics"))
  .configs(IntegrationTest)
  .settings(Defaults.itSettings)
  .settings(
    organization := "com.star.bright.analytics",
    name := "analytics"
  )
  .settings(libraryDependencies ++= Dependencies.starBrightAnalytics)

lazy val trading = (project in file("modules/app"))
  .configs(IntegrationTest)
  .settings(Defaults.itSettings)
  .settings(
    organization := "com.star.bright.analytics",
    name := "trading"
  )
  .settings(libraryDependencies ++= Dependencies.starBrightAnalytics)

lazy val root = (project in file("."))
  .aggregate(core, data, strategies, analytics)
