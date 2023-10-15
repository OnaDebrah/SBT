package com.star.bright.core.reader

import cats.effect.IO
import cats.effect.testing.scalatest.AsyncIOSpec
import org.scalatest.funspec.AsyncFunSpecLike
import org.scalatest.matchers.should.Matchers

class LocalFileReaderSpec extends AsyncFunSpecLike with AsyncIOSpec with Matchers {

  describe("The LocalFileReader") {
    it("should fail to read if incorrect path is given") {
      val invalidPath = "src/test/resources/ETH-USD.csv"
      LocalFileReader.readFile[IO](invalidPath).assertThrows[ReaderError]
    }

    it("should read successfully if correct path is given") {
      val validPath = "/resources/"
      println(LocalFileReader.readFile[IO](validPath).toString())
      LocalFileReader.readFile[IO](validPath).assertNoException
    }
  }

}
