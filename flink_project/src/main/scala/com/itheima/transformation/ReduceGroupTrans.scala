package com.itheima.transformation

import org.apache.flink.api.scala.{ExecutionEnvironment, _}

/**
 * 对group的数据进行聚合操作实现
 * reduceGroup:对应的首先在group内部进行聚合计算操作，然后传递数据进行操作实现。
 * reduce操作对应的是在数据传输之后进行reduce操作实现。
 **/
object ReduceGroupTrans {

  def main(args: Array[String]): Unit = {
    // 创建批处理运行环境
    val env: ExecutionEnvironment = ExecutionEnvironment.getExecutionEnvironment
    // 执行reduceGroup操作实现
    val sourceDataset: DataSet[(String, Int)] = env.fromCollection(List(("java", 1), ("java", 1), ("java", 1)))
    // 执行数据操作实现
    val sinkDataset: DataSet[(String, Int)] = sourceDataset.reduceGroup(itertable => {
      // 对应的是对于组内的数据进行聚合操作实现的。
      itertable.reduce((priv, next) => (priv._1, priv._2 + next._2))
    })
    sinkDataset.print()
  }
}
