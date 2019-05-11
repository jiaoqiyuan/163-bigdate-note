package com.imooc.spark

import org.apache.spark.sql.SparkSession

/**
  * DataFrame 其他操作
  */
object DataFrameCase {
    def main(args: Array[String]): Unit = {
        val spark = SparkSession.builder().appName("DataFrameCase").master("local[2]").getOrCreate()
        val rdd = spark.sparkContext.textFile("/home/jony/resource/data/student.data")

        import spark.implicits._
        val studentDF = rdd.map(_.split("\\|")).map(
            line => Student(
                line(0).toInt,
                line(1),
                line(2),
                line(3))).toDF()

//        studentDF.show(30, false)
//        studentDF.take(10).foreach(println)
//        studentDF.head(3).foreach(println)
//        studentDF.select("email", "name").show(30, false)
//        studentDF.filter("name=='' OR name == 'NULL'").show(30, false)

        //找出以名字以m开头的人
//        studentDF.filter("substr(name, 0, 1) = 'M'").show()

        //sort
//        studentDF.sort("name", "id").show
//        studentDF.sort(studentDF("name"), studentDF("id").desc).show

//        studentDF.select(studentDF("name").as("Student_name")).show()

        val studentDF2 = rdd.map(_.split("\\|")).map(
            line => Student(
                line(0).toInt,
                line(1),
                line(2),
                line(3))).toDF()

        //join
        studentDF.join(studentDF2, studentDF.col("id") === studentDF2.col("id")).show()

        spark.stop()
    }

    case class Student(id: Int, name: String, phone: String, email: String)
}
