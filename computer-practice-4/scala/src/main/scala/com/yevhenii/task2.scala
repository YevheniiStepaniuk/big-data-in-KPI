package com.yevhenii

import org.apache.spark.graphx.lib.ShortestPaths
import org.apache.spark.{SparkConf, SparkContext}
import org.apache.spark.graphx.{Edge, Graph, VertexId}
import org.apache.spark.rdd.RDD

import scala.util.Properties

class task2 extends Run{
  def run(): Unit = {
    val conf = new SparkConf().setAppName("openflights-app").setMaster("local[2]");
    val sc = new SparkContext(conf)

    val calculator = new DistanceCalculator();

    val routes = sc.textFile("hdfs://namenode:8020/graph/routes.csv")
      .map(line => line.split(";"))
      .filter(line => !line.contains("\\N"))
      .map(line => new Route(line(0), line(1).toLong, line(2), line(3).toLong, line(4), line(5).toLong))
      .cache()

    val airports= sc.textFile("hdfs://namenode:8020/graph/airports-extended.csv")
      .map(line => line.split(";"))
      .map(line => new Airport(line(0).toLong, line(1), line(2), line(3), line(4), line(5), line(6).toDouble, line(7).toDouble))

    val airportsCollection = airports.collect();


    val vertex: RDD[(VertexId, String)] = airports.map(airport => (airport.id, airport.name))
    val edges: RDD[Edge[Double]] = routes.map(route => {
      val sourceAirport = airportsCollection
        .filter(airport => airport.id == route.sourceAirportId)
        .map(airport => Location(airport.latitude, airport.longtitude))
        .head
      val destAirport = airportsCollection
        .filter(airport => airport.id == route.destinationAirportId)
        .map(airport => Location(airport.latitude, airport.longtitude))
        .head

      val dist = calculator.calculateDistanceInKilometer(sourceAirport, destAirport)

      Edge(route.sourceAirportId, route.destinationAirportId, dist)
    });

    val graph = Graph(vertex, edges).cache()

    val from = Properties.envOrElse("TASK_2__FROM_ID", "1").toLong
    val to = Properties.envOrElse("TASK_2__TO_ID", "2").toLong;
    val fromAirport = airportsCollection.filter(_.id == from).head;
    val toAirport = airportsCollection.filter(_.id == to).head

    val shortest: Option[Int] = ShortestPaths.run(graph, Seq(to)).vertices.filter(v => v._1 == from).first._2.get(to)
    val initialGraph = graph.mapVertices((id, _) => if (id == from) 0.0 else Double.NegativeInfinity)

    //http://lamastex.org/courses/ScalableDataScience/2016/graphXProgGuide.html
    val sssp = initialGraph.pregel(Double.PositiveInfinity)(
      (id, dist, newDist) => math.max(dist, newDist), // Vertex Program
      triplet => {  // Send Message
        if (triplet.srcAttr + triplet.attr < triplet.dstAttr) {
          Iterator((triplet.dstId, triplet.srcAttr + triplet.attr))
        } else {
          Iterator.empty
        }
      },
      (a,b) => math.max(a,b) // Merge Message
    )

    val longest = sssp.vertices.filter(x => x._1 == to).sortBy(x => x._2).map(_._2).first()

    println("Shortest path from " + fromAirport.name + " to " + toAirport.name + " : " + shortest.get)
    println("Longest path from " + fromAirport.name + " to " + toAirport.name + " : " + longest)

  }
}
