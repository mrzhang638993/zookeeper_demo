package cn.itcast.spark.rdd.exec

import org.apache.spark.rdd.RDD
import org.apache.spark.{SparkConf, SparkContext}
import org.junit.Test

class ScalaPartitionTest {
  private val exec: SparkConf = new SparkConf().setMaster("local[6]").setAppName("exec2")
  private val context = new SparkContext(exec)
  context.setCheckpointDir("checkPoint")

  @Test
  def testPartitions(): Unit ={
    // 在parallelize阶段可以指定分区数信息来确定分区操作。
    val value: RDD[Int] = context.parallelize(Seq(1, 2, 3, 4, 5, 6, 7), numSlices = 3)
    println(value.partitions.size)
    // 重新创建分区，指定分区数.指定的是最小分区数信息的，实际的分区数>=最小分区数
    val value1: RDD[String] = context.textFile("F:\\works\\hadoop1\\zookeeper-demo\\spark\\src\\main\\scala\\cn\\itcast\\spark\\rdd\\exec\\BeijingPM20100101_20151231_noheader.csv", 6)
    println("指定最小分区数信息===="+value1.partitions.size)
    // 了解一下如何进行重分区操作
    // 默认的情况下coalesce是只能够减少分区的，需要增加分区操作的话需要指定shuffle参数的shuffle=true
    //  只能够更改新生成的分区数的，原来的分区数是不能更改的。rdd是只读的。
    val value2: RDD[String] = value1.coalesce(5)
    println("coalesce分区之后的代码操作==="+value2.partitions.size)
    //  rdd的分区数修改
    val value3: RDD[String] = value1.coalesce(7, true)
    println("coalesce 修改分区数信息==="+value3.partitions.size)
    //  repartitions修改分区数信息。可以减少和增加分区的。
    //  repartitions的代码如下：coalesce(numPartitions, shuffle = true) 底层是coalesce加上true的
    val value4: RDD[String] = value1.repartition(1)
    println("repartition 修改重新生成的partition的分区数信息"+value4.partitions.size)

    //  spark中的很多的算子都是支持repartitions分区数信息的。
    //  partitioner只有在key-value类型的数据中才可以使用的
   // def reduceByKey(partitioner: Partitioner, func: (V, V) => V): RDD[(K, V)] = self.withScope {
    //    combineByKeyWithClassTag[V]((v: V) => v, func, func, partitioner)
    //  }
  }

  /**
   * 一般情况下设计shuffle的算子都是会涉及到新的分区数的。如果没有指定新的分区数的。默认的是从父RDD中继承分区数信息
   * */

}
