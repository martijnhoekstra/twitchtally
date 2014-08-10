import scalariform.formatter.preferences._

name := "twitchtally"

version := "0.0.1-SNAPSHOT"

scalaVersion := "2.11.1"

resolvers ++= Seq(
  "Sonatype Releases" at "http://oss.sonatype.org/content/repositories/releases",
  "Typesafe Repository" at "http://repo.typesafe.com/typesafe/releases"
)

libraryDependencies ++= Seq(
  "org.scalacheck" %% "scalacheck" % "1.11.4" % "test",
  "io.spray" %% "spray-can" % "1.3.1",
  "io.spray" %% "spray-routing" % "1.3.1",
  "com.typesafe.akka" %% "akka-actor" % "2.3.4"
)

//lazy val root = (project in file(".")).enablePlugins(SbtTwirl)

Revolver.settings

//scalariformSettings

//ScalariformKeys.preferences := ScalariformKeys.preferences.value
//  .setPreference(AlignSingleLineCaseStatements, true)
//  .setPreference(PreserveDanglingCloseParenthesis, true)