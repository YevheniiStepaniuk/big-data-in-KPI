name := "TwitterToStreaming"
version := "1.0"
scalaVersion := "2.11.12"

libraryDependencies += "org.apache.spark" %% "spark-core" % "2.4.3"
libraryDependencies += "org.apache.spark" %% "spark-streaming" % "2.4.3"
libraryDependencies += "org.apache.spark" %% "spark-sql" % "2.4.3"
libraryDependencies += "com.datastax.spark" %% "spark-cassandra-connector" % "2.4.1"
libraryDependencies += "org.apache.spark" % "spark-sql-kafka-0-10_2.11" % 2.2.0

libraryDependencies += "org.json4s" %% "json4s-jackson" % "3.2.11"

resolvers += "cloudera-repos" at "https://repository.cloudera.com/artifactory/cloudera-repos/"


assemblyMergeStrategy in assembly := {
  case PathList("META-INF", xs @ _*) => MergeStrategy.discard
  case _ => MergeStrategy.first
}

mainClass in assembly := Some("com.yevhenii.hdfsRdd.hdfsRdd")