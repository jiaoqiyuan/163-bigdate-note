package com.imooc.flink.scala.course04

import org.apache.flink.api.scala.ExecutionEnvironment

import scala.collection.mutable.ListBuffer

object DataSetTransformationApp {
    def main(args: Array[String]): Unit = {
        val env = ExecutionEnvironment.getExecutionEnvironment
        //mapFunction(env)
        //filterFunction(env)
        mapPartitionFunction(env)
    }

    def mapPartitionFunction(env: ExecutionEnvironment): Unit = {
        val students = new ListBuffer[String]
        for (i <- 1 to 100) {
            students.append("student: " + i)
        }

        import org.apache.flink.api.scala._
        val data = env.fromCollection(students).setParallelism(4)
//        data.map(x => {
//            //获取Connection
//            val connection = DBUtils.getConnnection()
//            println(connection + "...")
//
//            //保存数据到DB
//            DBUtils.returnConnection(connection)
//        }).print()
        data.mapPartition(x => {
            val connection = DBUtils.getConnnection()
            println(connection + "....")
            DBUtils.returnConnection(connection)
            x
        }).print()
    }

    def filterFunction(env: ExecutionEnvironment): Unit = {
        import org.apache.flink.api.scala._
        //链式编程
        env.fromCollection(List(1, 2, 3, 4, 5, 6, 7, 8, 9, 10))
            .map(_ + 1)
            .filter(_ > 5)
            .print()
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
