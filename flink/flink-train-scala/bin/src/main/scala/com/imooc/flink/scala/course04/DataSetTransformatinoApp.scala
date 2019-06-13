package com.imooc.flink.scala.course04

import org.apache.flink.api.scala.ExecutionEnvironment

object DataSetTransformatinoApp {
    def main(args: Array[String]): Unit = {
        val env = ExecutionEnvironment.getExecutionEnvironment
        mapFunction(env)
    }

    def mapFunction(env: ExecutionEnvironment): Unit = {
        import org.apache.flink.api.scala._
        val data = env.fromCollection(List(1, 2, 3, 4, 5, 6, 7, 8, 9, 10))
        data.print()
        println("--------------")
        //data.map((x: Int) => x * 2).print()
        //data.map((x) => x * 2).print()
        //data.map(x => x * 2).print()
        data.map(_*2).print()
    }
}
