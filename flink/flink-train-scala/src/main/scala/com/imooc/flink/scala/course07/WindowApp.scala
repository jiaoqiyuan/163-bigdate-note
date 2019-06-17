package com.imooc.flink.scala.course07

import org.apache.flink.streaming.api.scala.StreamExecutionEnvironment
import org.apache.flink.streaming.api.windowing.time.Time

object WindowApp {
    def main(args: Array[String]): Unit = {
        val environment = StreamExecutionEnvironment.getExecutionEnvironment
        val text = environment.socketTextStream("localhost", 9999)
        import org.apache.flink.api.scala._
        text.flatMap(_.split(","))
            .map((_, 1))
            .keyBy(0)
            .timeWindow(Time.seconds(10), Time.seconds(5))
            .sum(1)
            .print()
            .setParallelism(1)

        environment.execute("WindowApp")
    }
}
