package com.imooc.flink.course06;

import org.apache.flink.api.java.DataSet;
import org.apache.flink.api.java.ExecutionEnvironment;
import org.apache.flink.api.java.operators.DataSource;
import org.apache.flink.table.api.Table;
import org.apache.flink.table.api.java.BatchTableEnvironment;
import org.apache.flink.types.Row;

public class JavaTableSQLAPI {
    public static void main(String[] args) throws Exception {
        ExecutionEnvironment environment = ExecutionEnvironment.getExecutionEnvironment();
        BatchTableEnvironment tableEnvironment = BatchTableEnvironment.getTableEnvironment(environment);
        String filepath = "file:\\D:\\imooc\\新一代大数据计算引擎 Flink从入门到实战-v\\input\\sales.csv";
        //csv => DataSet
        DataSource<Sales> csv = environment.readCsvFile(filepath)
                .ignoreFirstLine()
                .pojoType(Sales.class, "transactionId", "customerId", "itemId", "amountPaid");
        //csv.print();

        Table sales = tableEnvironment.fromDataSet(csv);
        tableEnvironment.registerTable("sales", sales);
        Table resultTable = tableEnvironment.sqlQuery("select customerId, sum(amountPaid) money from sales group by customerId");

        DataSet<Row> result = tableEnvironment.toDataSet(resultTable, Row.class);
        result.print();
    }

    public static class Sales {
        public String transactionId;
        public String customerId;
        public String itemId;
        public Double amountPaid;
    }
}
