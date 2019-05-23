package com.yevhenii

import scala.util.Properties

object mainApplication {
  def main(args: Array[String]): Unit ={
    val mode = Properties.envOrElse("MODE", "1")

    println(s"Running Task#" + mode)

    val run: Unit = mode match {
      case "1" => new task1().run()
      case "2" => new task2().run()
      case "3" => new task3().run()
      case "4" => new task4().run()

    }
  }
}


abstract class Run {
  def run();
}