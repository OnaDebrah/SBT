package com.star.bright.core.reader

import cats.effect.kernel.{Resource, Sync}
import cats.implicits._

import java.io.{File, InputStream}
import scala.io.Source

case class ClosingPrices(
  date: String,
  open: Double,
  high: Double,
  low: Double,
  close: Double,
  adjClose: Double,
  volume: Double
)

object LocalFileReader {

  def readFile[F[_]: Sync](inputPath: String): F[String] =
    Resource
      .make[F, Source](getSource(inputPath))(closeSource(_))
      .use(readLines(_))
      .adaptError(err => ReaderError(err))

  private def getSource[F[_]: Sync](inputPath: String): F[Source] =
    for {
      path <- cleanPath(inputPath).pure[F]
      source <- Sync[F].blocking(Source.fromResource(path))
    } yield source

  private def closeSource[F[_]: Sync](source: Source): F[Unit] =
    Sync[F].blocking(source.close())

  private def readLines[F[_]: Sync](source: Source): F[String] =
    Sync[F].blocking(source.getLines().mkString("\n"))

  def readFileAsInputStream[F[_]: Sync](inputPath: String): F[Resource[F, InputStream]] =
    (for {
      ip <- cleanPath(inputPath).pure[F]
      acquire = Sync[F].blocking(getClass.getClassLoader.getResourceAsStream(ip))
      out = Resource.fromAutoCloseable[F, InputStream](acquire)
    } yield out).adaptError(err => ReaderError(err))

  // ensure to drop the 1st slash if it exists
  private[this] def cleanPath(p: String): String = if (p.headOption.contains('/')) p.drop(1) else p

  def getListOfFiles(directory: String): List[File] = {
    val file: File = new File(directory)
    if (file.exists && file.isDirectory) {
      file.listFiles.filter(_.isFile).toList
    } else {
      List[File]()
    }
  }

}
