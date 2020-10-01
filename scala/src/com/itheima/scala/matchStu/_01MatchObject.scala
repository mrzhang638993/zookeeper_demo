package com.itheima.scala.matchStu

import scala.io.StdIn

object _01MatchObject {

  def main(args: Array[String]): Unit = {
    //  从控制台获取输入文本
    val str = StdIn.readLine()
    //  使用模式匹配完成match的相关的匹配操作的
    val str1 = str match {
      case "hadoop" => "大数据分布式计算和存储框架"
      case "zookeeper" => "大数据分布式协调框架"
      case "spark" => "大数据分布式内存计算框架"
      case _ => "未匹配到任何的内容"
    }
    println(str1)
  }
}
