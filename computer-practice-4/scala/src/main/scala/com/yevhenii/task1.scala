package com.yevhenii

import org.apache.spark.graphx.{Edge, Graph, VertexId}
import org.apache.spark.rdd.RDD
import org.apache.spark.{SparkConf, SparkContext}

class task1 extends Run{

  def run() {
    val conf = new SparkConf().setAppName("openflights-app").setMaster("local[2]");
    val sc = new SparkContext(conf)

    val routes: RDD[Route] = sc.textFile("hdfs://namenode:8020/graph/routes.csv")
      .map(line => line.split(";"))
      .filter(line => !line.contains("\\N"))
      .map(line => new Route(line(0), line(1).toLong, line(2), line(3).toLong, line(4), line(5).toLong))

    val airports: RDD[Airport] = sc.textFile("hdfs://namenode:8020/graph/airports-extended.csv")
      .map(line => line.split(";"))
      .map(line => new Airport(line(0).toLong, line(1), line(2), line(3), line(4), line(5), line(6).toDouble, line(7).toDouble))

    val vertex: RDD[(VertexId, String)] = airports.map(airport => (airport.id, airport.name))
    val edges: RDD[Edge[VertexId]] = routes.map(route => Edge(route.sourceAirportId, route.destinationAirportId))

    val graph = Graph(vertex, edges).cache()
    val maxVertex = graph.inDegrees.union(graph.outDegrees).sortBy(_._2, false).take(1).head
    val minVertex = graph.inDegrees.union(graph.outDegrees).sortBy(_._2, true).take(1).head

    val maxAirport: String = graph.vertices.filter((v) => v._1 == maxVertex._1).map(_._2).first()
    val minAirport: String = graph.vertices.filter((v) => v._1 == minVertex._1).map(_._2).first()

    println("Airport with max routes: ")
    println(maxAirport)

    println("Airport with min routes: ")
    println(minAirport)
  }
}
