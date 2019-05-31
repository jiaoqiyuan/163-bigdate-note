package com.imooc.flink.course05;

import org.apache.flink.streaming.api.datastream.DataStreamSource;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;

public class JavaDataStreamSourceApp {
    public static void main(String[] args) throws Exception {
        StreamExecutionEnvironment environment = StreamExecutionEnvironment.getExecutionEnvironment();
        //socketFunction(environment);
        //nonParallelSourceFunction(environment);
        //parallelSourceFunction(environment);
        richParallelSourceFunction(environment);
        environment.execute("JavaDataStreamSourceApp");
    }

    public static void socketFunction(StreamExecutionEnvironment environment) {
        DataStreamSource<String> dataStreamSource = environment.socketTextStream("localhost", 9999);
        dataStreamSource.print().setParallelism(1);

    }

    public static void nonParallelSourceFunction(StreamExecutionEnvironment environment) {
        DataStreamSource<Long> dataStreamSource = environment.addSource(new JavaCustomNonParallelSourceFunction());
        dataStreamSource.print().setParallelism(1);
    }

    public static void parallelSourceFunction(StreamExecutionEnvironment environment) {
        DataStreamSource<Long> dataStreamSource = environment.addSource(new JavaCustomParallelSourceFunction())
                .setParallelism(1);
        dataStreamSource.print().setParallelism(1);
    }

    public static void richParallelSourceFunction(StreamExecutionEnvironment environment) {
        DataStreamSource<Long> dataStreamSource = environment.addSource(new JavaCustomRichParallelSourceFunction())
                .setParallelism(1);
        dataStreamSource.print().setParallelism(1);
    }
}
