package com.imooc.flink.course04;

import org.apache.flink.api.java.ExecutionEnvironment;
import org.apache.flink.api.java.operators.DataSource;
import org.apache.flink.core.fs.FileSystem;

import java.util.ArrayList;
import java.util.List;

public class JavaDataSetSinkApp {
    public static void main(String[] args) throws Exception {
        ExecutionEnvironment env = ExecutionEnvironment.getExecutionEnvironment();
        List<Integer> info = new ArrayList<>();
        for (int i = 1; i <= 10; i++) {
            info.add(i);
        }

        String filePath = "D:\\imooc\\新一代大数据计算引擎 Flink从入门到实战-v\\input\\sinkout\\out.txt";
        DataSource<Integer> data = env.fromCollection(info);
        data.writeAsText(filePath, FileSystem.WriteMode.OVERWRITE);
        env.execute("JavaDataSetSinkApp");
    }
}
