package com.imooc.flink.course04;

import com.imooc.flink.scala.course04.DBUtils;
import org.apache.flink.api.common.functions.*;
import org.apache.flink.api.common.operators.Order;
import org.apache.flink.api.java.ExecutionEnvironment;
import org.apache.flink.api.java.operators.DataSource;
import org.apache.flink.api.java.tuple.Tuple2;
import org.apache.flink.api.java.tuple.Tuple3;
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
        //firstFunction(environment);
        //flatMapFunction(environment);
        //distinctFunction(environment);
        //joinFunction(environment);
        //outerJoinFunction(environment);
        //crossFunction(environment);
    }

    private static void crossFunction(ExecutionEnvironment environment) throws Exception {
        List<String> info1 = new ArrayList<>();
        info1.add("曼联");
        info1.add("曼城");

        List<Integer> info2 = new ArrayList<>();
        info2.add(3);
        info2.add(1);
        info2.add(0);

        DataSource<String> data1 = environment.fromCollection(info1);
        DataSource<Integer> data2 = environment.fromCollection(info2);

        data1.cross(data2).print();
    }

    private static void outerJoinFunction(ExecutionEnvironment env) throws Exception {
        List<Tuple2<Integer, String>> info1 = new ArrayList<Tuple2<Integer, String>>();
        info1.add(new Tuple2<>(1, "hadoop"));
        info1.add(new Tuple2<>(2, "spark"));
        info1.add(new Tuple2<>(3, "flink"));
        info1.add(new Tuple2<>(4, "java"));
        info1.add(new Tuple2<>(5, "Spring"));
        info1.add(new Tuple2<>(6, "Linux"));

        List<Tuple2<Integer, String>> info2 = new ArrayList<Tuple2<Integer, String>>();
        info2.add(new Tuple2<>(1, "hadoop"));
        info2.add(new Tuple2<>(2, "spark"));
        info2.add(new Tuple2<>(3, "flink"));
        info2.add(new Tuple2<>(4, "java"));
        info2.add(new Tuple2<>(5, "Spring"));
        info2.add(new Tuple2<>(7, "Vue"));

        DataSource<Tuple2<Integer, String>> data1 = env.fromCollection(info1);
        DataSource<Tuple2<Integer, String>> data2 = env.fromCollection(info2);

        data1.leftOuterJoin(data2).where(0).equalTo(0).with(new JoinFunction<Tuple2<Integer, String>, Tuple2<Integer, String>, Tuple3<Integer, String, String>>() {
            @Override
            public Tuple3<Integer, String, String> join(Tuple2<Integer, String> first, Tuple2<Integer, String> second) throws Exception {
                if (second == null) {
                    return new Tuple3<>(first.f0, first.f1, "-");
                } else {
                    return new Tuple3<>(first.f0, first.f1, second.f1);
                }
            }
        }).print();

        System.out.println("==============================");

        data1.rightOuterJoin(data2).where(0).equalTo(0).with(new JoinFunction<Tuple2<Integer, String>, Tuple2<Integer, String>, Tuple3<Integer, String, String>>() {
            @Override
            public Tuple3<Integer, String, String> join(Tuple2<Integer, String> first, Tuple2<Integer, String> second) throws Exception {
                if (first == null) {
                    return new Tuple3<>(second.f0, "-", second.f1);
                } else {
                    return new Tuple3<>(first.f0, first.f1, second.f1);
                }
            }
        }).print();

        System.out.println("===============================");

        data1.fullOuterJoin(data2).where(0).equalTo(0).with(new JoinFunction<Tuple2<Integer, String>, Tuple2<Integer, String>, Tuple3<Integer, String, String>>() {
            @Override
            public Tuple3<Integer, String, String> join(Tuple2<Integer, String> first, Tuple2<Integer, String> second) throws Exception {
                if (first == null) {
                    return new Tuple3<>(second.f0, "-", second.f1);
                } else if (second == null) {
                    return new Tuple3<>(first.f0, first.f1, "-");
                } else {
                    return new Tuple3<>(first.f0, first.f1, second.f1);
                }
            }
        }).print();
    }


    public static void joinFunction(ExecutionEnvironment env) throws Exception {
        List<Tuple2<Integer, String>> info1 = new ArrayList<Tuple2<Integer, String>>();
        info1.add(new Tuple2<>(1, "hadoop"));
        info1.add(new Tuple2<>(2, "spark"));
        info1.add(new Tuple2<>(3, "flink"));
        info1.add(new Tuple2<>(4, "java"));
        info1.add(new Tuple2<>(5, "Spring"));
        info1.add(new Tuple2<>(6, "Linux"));

        List<Tuple2<Integer, String>> info2 = new ArrayList<Tuple2<Integer, String>>();
        info2.add(new Tuple2<>(1, "hadoop"));
        info2.add(new Tuple2<>(2, "spark"));
        info2.add(new Tuple2<>(3, "flink"));
        info2.add(new Tuple2<>(4, "java"));
        info2.add(new Tuple2<>(5, "Spring"));
        info2.add(new Tuple2<>(7, "Vue"));

        DataSource<Tuple2<Integer, String>> data1 = env.fromCollection(info1);
        DataSource<Tuple2<Integer, String>> data2 = env.fromCollection(info2);
        
        data1.join(data2).where(0).equalTo(0).with(new JoinFunction<Tuple2<Integer, String>, Tuple2<Integer, String>, Tuple3<Integer, String, String>>() {
            @Override
            public Tuple3<Integer, String, String> join(Tuple2<Integer, String> first, Tuple2<Integer, String> second) throws Exception {
                return new Tuple3<>(first.f0, first.f1, second.f1);
            }
        }).print();
    }

    public static void distinctFunction(ExecutionEnvironment env) throws Exception {
        List<String> info = new ArrayList<>();
        info.add("hadoop,spark");
        info.add("hadoop, flink");
        info.add("flink,flink");
        DataSource<String> data = env.fromCollection(info);
        data.flatMap(new FlatMapFunction<String, String>() {
            @Override
            public void flatMap(String value, Collector<String> collector) throws Exception {
                String[] splits = value.split(",");
                for (String split : splits) {
                    collector.collect(split.trim());
                }
            }
        }).distinct().print();
    }

    public static void flatMapFunction(ExecutionEnvironment env) throws Exception {
        List<String> info = new ArrayList<>();
        info.add("hadoop,spark");
        info.add("hadoop, flink");
        info.add("flink,flink");
        DataSource<String> data = env.fromCollection(info);
        data.flatMap(new FlatMapFunction<String, String>() {
            @Override
            public void flatMap(String value, Collector<String> collector) throws Exception {
                String[] splits = value.split(",");
                for (String split : splits) {
                    collector.collect(split.trim());
                }
            }
        }).map(new MapFunction<String, Tuple2<String, Integer>>() {
            @Override
            public Tuple2<String, Integer> map(String value) throws Exception {
                return new Tuple2<>(value, 1);
            }
        }).groupBy(0).sum(1).print();
    }

    public static void firstFunction(ExecutionEnvironment env) throws Exception {
        List<Tuple2<Integer, String>> info = new ArrayList<Tuple2<Integer, String>>();
        info.add(new Tuple2<>(1, "hadoop"));
        info.add(new Tuple2<>(1, "spark"));
        info.add(new Tuple2<>(1, "flink"));
        info.add(new Tuple2<>(2, "java"));
        info.add(new Tuple2<>(2, "Spring"));
        info.add(new Tuple2<>(3, "Linux"));
        info.add(new Tuple2<>(4, "Vue"));

        DataSource<Tuple2<Integer, String>> data = env.fromCollection(info);
        data.first(3).print();
        System.out.println("======================");
        data.groupBy(0).first(2).print();
        System.out.println("======================");
        data.groupBy(0).sortGroup(1, Order.DESCENDING).first(2).print();

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
                for (String word : inputs) {
                    collector.collect(word);
                }
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
