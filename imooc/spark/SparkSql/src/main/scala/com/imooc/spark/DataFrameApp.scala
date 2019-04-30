package com.imooc.spark

import org.apache.spark.sql.SparkSession

/**
  * DataFram API basic operations
  */
object DataFrameApp {
    def main(args: Array[String]): Unit = {
        //create spark session
        val sparkSession = SparkSession.builder().appName("DataFrameApp")
                .master("local[2]").getOrCreate()

        //load json file
        val peopleDF = sparkSession.read.format("json")
                .load("/home/jony/apps/packages/spark-2.1.0/dist/examples/src/main/resources/people.json")

        //print schema info from dataframe
        peopleDF.printSchema()

        //print 20 contents from dataframe
        peopleDF.show()

        //print name column data
        peopleDF.select("name").show()
        peopleDF.select(peopleDF.col("name"), (peopleDF.col("age") + 10).as("add_age")).show()

        //filter age
        peopleDF.filter(peopleDF.col("age") > 19).show()

        //group by some column and do aggregation
        peopleDF.groupBy("age").count().show()

        //close sparksession
        sparkSession.stop()
    }
}
