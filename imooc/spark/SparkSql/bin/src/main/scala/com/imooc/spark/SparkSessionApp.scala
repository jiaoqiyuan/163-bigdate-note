package com.imooc.spark

import org.apache.spark.sql.SparkSession

object SparkSessionApp {
  def main(args: Array[String]): Unit = {
    val spark = SparkSession.builder().appName("SparkSessionApp").master("local[2]").getOrCreate()

    val people = spark.read.format("json").load("/home/jony/apps/packages/spark-2.1.0/dist/examples/src/main/resources/people.json")
    people.show()

    spark.stop()
  }
}
