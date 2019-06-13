package com.imooc.flink.scala.course02

import org.apache.flink.api.scala.ExecutionEnvironment

/**
  * 使用Scala开发应用程序
  */
object BatchWCScalaApp {
    def main(args: Array[String]): Unit = {
        val input = "file:///D:/imooc/新一代大数据计算引擎 Flink从入门到实战-v/input"
        val environment = ExecutionEnvironment.getExecutionEnvironment

        val text = environment.readTextFile(input)

        import org.apache.flink.api.scala._

        text.flatMap(_.toLowerCase.split("\t"))
            .filter(_.nonEmpty)
            .map((_, 1))
            .groupBy(0)
            .sum(1)
            .print()

    }
}
