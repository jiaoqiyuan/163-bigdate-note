package com.imooc.flink.java.course02;

import org.apache.flink.api.common.functions.FlatMapFunction;
import org.apache.flink.api.java.ExecutionEnvironment;
import org.apache.flink.api.java.operators.DataSource;
import org.apache.flink.api.java.tuple.Tuple2;
import org.apache.flink.util.Collector;

public class BatchWCJavaApp {
    public static void main(String[] args) throws Exception {
        String input = "file:///D:\\imooc\\新一代大数据计算引擎 Flink从入门到实战-v\\input";
        ExecutionEnvironment env = ExecutionEnvironment.getExecutionEnvironment();
        DataSource<String> text = env.readTextFile(input);
//        text.print();
        text.flatMap(new FlatMapFunction<String, Tuple2<String, Integer>>() {
            @Override
            public void flatMap(String value, Collector<Tuple2<String, Integer>> collector) throws Exception {
                String[] tokens = value.toLowerCase().split(",");
                for (String token : tokens) {
                    if (token.length() > 0) {
                        collector.collect(new Tuple2<String, Integer>(token, 1));
                    }
                }
            }

        }).groupBy(0).sum(1).print();
    }
}
