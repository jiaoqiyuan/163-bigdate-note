package com.imooc.flink.scala.course07

import org.apache.flink.streaming.api.scala.StreamExecutionEnvironment
import org.apache.flink.streaming.api.windowing.time.Time

object WindowReduceApp {
    def main(args: Array[String]): Unit = {
        val environment = StreamExecutionEnvironment.getExecutionEnvironment
        val text = environment.socketTextStream("localhost", 9999)

        import org.apache.flink.api.scala._
        text.flatMap(_.split(","))
            .map(x => (1, x.toInt))     //(1, 2, 3, 4) => (1, 1) (1, 2) (1, 3) (1, 4)
            .keyBy(0)            //因为所有key都是1，所以所有的元素都到一个task中执行
            .timeWindow(Time.seconds(5))
            .reduce((v1, v2) => {
                println(v1 + "...." + v2)   //两两处理数据
                (v1._1, v1._2 + v2._2)
            })
            .print()
            .setParallelism(1)

        environment.execute("WindowReduceApp")
    }
}
