package com.imooc.flink.scala.course07

import org.apache.flink.api.java.tuple.Tuple
import org.apache.flink.streaming.api.scala.StreamExecutionEnvironment
import org.apache.flink.streaming.api.scala.function.ProcessWindowFunction
import org.apache.flink.streaming.api.windowing.time.Time
import org.apache.flink.streaming.api.windowing.windows.TimeWindow
import org.apache.flink.util.Collector

object WindowProcessApp {
    def main(args: Array[String]): Unit = {
        val environment = StreamExecutionEnvironment.getExecutionEnvironment
        val text = environment.socketTextStream("localhost", 9999)

        import org.apache.flink.api.scala._
        text.flatMap(_.split(","))
            .map(x => (1, x.toInt))     //(1, 2, 3, 4) => (1, 1) (1, 2) (1, 3) (1, 4)
            .keyBy(0)            //因为所有key都是1，所以所有的元素都到一个task中执行
            .timeWindow(Time.seconds(5))
            .process(new ProcessWindowFunction[(Int, Int), String, Tuple, TimeWindow] {
                override def process(key: Tuple, context: Context, elements: Iterable[(Int, Int)], out: Collector[String]): Unit = {
                    println("==================")
                    var count = 0L
                    for (in <- elements) {
                        count = count + 1
                    }
                    out.collect(s"Window ${context.window} count: $count")
                }
            })
            .print()
            .setParallelism(1)

        environment.execute("WindowReduceApp")
    }


}
