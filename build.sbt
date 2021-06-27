import Dependencies._
import com.typesafe.sbt.packager.docker.{Cmd, ExecCmd}

ThisBuild / crossPaths := false
ThisBuild / organization := "org.uk.aeb"
ThisBuild / scalacOptions += ""
ThisBuild / scalaVersion := "2.12.14"
ThisBuild / version := "0.1"

ThisBuild / resolvers ++= Seq(
    Resolver.mavenLocal
)

lazy val root = (project in file("."))
  .enablePlugins(JavaAppPackaging)
  .settings(
      name := "scala-spark",
      libraryDependencies ++= awsDependencies ++ rootDependencies ++ sparkDependencies,
      assembly / mainClass := Some(s"${organization.value}.Main"),
      assembly / assemblyJarName := "scala-spark.jar",
      assembly / test := {}
  )

Test / unmanagedSourceDirectories += baseDirectory.value / "src/it/scala"

assembly / assemblyMergeStrategy := {
  case x if x.contains("io.netty.versions.properties") =>
    MergeStrategy.discard
  case x                                               =>
    val oldStrategy = (assembly / assemblyMergeStrategy).value
    oldStrategy(x)
}

publishMavenStyle := true

Universal / mappings := {
  val universalMappings = (Universal / mappings).value
  val fatJar = (Compile / assembly).value
  val filtered = universalMappings.filter { case (_, name) =>
    !name.endsWith(".jar")
  }
  filtered :+ (fatJar -> ("lib/" + fatJar.getName))
}

dockerCommands := Seq(
    Cmd("FROM", "datamechanics/spark:jvm-only-3.0.2-hadoop-3.2.0-java-11-scala-2.12-dm13"),
    Cmd("COPY", s"opt/docker/lib/${(assembly / assemblyJarName).value}", "/app"),
    ExecCmd("ENTRYPOINT", "/bin/bash")
)
