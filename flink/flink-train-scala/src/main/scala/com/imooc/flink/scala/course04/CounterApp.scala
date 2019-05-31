package com.imooc.flink.scala.course04

import org.apache.flink.api.common.accumulators.LongCounter
import org.apache.flink.api.common.functions.RichMapFunction
import org.apache.flink.api.scala.ExecutionEnvironment
import org.apache.flink.configuration.Configuration
import org.apache.flink.core.fs.FileSystem.WriteMode

object CounterApp {
    def main(args: Array[String]): Unit = {
        val env = ExecutionEnvironment.getExecutionEnvironment
        import org.apache.flink.api.scala._
        val data = env.fromElements("hadoop", "spark", "flink", "strom", "pyspark")

//        data.map(new RichMapFunction[String, Integer] {
//            var count = 0
//            override def map(value: String): Integer = {
//                count = count + 1
//                println("counter:" + count)
//                count
//            }
//        }).setParallelism(100).print()

        data.map(new RichMapFunction[String, String] {
            //1.定义计数器
            val counter = new LongCounter()

            //2.注册计数器
            override def open(parameters: Configuration): Unit = {
                getRuntimeContext.addAccumulator("ele_counts_scala", counter)
            }
            override def map(value: String): String = {
                counter.add(1)
                value
            }
        }).writeAsText("file:\\D:\\imooc\\新一代大数据计算引擎 Flink从入门到实战-v\\input\\sinkout\\sink-scala-counter.txt",
            WriteMode.OVERWRITE).setParallelism(3)

        val jobResult = env.execute("CounterApp")

        //3.获取计数器
        val num = jobResult.getAccumulatorResult[Long]("ele_counts_scala")
        println("num:" + num)
    }
}
