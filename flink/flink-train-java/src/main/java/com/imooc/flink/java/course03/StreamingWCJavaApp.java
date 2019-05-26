package com.imooc.flink.java.course03;

import org.apache.flink.api.common.functions.FlatMapFunction;
import org.apache.flink.api.java.tuple.Tuple;
import org.apache.flink.api.java.tuple.Tuple2;
import org.apache.flink.streaming.api.datastream.DataStreamSource;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.windowing.time.Time;
import org.apache.flink.util.Collector;

/**
 * 使用java开发Flink实时处理应用程序
 */
public class StreamingWCJavaApp {
    public static void main(String[] args) throws Exception {
        String input = "file:///D:/imooc/新一代大数据计算引擎 Flink从入门到实战-v/input";
        
        //获取执行环境
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();

        //读取数据
        DataStreamSource<String> dataStreamSource = env.socketTextStream("localhost", 9999);

        //执行转换操作
        dataStreamSource.flatMap(new FlatMapFunction<String, WC>() {
            @Override
            public void flatMap(String value, Collector<WC> collector) throws Exception {
                String[] tokens = value.toLowerCase().split(",");
                for (String token : tokens) {
                    if (token.length() > 0) {
                        collector.collect(new WC(token, 1));
                    }
                }
            }
        }).keyBy("word")
                .timeWindow(Time.seconds(5))
                .sum("count")
                .setParallelism(1)
                .print();

        env.execute("StreamingWCJavaApp");
    }

    public static class WC {
        private String word;
        private int count;

        public WC() {

        }

        public WC (String word, int count) {
            this.word = word;
            this.count = count;
        }

        public String toString() {
            return "WC { word = '" + word + "'" + ", count = " + count + " }";
        }

        public String getWord() {
            return word;
        }

        public void setWord(String word) {
            this.word = word;
        }

        public int getCount() {
            return count;
        }

        public void setCount(int count) {
            this.count = count;
        }
    }
}
