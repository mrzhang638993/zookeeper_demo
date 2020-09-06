package cn.itcast.spark.rdd

import org.apache.spark.rdd.RDD
import org.apache.spark.{SparkConf, SparkContext}
import org.junit.Test

class Exec3 {

  private val exec: SparkConf = new SparkConf().setMaster("local[2]").setAppName("exec3")
  private val context = new SparkContext(exec)
  context.setCheckpointDir("checkPoint")
  /**
   * 执行exec3的作业任务
   * 算子进行聚合计算出苹果和华为的总售卖数并进行打印
   * */
  @Test
  def exec3(): Unit ={
    val value: RDD[(String, Int)] = context.parallelize(Seq(("苹果", 10), ("苹果", 15), ("华为", 20)))
    //def aggregateByKey[U: ClassTag](zeroValue: U)(seqOp: (U, V) => U,
    //      combOp: (U, U) => U): RDD[(K, U)] = self.withScope {
    //    aggregateByKey(zeroValue, defaultPartitioner(self))(seqOp, combOp)
    //  }
    // 最终返回的是u，v代表的是item。
    //seqOp 合并的是同一个分区中的数值,combOp合并的是不同分区中的数的 前面的数据代表的是返回的数值的，其中u以及v的数据类型是一直的
    value.map(item=>(item._1,(item._2,1)))
      // 更加key进行聚合操作
      .aggregateByKey((0.0,0.0))((item,zeroValue)=>((zeroValue._1+item._1,zeroValue._2+item._2)),(items,zeroValues)=>(zeroValues._1+items._1,zeroValues._2+items._2))
      .map(item=>(item._1,item._2._1/item._2._2)).collect().foreach(println(_))
    context.stop()
  }

  /**
   * 答案是错误的。需要识别和侦测一下。
   * List((苹果,150.0), (华为,20.0))  local
   * List((苹果,150.0), (华为,20.0)) local[1]
   * List((苹果,150.0), (华为,20.0))
   * List((苹果,150.0), (华为,20.0))  local[1]
   * List((华为,20.0), (苹果,25.0))  local[2]
   * */
@Test
  def  testError(): Unit ={
    val rdd = context.parallelize(Seq(("苹果", 10), ("苹果", 15), ("华为", 20)))
    val result = rdd.aggregateByKey(1.0)(
      seqOp = (zero, price) => price * zero,
      combOp = (curr, agg) => curr + agg
    ).collect()
    context.stop()
  }
}
