package com.imooc.flink.scala.course06

import org.apache.flink.api.scala.ExecutionEnvironment
import org.apache.flink.table.api.TableEnvironment
import org.apache.flink.types.Row

object TableSQLAPI {
    def main(args: Array[String]): Unit = {
        val environment = ExecutionEnvironment.getExecutionEnvironment
        val tableEnvironment = TableEnvironment.getTableEnvironment(environment)
        import org.apache.flink.api.scala._
        val filepath = "file:\\D:\\imooc\\新一代大数据计算引擎 Flink从入门到实战-v\\input\\sales.csv"
        val csv = environment.readCsvFile[SaleLog](filepath, ignoreFirstLine = true)
        csv.print()

        // DataSet => Table
        val csvTable = tableEnvironment.fromDataSet(csv)

        // Table => table
        tableEnvironment.registerTable("sales", csvTable)

        val resultTable = tableEnvironment.sqlQuery("select customerId, sum(amountPaid) money from sales group by customerId")
        tableEnvironment.toDataSet[Row](resultTable).print()
    }

    case class SaleLog(transactionId: String, customerId: String, itemId:String, amountPaid: Double)
}
