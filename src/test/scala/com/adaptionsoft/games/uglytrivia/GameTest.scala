package com.adaptionsoft.games.uglytrivia

import java.nio.charset.{StandardCharsets, Charset}
import java.nio.file.{Paths, Files}

import com.adaptionsoft.games.AppTest
import com.adaptionsoft.games.trivia.runner.GameRunner

/**
 * Created by arturas on 15.2.28.
 */
class GameTest extends AppTest {
  def gameTest(f: (Game, ReadConsole) => Unit) = captureOut(f(new Game, _))
  def gameTestWithPlayers(f: (Game, ReadConsole, Seq[String]) => Unit) = {
    val players = Seq("First", "Second", "Third")
    val game = captureOut { _ =>
      val game = new Game
      players.foreach(game.add)
      game
    }
    captureOut(f(game, _, players))
  }

  "Some game" - {
    "should print nothing if just instantiated" in gameTest { (game, read) =>
      read() shouldBe ""
    }

    "should print a player name when he/she is added" in
      gameTest { (game, read) =>
        game.add("Jonas")
        read() shouldBe "Jonas was added\nThey are player number 1\n"
      }

    "should print player names when they are added" in
      gameTest { (game, read) =>
        val names = Seq("Jonas", "Tadas", "Petras")
        names.foreach(game.add)
        val expected = names.zipWithIndex.map { case (name, idx) =>
          s"$name was added\nThey are player number ${idx + 1}"
        }.mkString("\n") + "\n"
        read() shouldBe expected
      }

    "should print rock question 1" in
    gameTest { (game, read) =>
      game.createRockQuestion(0) shouldBe "Rock Question 0"
    }

    "should output question" in
    gameTestWithPlayers { (game, read, players) =>
      game.roll(5)
      read() shouldBe """First is the current player
                        |They have rolled a 5
                        |First's new location is 5
                        |The category is Science
                        |Science Question 0
                        |""".stripMargin
    }

    "when flagging for the right answer" - {
      "should say that the answer was correct" in
      gameTestWithPlayers { (game, read, players) =>
        val (out, winner) = game.wasCorrectlyAnsweredSafe
        out.headOption shouldBe Some("Answer was correct!!!!")
      }
    }

    "when flagging for the wrong answer" - {
      "should put a player in the penalty box" in
      gameTestWithPlayers { (game, read, players) =>
        val currentPlayer = game.currentPlayer
        game.inPenaltyBox(currentPlayer) shouldBe false
        game.wrongAnswer
        game.inPenaltyBox(currentPlayer) shouldBe true
      }

      "should increase current player" in
      gameTestWithPlayers { (game, read, players) =>
        val currentPlayer = game.currentPlayer
        game.wrongAnswer
        game.currentPlayer shouldBe currentPlayer + 1
      }

      "should produce some output" in
      gameTestWithPlayers { (game, read, players) =>
        game.wrongAnswer
        read() shouldBe """Question was incorrectly answered
                          |First was sent to the penalty box
                          |""".stripMargin
      }

      "should return some output" in
      gameTestWithPlayers { (game, _, _) =>
        val (output, ret) = game.wrongAnswerSafe()
        output shouldBe Vector(
          "Question was incorrectly answered",
          "First was sent to the penalty box"
        )
      }
    }
  }

  "Current implementation" - {
    "should have identical output to golden master" in {
      val rand = new java.util.Random(0l)
      val path = Paths.get("src", "test", "resources", "golden-master.txt")
      val expected =
        new String(Files.readAllBytes(path), StandardCharsets.UTF_8)
      val actual = captureOut { read =>
        GameRunner.playALot(rand, 300)
        read()
      }
      actual shouldBe expected
    }
  }
}
