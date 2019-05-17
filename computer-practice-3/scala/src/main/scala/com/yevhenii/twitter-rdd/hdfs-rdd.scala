package com.yevhenii.twitterRdd
import org.apache.spark.SparkConf
import org.apache.spark.streaming.twitter.TwitterUtils
import org.apache.spark.streaming.{Seconds, StreamingContext}

import scala.util.Properties
object hdfsRdd {
  def main(args: Array[String]) {
    val csvFilePath = "hdfs://namenode:8020/tables_data/data_set/For_Hire_Vehicles__FHV__-_Active.csv"
    val appName = "HDFSData"
    val conf = new SparkConf()
    conf.setAppName(appName).setMaster("local[2]")

    val spark = SparkSession.builder.config(conf).getOrCreate()

    val dataFrame = spark.read.format("CSV").option("header","true").load(csvfilePath)

    dataFrame.writeStream.format("console").start();
  }
}