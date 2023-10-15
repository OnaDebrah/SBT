package com.star.bright.core.writer

import cats.effect.IO
import cats.effect.testing.scalatest.AsyncIOSpec
import org.scalatest.funspec.AsyncFunSpecLike
import org.scalatest.matchers.should.Matchers

import java.io.File
import java.nio.file.Paths

class FileWriterSpec extends AsyncFunSpecLike with AsyncIOSpec with Matchers {

  private val projectFolder = "/outputs"
  private val useTestFolder = true

  describe("The FileWriter") {

    ignore("should fail to write if incorrect path is given") {
      val invalidPath = "/output/json/not_valid/"
      val filename = "test.txt"
      val content = "Writing some random stuff"
      FileWriter.writeFile[IO](projectFolder, invalidPath, filename, content, useTestFolder).assertThrows[WriterError]
    }

    it("should write successfully if correct path is given") {
      val validPath = "/"
      val filename = "test.txt"
      val content = "Writing some random stuff"
      FileWriter
        .writeFile[IO](projectFolder, validPath, filename, content, useTestFolder)
        .assertNoException
        .andThen { _ =>
          // clean up file
          val path = Paths.get("").toAbsolutePath.toString + projectFolder + "/src/test/resources" + validPath
          val file = new File(path, filename)
          file.delete()
        }
    }
  }

}
