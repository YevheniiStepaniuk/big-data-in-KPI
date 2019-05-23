package com.yevhenii.hdfsRdd
import it.nerdammer.spark.hbase.conversion.FieldWriter
import org.apache.spark.SparkConf
import org.apache.spark.sql.types.StructType
import org.apache.spark.sql.{DataFrame, Row, SparkSession}

import scala.util.Properties

object hdfsRdd {

  implicit def myDataWriter: FieldWriter[Row] = new FieldWriter[Row] {
    override def map(data: Row): HBaseData =
      Seq(

      )

    override def columns = Seq("prg", "name")
  }
  def main(args: Array[String]) {
    val csvFilePath = Properties.envOrElse("CSV_FILE_PATH", "")
    val csvOutputFilePath = Properties.envOrElse("CSV_OUTPUT_FILE_PATH", "")
    val appName = "HDFSData"
    val conf = new SparkConf()
    conf.setAppName(appName).setMaster("local[2]").set("spark.hbase.host", "hbase")

    val spark = SparkSession.builder.config(conf).getOrCreate()

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
      .schema(schema)
      .csv(csvFilePath)
      .where("base_type = 'LUXURY'")

    println(csvOutputFilePath)

    dataFrame.show(10)

    dataFrame.write.option("header", "true").csv(csvOutputFilePath)
//    val rdd: RDD[Row] = dataFrame.rdd
//    rdd.toHBaseTable("rdd").toColumns("active", "vehicle_license_number", "name", "license_type", "expiration_date", "permit_license_number", "dmv_license_plate_number", "vehicle_vin_number", "wheelchair_accessible", "certification_date", "hack_up_date", "vehicle_year", "base_number", "base_name", "base_type", "veh", "base_telephone_number", "website", "base_address", "reason", "order_date", "last_date_updated", "last_time_updated");
  }
}