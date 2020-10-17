package com.itheima.transformation

import org.apache.flink.api.common.functions.RichMapFunction
import org.apache.flink.api.scala.{DataSet, ExecutionEnvironment, _}

/**
 * rebalance操作:解决数据倾斜的问题和操作实现
 * 使用rebalance可以使用轮询的方式将数据均匀的分配到不同的分区上的。
 **/
object RebalanceTrans {

  def main(args: Array[String]): Unit = {
    // 生成执行环境
    val env: ExecutionEnvironment = ExecutionEnvironment.getExecutionEnvironment
    val rangeValue: DataSet[Long] = env.generateSequence(0, 100)
    // 过滤出来大于8的数字执行操作实现的.使用rebalance可以均匀分配数据和实现
    val filterValue: DataSet[Long] = rangeValue.filter(_ > 8).rebalance()
    /*  val mapDataSetValue: DataSet[(Int, Long)] = filterValue.map(new RichMapFunction[Long, (Int, Long)] {
        override def map(value: Long): (Int, Long) = {
          (getRuntimeContext.getIndexOfThisSubtask, value)
        }
      })
      mapDataSetValue.print()*/
    //从打印出来的结果显示，数据存在倾斜的问题存在的。
    //下面使用rebalance进行均匀分区数据
    val mapDataValue: DataSet[(Int, Long)] = filterValue.rebalance().map(new RichMapFunction[Long, (Int, Long)] {
      override def map(value: Long): (Int, Long) = {
        (getRuntimeContext.getIndexOfThisSubtask, value)
      }
    })
    mapDataValue.print()
  }
}
