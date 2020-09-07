package cn.itcast.spark.rdd

import org.apache.spark.rdd.RDD
import org.apache.spark.{SparkConf, SparkContext}
import org.junit.Test

class Exec3 {

  private val exec: SparkConf = new SparkConf().setMaster("local[3]").setAppName("exec3")
  private val context = new SparkContext(exec)
  context.setCheckpointDir("checkPoint")
  /**
   * 执行exec3的作业任务
   * 算子进行聚合计算出苹果和华为的总售卖数并进行打印
   * */
  @Test
  def exec3(): Unit ={
    val value: RDD[(String, Int)] = context.parallelize(Seq(("苹果", 10), ("苹果", 15),("苹果", 11), ("华为", 20)))
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
   * aggregateByKey对应的计算结果是首先执行部分的计算的，然后执行的是全局的计算的。
   * seqOp 执行的是分区内部的计算的，对应的一直会计算1*10 1*10*15 要求至少一个的
   * combOp：执行的是shuffle之后的分区的计算的，对应的计算之后会执行分区合并操作的
   * aggregateByKey的计算结果都是错误的。
   * (华为,20.0)
   * (苹果,161.0)
   *
   * */
@Test
  def  testError(): Unit ={
    val rdd = context.parallelize(Seq(("苹果", 10), ("苹果", 15),("苹果", 11),("苹果", 12),("华为", 20)))
     rdd.aggregateByKey(0)(
      // aggregateByKey中的seqOp以及combOp如果执行不同的操作的话，
       // 对应的输出的结果就不一样了。这样的话，最终的输出结果千差万别的。
       //  解决的办法：
       // 1.将seqOp以及combOp的算法转换成为一致的，这样的话，rdd的分区数对于结果的影响就可以忽略了，结果是一样的
       // 2.需要注意以下使用操作，这个问题的操作设计到单个分区的操作和多个分区的操作的，需要关注一下他的使用的。不要使用不同的运算符操作的。
      seqOp = (zero, price) =>  zero+price,
      combOp = (curr, agg) => curr + agg
    ).collect().foreach(println(_))
    context.stop()
  }
}
