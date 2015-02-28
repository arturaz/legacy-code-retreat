import sbt._

scalaVersion := "2.11.5"

name := "legacy code retreat"

resolvers += Resolver.sonatypeRepo("releases")

resolvers += Resolver.sonatypeRepo("snapshots")

val Scalaz = "7.1.1"

libraryDependencies ++= Seq(
  "org.scalaz" %% "scalaz-core" % Scalaz,
  "org.scalaz" %% "scalaz-effect" % Scalaz
)

val Monocle = "1.0.1"   // or "1.1.0-SNAPSHOT"

libraryDependencies ++= Seq(
  "com.github.julien-truffaut"  %%  "monocle-core"    % Monocle,
  "com.github.julien-truffaut"  %%  "monocle-generic" % Monocle,
  "com.github.julien-truffaut"  %%  "monocle-macro"   % Monocle,
  "com.github.julien-truffaut"  %%  "monocle-law"     % Monocle % "test"
)

libraryDependencies += "org.scalatest" % "scalatest_2.11" % "2.2.4" % "test"