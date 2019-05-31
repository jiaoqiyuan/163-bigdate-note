package com.imooc.flink.course04;

import org.apache.commons.io.FileUtils;
import org.apache.flink.api.common.functions.RichMapFunction;
import org.apache.flink.api.java.ExecutionEnvironment;
import org.apache.flink.api.java.operators.DataSource;
import org.apache.flink.configuration.Configuration;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class JavaDistributeApp {
    public static void main(String[] args) throws Exception {
        ExecutionEnvironment environment = ExecutionEnvironment.getExecutionEnvironment();
        String filePath = "file:\\D:\\imooc\\新一代大数据计算引擎 Flink从入门到实战-v\\input\\hello.txt";

        //1. 注册一个本地文件
        environment.registerCachedFile(filePath, "java-cf");
        DataSource<String> data = environment.fromElements("hadoop", "spark", "flink", "pyspark", "storm");

        data.map(new RichMapFunction<String, String>() {
            List<String> list = new ArrayList<>();
            @Override
            public void open(Configuration parameters) throws Exception {
                File file = getRuntimeContext().getDistributedCache().getFile("java-cf");
                List<String> lines = FileUtils.readLines(file);
                for (String line : lines) {
                    System.out.println("line: " + line);
                }

            }

            @Override
            public String map(String value) throws Exception {
                return value;
            }
        }).print();
    }
}
