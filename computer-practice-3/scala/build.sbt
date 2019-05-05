name := "TwitterToStreaming"
version := "1.0"
scalaVersion := "2.11.12"

libraryDependencies += "org.apache.kafka" %% "kafka" % "0.9.0.1"
libraryDependencies += "org.apache.kafka" % "kafka-clients" % "0.9.0.1"

libraryDependencies += "it.nerdammer.bigdata" % "spark-hbase-connector_2.10" % "1.0.3"

libraryDependencies += "org.apache.spark" %% "spark-core" % "1.6.1"
libraryDependencies += "org.apache.spark" %% "spark-sql" % "1.6.1"
libraryDependencies += "org.apache.spark" %% "spark-streaming" % "1.6.1"
libraryDependencies += "org.apache.spark" %% "spark-streaming-kafka" % "1.6.1"
libraryDependencies += "org.apache.spark" %% "spark-streaming-twitter" % "1.6.3"

libraryDependencies += "org.json4s" %% "json4s-jackson" % "3.2.11"

libraryDependencies += "log4j" % "log4j" % "1.2.17"



resolvers += "cloudera-repos" at "https://repository.cloudera.com/artifactory/cloudera-repos/"