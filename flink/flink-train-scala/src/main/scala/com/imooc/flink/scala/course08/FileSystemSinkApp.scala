package com.imooc.flink.scala.course08


import org.apache.flink.streaming.api.scala.StreamExecutionEnvironment
import org.apache.flink.streaming.connectors.fs.StringWriter
import org.apache.flink.streaming.connectors.fs.bucketing.{BucketingSink, DateTimeBucketer}

object FileSystemSinkApp {
    def main(args: Array[String]): Unit = {
        val environment = StreamExecutionEnvironment.getExecutionEnvironment

        val data = environment.socketTextStream("localhost", 9999)

        data.print().setParallelism(1)

        val filePath = "file:\\D:\\imooc\\新一代大数据计算引擎 Flink从入门到实战-v\\input\\hdfssink"
        val sink = new BucketingSink[String](filePath)
        sink.setBucketer(new DateTimeBucketer[String]("yyyy-MM-dd--HHmm"))
        sink.setWriter(new StringWriter())
        sink.setBatchRolloverInterval(20)

        data.addSink(sink)

        environment.execute("FileSystemSinkApp")
    }
}
