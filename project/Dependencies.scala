import sbt._

object Dependencies {

  lazy val awsJavaSdkVersion = "1.11.375"
  lazy val deltaVersion = "0.8.0"
  lazy val hadoopVersion = "3.2.0"
  lazy val scalaTestVersion = "3.2.5"
  lazy val sparkVersion = "3.0.2"
  lazy val typesafeVersion = "1.4.1"

  val hadoopAwsDependency = "org.apache.hadoop" % "hadoop-aws" % hadoopVersion

  val awsDependencies = Seq(
      hadoopAwsDependency
  )

  val scalacticDependency = "org.scalactic" %% "scalactic" % scalaTestVersion
  val scalaTestDependency = "org.scalatest" %% "scalatest" % scalaTestVersion
  val typesafeDependency = "com.typesafe" % "config" % typesafeVersion

  val rootDependencies = Seq(
      scalacticDependency % Test,
      scalaTestDependency % Test,
      typesafeDependency
  )

  val deltaDependency = "io.delta" %% "delta-core" % deltaVersion
  val sparkDependency = "org.apache.spark" %% "spark-core" % sparkVersion
  val sparkSqlDependency = "org.apache.spark" %% "spark-sql" % sparkVersion

  val sparkDependencies = Seq(
      deltaDependency,
      sparkDependency % Provided,
      sparkSqlDependency % Provided
  )

}
