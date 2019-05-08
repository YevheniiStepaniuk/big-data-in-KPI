package com.yevhenii

import org.apache.spark.graphx.lib.ShortestPaths
import org.apache.spark.{SparkConf, SparkContext}
import org.apache.spark.graphx.{Edge, Graph, VertexId}
import org.apache.spark.rdd.RDD

class task2 {
  def run(): Unit = {
    val conf = new SparkConf().setAppName("openflights-app").setMaster("local[2]");
    val sc = new SparkContext(conf)

    val calculator = new DistanceCalculator();

    val routes = sc.textFile("hdfs://namenode:8020/graph/routes.csv")
      .map(line => line.split(","))
      .filter(line => !line.contains("\\N"))
      .map(line => new Route(line(0), line(1).toLong, line(2), line(3).toLong, line(4), line(5).toLong))
      .cache()

    val airports= sc.textFile("hdfs://namenode:8020/graph/airports-extended.csv")
      .map(line => line.split(","))
      .map(line => new Airport(line(0).toLong, line(1), line(2), line(3), line(4), line(5), line(6).toDouble, line(7).toDouble))

    val vertex: RDD[(VertexId, String)] = airports.map(airport => (airport.id, airport.name))
    val edges: RDD[Edge[VertexId]] = routes.map(route => {
      val sourceAirport = airports
        .filter(airport => airport.id == route.sourceAirportId)
        .map(airport => Location(airport.latitude, airport.longtitude))
        .first()
      val destAirport = airports
        .filter(airport => airport.id == route.destinationAirportId)
        .map(airport => Location(airport.latitude, airport.longtitude))
        .first()

      val dist = calculator.calculateDistanceInKilometer(sourceAirport, destAirport)

      Edge(route.sourceAirportId, route.destinationAirportId, dist)
    });

    val graph = Graph(vertex, edges).cache()

    val from = 3
    val to =2;
    val shortest = ShortestPaths.run(graph, Seq(from)).vertices.filter(v => v._1 == to).first._2.get(from)
  }
}
