package com.imooc.flink.scala.course05

import org.apache.flink.streaming.api.scala.StreamExecutionEnvironment
import org.apache.flink.api.scala._

import scala.collection.immutable.Range.Long

object ScalaDataStreamSourceApp {

    def socketFunction(environment: StreamExecutionEnvironment): Unit = {
        val data = environment.socketTextStream("localhost", 9999)
        data.print().setParallelism(1)

    }

    def parallelSourceFunction(environment: StreamExecutionEnvironment): Unit = {
        val data = environment.addSource(new CustomParallelSourceFunction).setParallelism(2)
        data.print().setParallelism(1)
    }

    def nonParallelSourceFunction(environment: StreamExecutionEnvironment): Unit = {
        val data = environment.addSource(new CustomNonParallelSourceFunction).setParallelism(2)
        data.print().setParallelism(1)
    }

    def richParallelSourceFunction(environment: StreamExecutionEnvironment) = {
        val data = environment.addSource(new CustomRichParallelSourceFunction).setParallelism(2)
        data.print().setParallelism(1)
    }

    def main(args: Array[String]): Unit = {
        val environment = StreamExecutionEnvironment.getExecutionEnvironment
        //socketFunction(environment);
        //nonParallelSourceFunction(environment)
        //parallelSourceFunction(environment)
        richParallelSourceFunction(environment)
        environment.execute("ScalaDataStreamSourceApp")
    }
}
