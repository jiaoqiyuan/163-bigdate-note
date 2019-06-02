package com.imooc.flink.course05;

import org.apache.flink.configuration.Configuration;
import org.apache.flink.streaming.api.functions.sink.RichSinkFunction;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;

public class SinkToMysql extends RichSinkFunction<Student> {
    Connection connection;
    PreparedStatement pstmt;

    private Connection getConnection() throws Exception {
        Connection conn = null;
        Class.forName("com.mysql.jdbc.Driver");
        String url = "jdbc:mysql://localhost:3306/imooc_flink";
        conn = DriverManager.getConnection(url, "root", "1");
        return conn;
    }

    public void open(Configuration parameters) throws Exception {
        super.open(parameters);

        connection = getConnection();
        String text = "insert into student(id,name,age) values (?,?,?)";
        pstmt = connection.prepareStatement(text);
    }

    public void invoke(Student value, Context context) throws Exception {
        System.out.println("incoke======");
        pstmt.setInt(1, value.getId());
        pstmt.setString(2, value.getName());
        pstmt.setInt(3, value.getAge());

        pstmt.executeUpdate();
    }

    public void close(Connection connection) throws Exception {
        super.close();
        if (pstmt != null) {
            pstmt.close();
        }
        if (connection != null) {
            connection.close();
        }
    }
}
