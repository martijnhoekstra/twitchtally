import scalariform.formatter.preferences._
import com.typesafe.sbt.SbtStartScript

seq(SbtStartScript.startScriptForClassesSettings: _*)

name := "twitchtally"

version := "0.0.1-SNAPSHOT"

scalaVersion := "2.10.3"

scalacOptions := Seq("-unchecked", "-deprecation", "-encoding", "utf8")

resolvers ++= Seq(
  "Sonatype Releases" at "http://oss.sonatype.org/content/repositories/releases",
  "Typesafe Repository" at "http://repo.typesafe.com/typesafe/releases",
  "spray" at "http://repo.spray.io/"
)

libraryDependencies ++= Seq(
  "org.scalacheck" %% "scalacheck" % "1.11.4" % "test",
  "io.spray" % "spray-can" % "1.3.1",
  "io.spray" % "spray-routing" % "1.3.1",
  "io.spray" % "spray-client" % "1.3.1",
  "io.spray" %% "spray-json" % "1.2.6",
  "com.typesafe.akka" %% "akka-actor" % "2.3.0",
  "com.wandoulabs.akka" %% "spray-websocket" % "0.1.2"
)

//lazy val root = (project in file(".")).enablePlugins(SbtTwirl)

Revolver.settings

//scalariformSettings

//ScalariformKeys.preferences := ScalariformKeys.preferences.value
//  .setPreference(AlignSingleLineCaseStatements, true)
//  .setPreference(PreserveDanglingCloseParenthesis, true)