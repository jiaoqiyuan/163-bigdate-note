package com.imooc.spark

import org.apache.spark.{SparkConf, SparkContext}
import org.apache.spark.sql.SQLContext;

/**
  * SQLContext 使用
  */
object SQLContextApp {

  def main(args: Array[String]): Unit = {

    val path = "file:///home/jony/apps/packages/spark-2.1.0/python/test_support/sql/people.json"
    //创建相应的Context
    val sparkConf = new SparkConf()
    sparkConf.setAppName("SQLContextApp").setMaster("local[2]")

    val sc = new SparkContext(sparkConf)
    val sqlContext = new SQLContext(sc)

    //相关处理，处理JSON 文件
    val people = sqlContext.read.format("json").load(path)
    people.printSchema()
    people.show()

    //关闭资源
    sc.stop()

  }

}
