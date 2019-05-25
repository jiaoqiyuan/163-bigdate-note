package com.imooc.flink.scala.course04

import org.apache.flink.api.common.operators.Order
import org.apache.flink.api.scala.ExecutionEnvironment

import scala.collection.mutable.ListBuffer

object DataSetTransformationApp {
    def main(args: Array[String]): Unit = {
        val env = ExecutionEnvironment.getExecutionEnvironment
        //mapFunction(env)
        //filterFunction(env)
        //mapPartitionFunction(env)
        //firstFunction(env)
        //flatMapFunction(env)
        //distinctFunction(env)
        //joinFunction(env)
        outerJoinFunction(env)
    }

    def outerJoinFunction(env: ExecutionEnvironment): Unit ={
        val info1 = ListBuffer[(Int, String)]()
        info1.append((1, "hadoop"))
        info1.append((2, "spark"))
        info1.append((3, "flink"))
        info1.append((4, "java"))
        info1.append((5, "Spring"))
        info1.append((6, "Linux"))

        val info2 = ListBuffer[(Int, String)]()
        info2.append((1, "beijing"))
        info2.append((2, "shanghai"))
        info2.append((3, "chengdu"))
        info2.append((4, "guangzhou"))
        info2.append((5, "hangzhou"))
        info2.append((7, "shenzhen"))

        import org.apache.flink.api.scala._
        val data1 = env.fromCollection(info1)
        val data2 = env.fromCollection(info2)

        data1.leftOuterJoin(data2).where(0).equalTo(0).apply((first, second) =>{
            if (second == null) {
                (first._1, first._2, "-")
            } else {
                (first._1, first._2, second._2)
            }
        }).print()

        println("==========================")

        data1.rightOuterJoin(data2).where(0).equalTo(0).apply((first, second) =>{
            if (first == null) {
                (second._1, "-", second._2)
            } else {
                (first._1, first._2, second._2)
            }
        }).print()

        println("===========================")

        data1.fullOuterJoin(data2).where(0).equalTo(0).apply((first, second) =>{
            if (first == null) {
                (second._1, "-", second._2)
            } else if (second == null){
                (first._1, first._2, "-")
            } else {
                (first._1, first._2, second._2)
            }
        }).print()
    }

    def joinFunction(env: ExecutionEnvironment): Unit = {
        val info1 = ListBuffer[(Int, String)]()
        info1.append((1, "hadoop"))
        info1.append((2, "spark"))
        info1.append((3, "flink"))
        info1.append((4, "java"))
        info1.append((5, "Spring"))
        info1.append((6, "Linux"))

        val info2 = ListBuffer[(Int, String)]()
        info2.append((1, "beijing"))
        info2.append((2, "shanghai"))
        info2.append((3, "chengdu"))
        info2.append((4, "guangzhou"))
        info2.append((5, "hangzhou"))
        info2.append((7, "shenzhen"))

        import org.apache.flink.api.scala._
        val data1 = env.fromCollection(info1)
        val data2 = env.fromCollection(info2)

        data1.join(data2).where(0).equalTo(0).apply((first, second) =>{
            (first._1, first._2, second._2)
        }).print()
    }

    def distinctFunction(env: ExecutionEnvironment): Unit = {
        val info = ListBuffer[String]()
        info.append("hadoop,spark")
        info.append("hadoop,flink")
        info.append("flink,flink")
        info.append("flink,flink")

        import org.apache.flink.api.scala._
        val data = env.fromCollection(info)
        data.flatMap(_.split(",")).distinct().print()
    }

    def flatMapFunction(env: ExecutionEnvironment): Unit = {
        val info = ListBuffer[String]()
        info.append("hadoop,spark")
        info.append("hadoop,flink")
        info.append("flink,flink")
        info.append("flink,flink")
        import org.apache.flink.api.scala._
        val data = env.fromCollection(info)
        //data.map(_.split(",")).print()
        //data.flatMap(_.split(",")).print()
        data.flatMap(_.split(",")).map((_, 1)).groupBy(0).sum(1).print()
    }

    def firstFunction(env:ExecutionEnvironment): Unit ={
        val info = ListBuffer[(Int, String)]()
        info.append((1, "hadoop"))
        info.append((1, "spark"))
        info.append((1, "flink"))
        info.append((2, "java"))
        info.append((2, "Spring"))
        info.append((3, "Linux"))
        info.append((4, "Vue"))

        import org.apache.flink.api.scala._
        val data = env.fromCollection(info)
        //data.first(4).print()
        //println("============")
        //data.groupBy(0).first(2).print()
        data.groupBy(0).sortGroup(1, Order.ASCENDING).first(2).print()
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
