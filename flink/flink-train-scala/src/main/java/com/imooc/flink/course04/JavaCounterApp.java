package com.imooc.flink.course04;

import org.apache.flink.api.common.JobExecutionResult;
import org.apache.flink.api.common.accumulators.LongCounter;
import org.apache.flink.api.common.functions.RichMapFunction;
import org.apache.flink.api.java.ExecutionEnvironment;
import org.apache.flink.api.java.operators.DataSource;
import org.apache.flink.configuration.Configuration;
import org.apache.flink.core.fs.FileSystem;

public class JavaCounterApp {
    public static void main(String[] args) throws Exception {
        ExecutionEnvironment env = ExecutionEnvironment.getExecutionEnvironment();
        DataSource<String> data = env.fromElements("hadoop", "spark", "flink", "strom", "pyspark");

        data.map(new RichMapFunction<String, String>() {
            LongCounter counter = new LongCounter();

            @Override
            public void open(Configuration parameters) throws Exception {
                super.open(parameters);
                getRuntimeContext().addAccumulator("ele_counter_java", counter);
            }

            @Override
            public String map(String value) throws Exception {
                counter.add(1);
                return value;
            }
        }).writeAsText("file:\\D:\\imooc\\新一代大数据计算引擎 Flink从入门到实战-v\\input\\sinkout\\sink-java-counter.txt",
                FileSystem.WriteMode.OVERWRITE).setParallelism(3);

        JobExecutionResult counterApp = env.execute("JavaCounterApp");
        Long num = counterApp.getAccumulatorResult("ele_counter_java");
        System.out.println("num:" + num);
    }
}
