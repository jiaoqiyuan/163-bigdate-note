package com.imooc.flink.scala.course05

import java.{lang, util}

import org.apache.flink.streaming.api.collector.selector.OutputSelector
import org.apache.flink.streaming.api.scala.StreamExecutionEnvironment

object DataStreamTransformationApp {

    def unionFunction(environment: StreamExecutionEnvironment) = {
        import org.apache.flink.api.scala._
        val data1 = environment.addSource(new CustomNonParallelSourceFunction)
        val data2 = environment.addSource(new CustomNonParallelSourceFunction)

        data1.union(data2).print().setParallelism(1)
    }

    def splitFunction(environment: StreamExecutionEnvironment) = {
        import org.apache.flink.api.scala._
        val data = environment.addSource(new CustomNonParallelSourceFunction)

        val splits = data.split(new OutputSelector[Long] {
            override def select(value: Long): lang.Iterable[String] = {
                val list = new util.ArrayList[String]()
                if (value % 2 == 0) {
                    list.add("event")
                } else {
                    list.add("odd")
                }
                list
            }
        })

        splits.select("odd", "event").print().setParallelism(1)
    }

    def main(args: Array[String]): Unit = {
        val environment = StreamExecutionEnvironment.getExecutionEnvironment
        //filterFunction(environment)
        //unionFunction(environment)
        splitFunction(environment)
        environment.execute("JavaDataStreamTransformationApp")
    }

    def filterFunction(environment: StreamExecutionEnvironment): Unit = {
        import org.apache.flink.api.scala._
        val data = environment.addSource(new CustomNonParallelSourceFunction)
        data.map(x => {
            println("x:" + x)
            x
        }).filter(_ %2 == 0).print().setParallelism(1)
    }
}
