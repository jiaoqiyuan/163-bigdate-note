package com.imooc.flink.scala.course03

import org.apache.flink.streaming.api.scala.StreamExecutionEnvironment
import org.apache.flink.streaming.api.windowing.time.Time

object StreamingWCScalaApp {
    def main(args: Array[String]): Unit = {
        val env = StreamExecutionEnvironment.getExecutionEnvironment

        val text = env.socketTextStream("localhost", 9999)

        import org.apache.flink.api.scala._
        text.flatMap(_.split(","))
                .map(x => WC(x, 1))
                .keyBy("word")
                .timeWindow(Time.seconds(5))
                .sum("count")
                .print()
                .setParallelism(1)

        env.execute("StreamingWCScalaApp")
    }

    case class WC(word: String, count: Int)
}
