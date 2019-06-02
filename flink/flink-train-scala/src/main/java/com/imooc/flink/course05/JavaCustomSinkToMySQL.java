package com.imooc.flink.course05;

import org.apache.flink.api.common.functions.MapFunction;
import org.apache.flink.streaming.api.datastream.DataStreamSource;
import org.apache.flink.streaming.api.datastream.SingleOutputStreamOperator;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;

public class JavaCustomSinkToMySQL {
    public static void main(String[] args) throws Exception {
        StreamExecutionEnvironment environment = StreamExecutionEnvironment.getExecutionEnvironment();

        DataStreamSource<String> source = environment.socketTextStream("localhost", 7777);
        SingleOutputStreamOperator<Student> studentString = source.map(new MapFunction<String, Student>() {
            @Override
            public Student map(String value) throws Exception {
                Student student = new Student();
                String[] strings = value.split(",");
                student.setId(Integer.parseInt(strings[0]));
                student.setName(strings[1]);
                student.setAge(Integer.parseInt(strings[2]));
                return student;
            }
        });

        studentString.addSink(new SinkToMysql());

        environment.execute("JavaCustomSinkToMySQL");
    }
}
