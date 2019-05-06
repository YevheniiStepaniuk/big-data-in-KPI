name := "TwitterToStreaming"
version := "1.0"
scalaVersion := "2.11.12"

libraryDependencies += "org.apache.spark" %% "spark-core" % "1.6.1"

resolvers += "cloudera-repos" at "https://repository.cloudera.com/artifactory/cloudera-repos/"

libraryDependencies += "org.apache.spark" %% "spark-graphx" % "2.4.2"


assemblyMergeStrategy in assembly := {
  case PathList("META-INF", xs @ _*) => MergeStrategy.discard
  case _ => MergeStrategy.first
}