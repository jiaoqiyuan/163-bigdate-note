package com.imooc.spark

import org.apache.spark.sql.SparkSession

/**
  * DataFrame RDD interoperate
  */
object DataFrameRDDApp {
    def main(args: Array[String]): Unit = {
        //get sparksession
        val spark = SparkSession.builder()
                .appName("DataFrameRDDApp")
                .master("local[2]")
                .getOrCreate()

        //RDD => DataFrame
        val rdd = spark.sparkContext.textFile("/home/jony/resource/infos.txt")

        //需要导入隐式转换
        import spark.implicits._
        val infoDF = rdd.map(_.split(","))
                .map(line => Info(line(0).toInt, line(1), line(2).toInt))
                .toDF()
        infoDF.show()

        //采用DF API 的操作获取数据
        infoDF.filter(infoDF.col("age") > 30).show()

        //采用sql语句的方式获取结果
        infoDF.createOrReplaceTempView("infos")
        spark.sql("select * from infos where age > 30").show()

        spark.stop()

    }

    case class Info(id: Int, name: String, age: Int)
}
