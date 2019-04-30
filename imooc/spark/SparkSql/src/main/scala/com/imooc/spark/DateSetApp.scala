package com.imooc.spark

import org.apache.spark.sql.SparkSession

/**
  * DateSet operation
  */
object DateSetApp {
    def main(args: Array[String]): Unit = {
        val spark = SparkSession.builder().appName("DateSetApp").master("local[2]").getOrCreate()

        val df = spark.read.option("header", "true").option("inferSchema", "true").csv("/home/jony/resource/data/sales.csv").toDF

        df.show()

        //需要导入隐式转换
        import spark.implicits._
        val ds = df.as[Sales]
        ds.map(line => line.itemId).show()

        spark.stop()
    }

    case class Sales(transactionId: Int, customerId: Int, itemId: Int, amountPaid: Double)
}
