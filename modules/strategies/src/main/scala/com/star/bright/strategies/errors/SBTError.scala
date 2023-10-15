package com.star.bright.strategies.errors

sealed abstract class SBTError(reason: String, cause: Throwable) extends Throwable(reason, cause) {
  def this(reason: String) = this(reason, None.orNull)
}

object SBTError {

  final case class ReaderError(reason: Throwable) extends SBTError(s"Can't do the read: ${reason.getMessage}", reason)

  final case class WriterError(reason: Throwable) extends SBTError(s"Can't do the write: ${reason.getMessage}", reason)

  final case class ParserError(reason: Throwable) extends SBTError(s"Can't do the parse: ${reason.getMessage}", reason)

}
