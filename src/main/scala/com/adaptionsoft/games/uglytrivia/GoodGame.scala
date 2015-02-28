package com.adaptionsoft.games.uglytrivia

import scalaz._
import scalaz.effect.IO
import scalaz.syntax.writer._
import scalaz.syntax.either._


case class Player(name: String)
case class PlayerState(
  player: Player, place: Int, purse: Int, inPenaltyBox: Boolean
)

object GoodGame {
  type Logged = Writer[Vector[String], GoodGame]

  def apply(
    questions: Questions, p1: Player, p2: Player
  ): Logged =
    fromList(questions, p1, p2).right_!
  def apply(
    questions: Questions, p1: Player, p2: Player, p3: Player
  ): Logged =
    fromList(questions, p1, p2, p3).right_!
  def apply(
    questions: Questions,
    p1: Player, p2: Player, p3: Player, p4: Player
  ): Logged =
    fromList(questions, p1, p2, p3, p4).right_!
  def apply(
    questions: Questions,
    p1: Player, p2: Player, p3: Player, p4: Player, p5: Player
  ): Logged =
    fromList(questions, p1, p2, p3, p4, p5).right_!
  def apply(
    questions: Questions,
    p1: Player, p2: Player, p3: Player, p4: Player, p5: Player, p6: Player
  ): Logged =
    fromList(questions, p1, p2, p3, p4, p5, p6).right_!

  def fromList(
    questions: Questions,
    p1: Player, p2: Player, rest: Player*
  ): String \/ Logged = {
    if (rest.size > 4)
      s"${rest.size + 2} players given, but only 6 players can be accepted".left
    else {
      val players = (Vector(p1, p2) ++ rest).map(PlayerState(_, 0, 0, false))
      val playersW = players.zipWithIndex.foldLeft(
        players.set(Vector.empty[String])
      ) { case (writer, (state, idx)) =>
        writer.flatMap { _ => players.set(Vector(
          s"${state.player.name} was added",
          s"They are player number ${idx + 1}"
        )) }
      }
      playersW.map(GoodGame(_, questions)).right
    }
  }
}

object Questions {
  def generate(howMany: Int = 50) = {
    def question(category: String)(idx: Int) = s"$category Question $idx"

    val popQuestion = question("Pop")
    val scienceQuestion = question("Science")
    val sportsQuestion = question("Sports")
    val rockQuestion = question("Rock")
    Questions(
      pop = Vector.tabulate(howMany)(popQuestion),
      science = Vector.tabulate(howMany)(scienceQuestion),
      sports = Vector.tabulate(howMany)(sportsQuestion),
      rock = Vector.tabulate(howMany)(rockQuestion)
    )
  }
}
case class Questions(
  pop: Vector[String], science: Vector[String], sports: Vector[String],
  rock: Vector[String]
)

case class GoodGame private (
  // Guaranteed to have 2 to 6 players.
  players: Vector[PlayerState],
  questions: Questions,
  currentPlayer: Int = 0,
  isGettingOutOfPenaltyBox: Boolean = false
) {

}
