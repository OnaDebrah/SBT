package com.star.bright.core.writer

import cats.effect.kernel.{Resource, Sync}
import cats.implicits._

import java.io.{BufferedWriter, File, PrintWriter}
import java.nio.file.Paths

object FileWriter {

  def writeFile[F[_]: Sync](
    projectFolder: String,
    outputPath: String,
    filename: String,
    content: String,
    useTestFolder: Boolean = false
  ): F[Unit] =
    (for {
      path <- (getResourcesPath(projectFolder, useTestFolder) + outputPath).pure[F]
      file <- Sync[F]
        .blocking {
          val file = new File(path, filename)
          if (!file.getParentFile.exists()) {
            file.getParentFile.mkdirs()
          }
          file
        }
      acquire = Sync[F].blocking(new BufferedWriter(new PrintWriter(file)))
      _ <- Resource
        .fromAutoCloseable[F, BufferedWriter](acquire)
        .use(_.write(content).pure[F])
    } yield ()).adaptError(err => WriterError(err))

  private[this] def getResourcesPath(projectFolder: String, useTestFolder: Boolean): String = {
    val absolutePath = Paths.get("").toAbsolutePath.toString
    if (useTestFolder) absolutePath + projectFolder + "/src/test/resources"
    else absolutePath + projectFolder + "/src/main/resources"
  }

}
