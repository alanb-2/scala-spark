import Dependencies._
import com.typesafe.sbt.packager.docker.{Cmd, ExecCmd}

ThisBuild / crossPaths := false
ThisBuild / organization := "uk.org.aeb"
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

val appDir = "/app"
val configDir = "/config"
val sparkArchiveName = s"spark-$sparkVersion-bin-hadoop$hadoopMajorMinorVersion"

dockerCommands := Seq(
    Cmd("FROM", "alpine:3.13"),
    Cmd("ENV", "JAVA_HOME=/usr/lib/jvm/java-11-openjdk"),
    Cmd("ENV", "SPARK_HOME=/usr/local/spark"),
    Cmd("ENV", "PATH=$PATH:${SPARK_HOME}/bin"),
    Cmd(
        "RUN",
        "apk update && \\"
          + "apk add --no-cache bash && \\"
          + "apk add --no-cache openjdk11 && \\"
          + "apk add --no-cache curl && \\"
          + s"curl https://archive.apache.org/dist/spark/spark-$sparkVersion/$sparkArchiveName.tgz | tar -xz -C /usr/local/ && \\"
          + s"ln -s $sparkArchiveName" + " $SPARK_HOME && \\"
          + s"mkdir $configDir"
    ),
    Cmd("WORKDIR", appDir),
    Cmd("COPY", s"opt/docker/lib/${(assembly / assemblyJarName).value}", s"$appDir/"),
    Cmd("RUN",
        s"chmod +x $appDir/${(assembly / assemblyJarName).value} $configDir "
          + "${SPARK_HOME}/sbin/*.sh ${SPARK_HOME}/bin/*.sh"
    ),
    ExecCmd("ENTRYPOINT", "/bin/bash")
)
