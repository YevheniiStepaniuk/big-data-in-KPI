package com.yevhenii.hdfsRdd
import com.datastax.spark.connector.cql.CassandraConnectorConf
import org.apache.spark.SparkConf
import org.apache.spark.sql.types.StructType
import org.apache.spark.sql.{DataFrame, SparkSession}
import org.apache.spark.sql.cassandra._

import scala.util.Properties

object hdfsRdd {
  def main(args: Array[String]) {
    val csvFilePath = Properties.envOrElse("CSV_FILE_PATH", "")
    val csvOutputFilePath = Properties.envOrElse("CSV_OUTPUT_FILE_PATH", "")
    val cassandraKeySpace = Properties.envOrElse("CASSANDRA_KEYSPACE", "");
    val cassandraTable = Properties.envOrElse("CASSANDRA_TABLE", "");

    val writeToCassandra = Properties.envOrElse("WRITE_TO_CASSANDRA", "false").toBoolean;
    val writeToHDFS = Properties.envOrElse("WRITE_TO_HDFS", "false").toBoolean;
    val useKafka = Properties.envOrElse("USE_KAFKA", "false").toBoolean;

    val cassandraHost = Properties.envOrElse("CASSANDRA_HOST", "");
    val cassandraUser = Properties.envOrElse("CASSANDRA_USER", "");
    val cassandraPassword = Properties.envOrElse("CASSANDRA_PASSWORD", "");

    val appName = "HDFSData"
    val conf = new SparkConf()
    conf.setAppName(appName).setMaster("local[2]")
      .set("spark.cassandra.connection.host", cassandraHost)
      .set("spark.cassandra.auth.username", cassandraUser)
      .set("spark.cassandra.auth.password", cassandraPassword)

    val spark = SparkSession.builder.config(conf).getOrCreate()
      .setCassandraConf(CassandraConnectorConf.KeepAliveMillisParam.option(10000))
      .setCassandraConf(cassandraHost,
        CassandraConnectorConf.ConnectionHostParam.option(cassandraHost))

    val schema = new StructType()
      .add("active", "string")
      .add("vehicle_license_number", "string")
      .add("name", "string")
      .add("license_type", "string")
      .add("expiration_date", "string")
      .add("permit_license_number", "string")
      .add("dmv_license_plate_number", "string")
      .add("vehicle_vin_number", "string")
      .add("wheelchair_accessible", "string")
      .add("certification_date", "date")
      .add("hack_up_date", "date")
      .add("vehicle_year", "string")
      .add("base_number", "string")
      .add("base_name", "string")
      .add("base_type", "string")
      .add("veh", "string")
      .add("base_telephone_number", "string")
      .add("website", "string")
      .add("base_address", "string")
      .add("reason", "string")
      .add("order_date", "string")
      .add("last_date_updated", "string")
      .add("last_time_updated", "string")

    val columnNames = Seq("name", "vehicle_year")

    val dataFrame: DataFrame = spark
      .read
      .option("header","true")
      .schema(schema)
      .csv(csvFilePath)
      .where("vehicle_year = '2014'")
//      .select(columnNames.head, columnNames.tail: _*)


    dataFrame.show(10)

    if(useKafka){
      val ds = dataFrame
        .select(columnNames.head, columnNames.tail: _*)
        .write
        .format("kafka")
        .option("kafka.bootstrap.servers", "kafka:9092")
        .option("topic", "vehicles")
        .save()
    }

    if(writeToHDFS)
      dataFrame.coalesce(1).write.option("header", "true").csv(csvOutputFilePath)

    if(writeToCassandra)
      dataFrame.write.format("org.apache.spark.sql.cassandra").options(Map( "table" -> cassandraTable, "keyspace" -> cassandraKeySpace)).save()
  }
}