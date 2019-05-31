package com.imooc.flink.scala.course04

import org.apache.flink.api.scala.ExecutionEnvironment
import org.apache.flink.core.fs.FileSystem.WriteMode

object DataSetSinkApp {
    def main(args: Array[String]): Unit = {
        val env = ExecutionEnvironment.getExecutionEnvironment

        import org.apache.flink.api.scala._
        val text = env.fromCollection(1 to 10)
        val filePath = "D:\\imooc\\新一代大数据计算引擎 Flink从入门到实战-v\\input\\sinkout\\out.txt"
        text.writeAsText(filePath, WriteMode.OVERWRITE).setParallelism(2)
        env.execute("DataSetSinkApp")
    }
}
