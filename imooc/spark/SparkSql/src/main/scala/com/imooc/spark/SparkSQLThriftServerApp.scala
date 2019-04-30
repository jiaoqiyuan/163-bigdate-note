package com.imooc.spark

import java.sql.DriverManager

/**
  * deal with hvie data through jdbc
  */
object SparkSQLThriftServerApp {
    def main(args: Array[String]): Unit = {
        //get driver
        Class.forName("org.apache.hive.jdbc.HiveDriver")

        //get connection
        val connection = DriverManager.getConnection("jdbc:hive2://192.168.8.170:10000", "jony", "")

        //execute sql
        val pstmt = connection.prepareStatement("select empno, ename, sal from emp")
        val rs = pstmt.executeQuery()
        while (rs.next()) {
            println("empno:" + rs.getInt("empno") + ", ename:" + rs.getString("ename")
                    + ", sal:" + rs.getDouble("sal"))
        }

        //close connection
        rs.close()
        pstmt.close()
        connection.close()
    }
}
