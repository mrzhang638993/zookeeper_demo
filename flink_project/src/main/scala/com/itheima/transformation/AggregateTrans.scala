package com.itheima.transformation

import org.apache.flink.api.java.aggregation._
import org.apache.flink.api.scala.{ExecutionEnvironment, _}

/**
 * Aggregate 对元组进行聚集操作，聚集操作的函数包括如下的操作实现:sum,min,max操作实现
 **/
object AggregateTrans {

  def main(args: Array[String]): Unit = {
    // 创建批处理运行环境
    val env: ExecutionEnvironment = ExecutionEnvironment.getExecutionEnvironment
    //  读取数据执行操作
    val sourceData: DataSet[(String, Int)] = env.fromCollection(List(("java", 1), ("java", 1), ("scala", 1)))
    //  使用如下的语句是报错的？要使用aggregate，只能使用字段索引名或索引名称来进行分组groupBy(0)，否则会报一下错误:
    //val value: GroupedDataSet[(String, Int)] = sourceData.groupBy(it=>it._1)
    val value: GroupedDataSet[(String, Int)] = sourceData.groupBy(0)
    val destValue: AggregateDataSet[(String, Int)] = value.aggregate(Aggregations.SUM, 1)
    destValue.print()
  }
}
