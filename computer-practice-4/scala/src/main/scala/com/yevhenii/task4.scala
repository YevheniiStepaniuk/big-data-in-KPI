package com.yevhenii

import org.apache.spark.graphx.{Edge, Graph, VertexId}
import org.apache.spark.rdd.RDD
import org.apache.spark.{SparkConf, SparkContext}

class task4 {
  def run(): Unit = {
    val conf = new SparkConf().setAppName("openflights-app").setMaster("local[2]");
    val sc = new SparkContext(conf)

    val routes = sc.textFile("hdfs://namenode:8020/graph/routes.csv")
      .map(line => line.split(","))
      .filter(line => !line.contains("\\N"))
      .map(line => new Route(line(0), line(1).toLong, line(2), line(3).toLong, line(4), line(5).toLong))
      .cache()

    val airports= sc.textFile("hdfs://namenode:8020/graph/airports-extended.csv")
      .map(line => line.split(","))
      .map(line => new Airport(line(0).toLong, line(1), line(2), line(3), line(4), line(5), line(6).toDouble, line(7).toDouble))

    val vertex: RDD[(VertexId, String)] = airports.map(airport => (airport.id, airport.name))
    val edges: RDD[Edge[VertexId]] = routes.map(route =>
      Edge(route.sourceAirportId, route.destinationAirportId)
    );

    val graph = Graph(vertex, edges).cache()

   val ids = graph.inDegrees.union(graph.outDegrees).filter(_._2 == 0).map(_._1).collect()

    if(ids.length  == 0)
      println("There no isolated airports")
    else println("Found " + ids.length + " isolated airports:")

    ids.foreach(id => {
      val port = airports.filter(x =>x.id  == id).map(_.name).first()

      println(port)
    })
  }
}
