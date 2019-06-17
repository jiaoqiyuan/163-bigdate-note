package com.imooc.flink.scala.course08

import java.util.Properties

import org.apache.flink.api.common.serialization.SimpleStringSchema
import org.apache.flink.streaming.api.CheckpointingMode
import org.apache.flink.streaming.api.scala.StreamExecutionEnvironment
import org.apache.flink.streaming.connectors.kafka.FlinkKafkaConsumer

object KafkaConnectorConsumerApp {
    def main(args: Array[String]): Unit = {
        val environment = StreamExecutionEnvironment.getExecutionEnvironment

        //checkpoint 常用设置参数
        environment.enableCheckpointing(4000)
        environment.getCheckpointConfig.setCheckpointingMode(CheckpointingMode.EXACTLY_ONCE)
        environment.getCheckpointConfig.setCheckpointTimeout(10000)
        environment.getCheckpointConfig.setMaxConcurrentCheckpoints(1)

        import org.apache.flink.api.scala._
        val properties = new Properties()
        properties.setProperty("bootstrap.servers", "192.168.60.21:9092")
        val data = environment.addSource(new FlinkKafkaConsumer[String]("pktest", new SimpleStringSchema(), properties))
        data.print()


        environment.execute("KafkaConnectorConsumerApp")
    }
}
