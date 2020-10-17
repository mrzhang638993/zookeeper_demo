package com.itheima.transformation

import org.apache.flink.api.scala.{ExecutionEnvironment, _}

object ReduceTrans {
  /**
   * 测试单个对象的转换操作实现和管理的
   **/
  def main(args: Array[String]): Unit = {
    // 创建批处理运行环境
    val env: ExecutionEnvironment = ExecutionEnvironment.getExecutionEnvironment
    val reduceOriginData: DataSet[(String, Int)] = env.fromCollection(List(("java", 1), ("java", 1), ("java", 1)))
    //  聚合操作对应的是将多个元素聚合成为一个元素进行操作实现的。
    val sinkData: DataSet[(String, Int)] = reduceOriginData.reduce((priv, next) => (priv._1, priv._2 + next._2))
    sinkData.print()
  }
}
