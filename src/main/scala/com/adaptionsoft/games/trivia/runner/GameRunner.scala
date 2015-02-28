package com.adaptionsoft.games.trivia.runner

import com.adaptionsoft.games.uglytrivia.Game
import java.util.Random


object GameRunner {
  def main(args: Array[String]) { play(new Random) }

  def play(rand: Random) = {
    var notAWinner = false

    val aGame = new Game()
    aGame.add("Chet")
    aGame.add("Pat")
    aGame.add("Sue")

    do {
      aGame.roll(rand.nextInt(5) + 1)
      if (rand.nextInt(9) == 7) {
        notAWinner = aGame.wrongAnswer
      }
      else {
        notAWinner = aGame.wasCorrectlyAnswered
      }
    } while (notAWinner)
  }

  def playALot(rand: Random, times: Int) = {
    (1 to times).foreach { _ => play(rand) }
  }
}