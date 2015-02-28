package com.adaptionsoft.games

import java.io.ByteArrayOutputStream

import org.scalactic.TypeCheckedTripleEquals
import org.scalatest.{Matchers, FreeSpec}

/**
 * Created by arturas on 15.2.28.
 */
trait AppTest extends FreeSpec with Matchers
with TypeCheckedTripleEquals {
  type ReadConsole = () => String

  def captureOut[A](f: ReadConsole => A) = {
    val stream = new ByteArrayOutputStream()
    Console.withOut(stream) {
      f(() => stream.toString("UTF-8"))
    }
  }
}
