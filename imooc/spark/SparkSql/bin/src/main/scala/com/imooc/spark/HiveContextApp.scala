package com.imooc.spark

import org.apache.spark.sql.hive.HiveContext
import org.apache.spark.{SparkConf, SparkContext}

object HiveContextApp {

  def main(args: Array[String]): Unit = {
    //创建相应的Context
    val sparkConf = new SparkConf()
//    sparkConf.setAppName("HiveContextApp").setMaster("local[2]")
    val sparkContext = new SparkContext()
    val hiveContext = new HiveContext(sparkContext)

    //相关处理
    hiveContext.table("emp").show()

    //关闭资源
    sparkContext.stop()
  }

}
