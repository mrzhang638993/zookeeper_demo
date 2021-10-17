package src.main.scala.moduleMatch

import scala.io.StdIn

//测试scala的match操作语句
object MatchDemo {
  def main(args: Array[String]): Unit = {
    val word: String = StdIn.readLine()
    //match是一个表达式的,表达式对应的是存在返回值的。
    val str: String = word match {
      case "hadoop" => "大数据分布式存储和计算框架"
      case "zookeeper" => "大数据分布式协调服务框架"
      case "spark" => "大数据分布式内存计算框架"
      case _ => "未匹配"
    }
    //对应的match是一个表达式的,存在的是表达式的数值的,对应的可以如下执行
    println(str)
  }
}
