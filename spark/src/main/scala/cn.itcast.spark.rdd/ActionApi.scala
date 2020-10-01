package cn.itcast.spark.rdd

import org.apache.spark.rdd.RDD
import org.apache.spark.{SparkConf, SparkContext}
import org.junit
import org.junit.Test


class ActionApi {

  private val exec: SparkConf = new SparkConf().setMaster("local").setAppName("exec2")
  private val context = new SparkContext(exec)
  context.setCheckpointDir("checkPoint")

  @junit.Test
  def  reduceByKey(): Unit ={
    val value: RDD[(String, Double)] = context.parallelize(Seq(("手机", 10.0), ("手机", 15.0), ("电脑", 20.0)))
    //   def reduce(f: (T, T) => T): T = withScope {
    // 计算出来的结果是一个元组的，不是一个reduce的
    val tuple: (String, Double) = value.reduce((curr: (String, Double), agg: (String, Double)) => ("总价", curr._2 + agg._2))
    //=====返回的是整个类型的数据的=====(总价,45.0)
    println("=====返回整个partition的分区数据====="+tuple)
    context.stop()
  }

  /**
   * collect使用集合的形式返回rdd的数据。
   * */
  @Test
  def testCollect(): Unit ={

  }

  /**
   * foreach
   * */
    @Test
  def  testForeach(): Unit ={
      val value: RDD[Int] = context.parallelize(Seq(1, 2, 3))
      //  rdd是并行计算的，所以不保证相关的顺序的。
      value.foreach(println(_))
      context.stop()
  }

  /**
   * count统计求和   countByKey:根据key进行分组统计
   * 每次调用action的时候，对应的都会执行一个job的。在job运行的过程中存在大量的log，对应的就是job的打印日志的操作的。
   *
   *
   * 解决数据倾斜的问题：需要找到那个key是被倾斜的操作的。
   * 通过countByKey可以查询key对应的数据总数，从而为数据倾斜提供一些解决思路。
   * */
  @Test
  def  testCount(): Unit ={
    val value: RDD[(String, Int)] = context.parallelize(Seq(("a", 1), ("a", 2), ("c", 3), ("c", 4)))
    //统计RDD中元素的个数
    println(value.count())
    // 根据key进行统计次数。得到的是map的数据结构。
    println(value.countByKey())
    //  统计value出现的次数 Map((c,4) -> 1, (a,2) -> 1, (c,3) -> 1, (a,1) -> 1)
    println(value.countByValue())
  }


  /**
   * first只是获取第一个数据的，take(N)对应的获取N个数据。
   * takeSample：前面的sample是map操作的，这个地方是一个action的直接获取样本的。
   * action操作会从所有的分区中获取数据的，速度比较的慢。first只是获取第一个元素的，所有first指挥处理第一个分区的数据的。所以速度很快的。
   * */
  @Test
  def  testTake(): Unit ={
    val value: RDD[Int] = context.parallelize(Seq(1, 2, 3, 4, 5))
    val ints: Array[Int] = value.take(3)
    ints.foreach(println(_))
    println(value.first())
    //  数据采用操作 withReplacement 是否覆盖，不覆盖的话数据还是存在于集合中的.true代表覆盖，取出来的话数据就不存在集合中了。
    val ints1: Array[Int] = value.takeSample(withReplacement = false, num = 3)
    ints1.foreach(println(_))
    value.foreach(println(_))
  }
}
