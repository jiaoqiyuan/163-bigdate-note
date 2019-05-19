package com.imooc.flink.course04;

import com.imooc.flink.scala.course04.DBUtils;
import org.apache.flink.api.common.functions.FilterFunction;
import org.apache.flink.api.common.functions.MapFunction;
import org.apache.flink.api.common.functions.MapPartitionFunction;
import org.apache.flink.api.java.ExecutionEnvironment;
import org.apache.flink.api.java.operators.DataSource;
import org.apache.flink.util.Collector;
import scala.Int;

import java.util.ArrayList;
import java.util.List;

public class DataSetTransformationApp {
    public static void main(String[] args) throws Exception {
        ExecutionEnvironment environment = ExecutionEnvironment.getExecutionEnvironment();
        //mapFunction(environment);
        //fileterFunction(environment);
        mapPartitionFunction(environment);
    }

    public static void mapPartitionFunction(ExecutionEnvironment env) throws Exception {
        List<String> list = new ArrayList<String>();
        for (int i = 0; i <= 100; i++) {
            list.add("student" + i);
        }

        DataSource<String> dataSource = env.fromCollection(list).setParallelism(6);
//        dataSource.map(new MapFunction<String, String>() {
//            @Override
//            public String map(String input) throws Exception {
//                String connection = DBUtils.getConnnection();
//                System.out.println("connection = " + connection);
//                DBUtils.returnConnection(connection);
//
//                return input;
//            }
//        }).print();
        dataSource.mapPartition(new MapPartitionFunction<String, String>() {
            @Override
            public void mapPartition(Iterable<String> inputs, Collector<String> collector) throws Exception {
                String connection = DBUtils.getConnnection();
                System.out.println("connection = " + connection);
                DBUtils.returnConnection(connection);

            }
        }).print();
    }


    public static void fileterFunction(ExecutionEnvironment env) throws Exception {
        List<Integer> list = new ArrayList<Integer>();
        for (int i = 0; i <= 10; i++) {
            list.add(i);
        }

        DataSource<Integer> dataSource = env.fromCollection(list);
        dataSource.map(new MapFunction<Integer, Integer>() {
            @Override
            public Integer map(Integer value) throws Exception {
                return value + 1;
            }
        }).filter(new FilterFunction<Integer>() {
            @Override
            public boolean filter(Integer value) throws Exception {
                return value > 5;
            }
        }).print();
    }
    
    public static void mapFunction(ExecutionEnvironment env) throws Exception {
        List<Integer> list = new ArrayList<Integer>();
        for (int i = 0; i <= 10; i++) {
            list.add(i);
        }
        DataSource<Integer> dataSource = env.fromCollection(list);
        dataSource.map(new MapFunction<Integer, Integer>() {
            @Override
            public Integer map(Integer value) throws Exception {
                return value + 1;
            }
        }).print();
    }
}
