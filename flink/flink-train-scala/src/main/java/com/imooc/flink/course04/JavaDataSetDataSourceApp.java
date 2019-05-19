package com.imooc.flink.course04;

import org.apache.flink.api.java.ExecutionEnvironment;

import java.util.ArrayList;
import java.util.List;

public class JavaDataSetDataSourceApp {
    public static void main(String[] args) throws Exception {
        ExecutionEnvironment environment = ExecutionEnvironment.getExecutionEnvironment();
//        fromCollection(environment);
        textFile(environment);
    }


    public static void textFile(ExecutionEnvironment env) throws Exception {
        String input = "file:///D:/imooc/新一代大数据计算引擎 Flink从入门到实战-v/input/hello.txt";
        env.readTextFile(input).print();
    }

    public static void fromCollection(ExecutionEnvironment env) throws Exception {
        List<Integer> list = new ArrayList<>();
        for (int i = 0; i <= 10; i++) {
            list.add(i);
        }
        env.fromCollection(list).print();
    }
}
