package com.yevhenii

import org.apache.spark.graphx.{Edge, Graph, TripletFields, VertexId, VertexRDD}
import org.apache.spark.rdd.RDD
import org.apache.spark.{SparkConf, SparkContext}
class task3 extends Run{
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

    val airlines = sc.textFile("hdfs://namenode:8020/graph/airlines.csv")
      .map(line => line.split(";"))
      .map(line => new Airline(line(0).toInt, line(1)))
      .cache()

    val vertex: RDD[(VertexId, String)] = airports.map(airport => (airport.id, airport.name))

    val edges: RDD[Edge[(Long, Int)]] = routes.map(route => {
      val sourceAirport = airportsCollection
        .filter(airport => airport.id == route.sourceAirportId)
        .map(airport => Location(airport.latitude, airport.longtitude))
        .head
      val destAirport = airportsCollection
        .filter(airport => airport.id == route.destinationAirportId)
        .map(airport => Location(airport.latitude, airport.longtitude))
        .head

      val dist = calculator.calculateDistanceInKilometer(sourceAirport, destAirport)

      Edge(route.sourceAirportId, route.destinationAirportId, (route.airlineId, dist))
    });

    val graph = Graph(vertex, edges).cache()

    val temp: VertexRDD[(Long, Int)] = graph.aggregateMessages[(Long, Int)](triplet =>
      {triplet.sendToDst(triplet.attr)}, (a, b) => (a._1, a._2 + b._2), TripletFields.EdgeOnly)
    val airlineId = temp.sortBy(x => x._2._2).first()._2._1;

    val airline = airlines.filter(x => x.airlineId == airlineId).first();
    println("Airline with max routes length: " + airline.airlineId)
    println(airline.name);



  }
}
