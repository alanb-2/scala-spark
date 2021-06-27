import Dependencies._

ThisBuild / organization := "org.my"
ThisBuild / scalacOptions += ""
ThisBuild / scalaVersion := "2.13.5"
ThisBuild / version := "0.1"

mainClass in (Compile, run) := Some("org.my.Main")
mainClass in (Compile, packageBin) := Some("org.my.Main")
mainClass in assembly := Some("org.my.Main")

lazy val root = (project in file("."))
  .enablePlugins(JavaAppPackaging)
  .settings(
      name := "scala-sbt-template",
      libraryDependencies ++= rootDependencies,
      test in assembly := {}
  )
