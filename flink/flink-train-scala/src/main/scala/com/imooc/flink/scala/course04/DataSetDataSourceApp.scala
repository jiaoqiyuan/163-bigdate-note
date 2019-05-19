package com.imooc.flink.scala.course04

import com.imooc.flink.course04.Person
import org.apache.flink.api.scala.ExecutionEnvironment
import org.apache.flink.configuration.Configuration

object DataSetDataSourceApp {
    def main(args: Array[String]): Unit = {
        val env = ExecutionEnvironment.getExecutionEnvironment
//        fromCollection(env)
//        textFile(env)
//        csvFile(env)
//        readRecursiveFiles(env)
        readGzipFile(env)

    }

    //从压缩文件中读取
    def readGzipFile(env: ExecutionEnvironment): Unit = {
        val input = "file:///D:/imooc/新一代大数据计算引擎 Flink从入门到实战-v/input/compression"
        //gzip文件可以直接读取，自动识别
        env.readTextFile(input).print()
    }

    //读取递归文件
    def readRecursiveFiles(env: ExecutionEnvironment): Unit = {
        val input = "file:///D:/imooc/新一代大数据计算引擎 Flink从入门到实战-v/input/"
        env.readTextFile(input).print()
        println("-----------------")
        val configuration = new Configuration()
        configuration.setBoolean("recursive.file.enumeration", true)
        env.readTextFile(input).withParameters(configuration).print()
    }

    def csvFile(env: ExecutionEnvironment): Unit = {
        import org.apache.flink.api.scala._
        val input = "file:///D:/imooc/新一代大数据计算引擎 Flink从入门到实战-v/input/people.csv"
        //读取csv文件
        //env.readCsvFile[(String, Integer, String)](input, ignoreFirstLine = true).print()

        //读取后两个字段
        //env.readCsvFile[(Int, String)](input, ignoreFirstLine = true, includedFields = Array(1, 2)).print()

        //使用case class读取
        //case class MyCaseClass(name: String, age: Integer)
        //env.readCsvFile[MyCaseClass](input, ignoreFirstLine = true, includedFields = Array(0, 1)).print()

        //使用Java 的POJO方式读取
        env.readCsvFile[Person](input, ignoreFirstLine = true, pojoFields = Array("name", "age", "work")).print()
    }

    def textFile(env: ExecutionEnvironment): Unit = {
        val input = "file:///D:/imooc/新一代大数据计算引擎 Flink从入门到实战-v/input/hello.txt"
        env.readTextFile(input).print()
    }

    def fromCollection(env: ExecutionEnvironment): Unit = {
        import org.apache.flink.api.scala._
        val data = 1 to 10
        env.fromCollection(data).print()
    }
}
