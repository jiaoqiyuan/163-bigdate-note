package com.imooc.flink.scala.course04

import scala.util.Random

object DBUtils {
    def getConnnection() = {
        new Random().nextInt(10) + ""
    }

    def returnConnection(connection: String) = {

    }
}
