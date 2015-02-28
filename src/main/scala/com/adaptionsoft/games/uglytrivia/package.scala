package com.adaptionsoft.games

import scalaz.\/

/**
 * Created by arturas on 15.2.28.
 */
package object uglytrivia {
  implicit class EitherExts[A, B](val e: A \/ B) extends AnyVal {
    def right_! = e.fold(
      a => throw new NoSuchElementException(s"no right exists! left=$a"),
      identity
    )
  }
}
