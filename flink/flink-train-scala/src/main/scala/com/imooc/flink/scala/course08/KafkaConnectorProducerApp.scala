package com.imooc.flink.scala.course08

import java.util.Properties

import org.apache.flink.api.common.serialization.SimpleStringSchema
import org.apache.flink.streaming.api.scala.StreamExecutionEnvironment
import org.apache.flink.streaming.connectors.kafka.{FlinkKafkaConsumer, FlinkKafkaProducer}
import org.apache.flink.streaming.util.serialization.KeyedSerializationSchemaWrapper

object KafkaConnectorProducerApp {
    def main(args: Array[String]): Unit = {
        val environment = StreamExecutionEnvironment.getExecutionEnvironment

        val data = environment.socketTextStream("localhost", 9999)
        val properties = new Properties()
        properties.setProperty("bootstrap.servers", "node01:9092")
        val kafkaSink = new FlinkKafkaProducer[String]("pktest",
            new KeyedSerializationSchemaWrapper[String](new SimpleStringSchema()),
            properties)

        data.addSink(kafkaSink)

        environment.execute("KafkaConnectorProducerApp")
    }
}
