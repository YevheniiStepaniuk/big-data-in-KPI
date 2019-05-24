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
    val appName = "HDFSData"
    val conf = new SparkConf()
    conf.setAppName(appName).setMaster("local[2]").set("spark.hbase.host", "hbase")

    val spark = SparkSession.builder.config(conf).getOrCreate()
    spark.setCassandraConf(CassandraConnectorConf.KeepAliveMillisParam.option(10000))
    spark.setCassandraConf("cassandra-seed-node", CassandraConnectorConf.ConnectionHostParam.option("cassandra-seed-node"))

    val schema = new StructType()
      .add("active", "string")
      .add("vehicle_license_number", "string")
      .add("name", "string")
      .add("license_type", "string")
      .add("expiration_date", "date")
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
      .add("order_date", "date")
      .add("last_date_updated", "date")
      .add("last_time_updated", "string")

    val dataFrame: DataFrame = spark
      .read
      .option("header","true")
//      .schema(schema)
      .csv(csvFilePath)
//      .where("Vehicle_Year = '2014'")


    dataFrame.show(10)

    dataFrame.coalesce(1).write.option("header", "true").csv(csvOutputFilePath)
    dataFrame.write.format("org.apache.spark.sql.cassandra").options(Map( "table" -> "vehicles", "keyspace" -> "testkeyspace")).save()
  }
}