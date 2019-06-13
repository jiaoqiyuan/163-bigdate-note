package com.imooc.flink.scala.course04

import org.apache.commons.io.FileUtils
import org.apache.flink.api.common.functions.RichMapFunction
import org.apache.flink.api.scala.ExecutionEnvironment
import org.apache.flink.configuration.Configuration

object DistributeCatchApp {
    def main(args: Array[String]): Unit = {
        val env = ExecutionEnvironment.getExecutionEnvironment
        val filePath = "file:\\D:\\imooc\\新一代大数据计算引擎 Flink从入门到实战-v\\input\\hello.txt"

        //1. 注册一个本地文件
        env.registerCachedFile(filePath, "scala-cf")

        import org.apache.flink.api.scala._
        val data = env.fromElements("hadoop", "spark", "flink", "pyspark", "storm")

        data.map(new RichMapFunction[String, String] {
            //2.在open方法中获取本地或HDFS的文件即可
            override def open(parameters: Configuration): Unit = {
                val file = getRuntimeContext.getDistributedCache.getFile("scala-cf")
                //lines是java的list
                val lines = FileUtils.readLines(file)
                import scala.collection.JavaConverters._
                for (ele <- lines.asScala) {
                    println(ele)
                }
            }
            override def map(value: String): String = {
                value
            }
        }).print()
    }
}
