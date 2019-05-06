package com.yevhenii

import org.apache.spark.graphx.{Edge, Graph}
import org.apache.spark.{SparkConf, SparkContext}

class task1 {
  val conf = new SparkConf().setAppName("openflights-app").setMaster("local[2]");
  val sc = new SparkContext(conf)

  val file = sc.textFile("src/main/resources/textFile1.csv").map(line => line.split(","))


  val vertex = file.map(line => (line(3).toLong, line(2)))
  val edges = file.map(line => new Edge(line(3).toLong, line(5).toLong))

  val graph = Graph(vertex, edges).cache()

  val maxVertex = graph.inDegrees.union(graph.outDegrees).sortBy(_._2, false).take(1);
  val minVertex = graph.inDegrees.union(graph.outDegrees).sortBy(_._2, true).take(1);

  val maxAirport = graph.vertices.filter((v) => v._1 == maxVertex).map(_._2)
  val minAirport = graph.vertices.filter((v) => v._1 == minVertex).map(_._2)

}
