package com.yevhenii.twitterRdd
import org.apache.spark.SparkConf
import org.apache.spark.streaming.twitter.TwitterUtils
import org.apache.spark.streaming.{Seconds, StreamingContext}
import twitter4j.auth.OAuthAuthorization
import twitter4j.conf.ConfigurationBuilder
import it.nerdammer.spark.hbase._

import scala.util.Properties
object twitterRdd {
  def main(args: Array[String]) {
    val appName = "TwitterData"
    val conf = new SparkConf()
    conf.setAppName(appName).setMaster("local[2]")

    val ssc = new StreamingContext(conf, Seconds(5))

    val consumerKey = Properties.envOrElse("TWITTER_CONSUMER_KEY", "")
    val consumerSecret = Properties.envOrElse("TWITTER_CONSUMER_SECRET", "")
    val accessToken = Properties.envOrElse("TWITTER_ACCESS_TOKEN", "")
    val accessTokenSecret = Properties.envOrElse("TWITTER_ACCESS_TOKEN_SECRET", "")
    val filters = Properties.envOrElse("TWITTER_HASH_TAGS_FILTERS", "spark scala music").split(' ')

    val cb = new ConfigurationBuilder
    cb.setDebugEnabled(true).setOAuthConsumerKey(consumerKey)
      .setOAuthConsumerSecret(consumerSecret)
      .setOAuthAccessToken(accessToken)
      .setOAuthAccessTokenSecret(accessTokenSecret)
    val auth = new OAuthAuthorization(cb.build)


    val twitterStream = TwitterUtils.createStream(ssc, Some(auth), filters)

    val hashTags = twitterStream.flatMap(status => status.getText.split(" ").filter(_.startsWith("#")))

  ssc.sparkContext.parallelize()
    val topCounts60 = hashTags.map((_, 1)).reduceByKeyAndWindow(_ + _, Seconds(60))
      .map{case (topic, count) => (count, topic)}
      .transform(_.sortByKey(false))
//
//    topCounts60.saveAsHadoopFiles("")


    topCounts60.foreachRDD(rdd => {
      val topList = rdd.take(10).toList
      val r = topList.map{case (count, tag) => s"$tag: $count"}

      rdd.toHBaseTable("mytable")
        .toColumns("column1", "column2")
        .inColumnFamily("mycf")
        .save()
      println(r)
    })

    ssc.start()
    ssc.awaitTermination()
  }
}