package com.imooc.flink.course05;

import org.apache.flink.api.common.functions.FilterFunction;
import org.apache.flink.api.common.functions.MapFunction;
import org.apache.flink.streaming.api.collector.selector.OutputSelector;
import org.apache.flink.streaming.api.datastream.DataStreamSource;
import org.apache.flink.streaming.api.datastream.SplitStream;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;

import java.util.ArrayList;
import java.util.List;

public class JavaDataStreamTransformationApp {
    public static void main(String[] args) throws Exception {
        StreamExecutionEnvironment environment = StreamExecutionEnvironment.getExecutionEnvironment();

        //filterFunction(environment);
        //unionFunciton(environment);
        splitFunction(environment);
        environment.execute("JavaDataStreamTransformationApp");
    }

    private static void splitFunction(StreamExecutionEnvironment environment) {
        DataStreamSource<Long> dataStreamSource = environment.addSource(new JavaCustomNonParallelSourceFunction());

        SplitStream<Long> splitStream = dataStreamSource.split(new OutputSelector<Long>() {
            @Override
            public Iterable<String> select(Long value) {
                List<String> list = new ArrayList<>();
                if (value % 2 == 0) {
                    list.add("event");
                } else {
                    list.add("odd");
                }
                return list;
            }
        });

        splitStream.select("odd").print().setParallelism(1);
    }

    private static void unionFunciton(StreamExecutionEnvironment environment) {
        DataStreamSource<Long> dataStreamSource1 = environment.addSource(new JavaCustomNonParallelSourceFunction());
        DataStreamSource<Long> dataStreamSource2 = environment.addSource(new JavaCustomNonParallelSourceFunction());

        dataStreamSource1.union(dataStreamSource2).print().setParallelism(1);
    }

    private static void filterFunction(StreamExecutionEnvironment environment) {
        DataStreamSource<Long> dataStreamSource = environment.addSource(new JavaCustomNonParallelSourceFunction());
        dataStreamSource.map(new MapFunction<Long, Long>() {
            @Override
            public Long map(Long value) throws Exception {
                System.out.println("value:" + value);
                return value;
            }
        }).filter(new FilterFunction<Long>() {
            @Override
            public boolean filter(Long value) throws Exception {
                return (value % 2 == 0);
            }
        }).print().setParallelism(1);
    }
}
