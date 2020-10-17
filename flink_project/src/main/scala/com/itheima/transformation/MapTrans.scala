package com.itheima.transformation

import org.apache.flink.api.scala.{ExecutionEnvironment, _}

object MapTrans {
  /**
   * flink的map转换操作实现
   **/
  def main(args: Array[String]): Unit = {
    // 生成执行环境
    val env: ExecutionEnvironment = ExecutionEnvironment.getExecutionEnvironment
    val listDataset: DataSet[String] = env.fromCollection(List[String]("1,张三", "2,李四", "3,王五", "4,赵六"))
    val persons: DataSet[Person] = listDataset.map {
      text => {
        val arr: Array[String] = text.split(",")
        Person(arr(0).toInt, arr(1))
      }
    }
    persons.print()
  }
}

case class Person(id: Int, name: String)

