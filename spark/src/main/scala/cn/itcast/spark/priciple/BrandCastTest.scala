package cn.itcast.spark.priciple

import org.apache.spark.broadcast.Broadcast
import org.apache.spark.{SparkConf, SparkContext}
import org.junit.Test

class BrandCastTest {

  private val exec: SparkConf = new SparkConf().setMaster("local[3]").setAppName("exec3")
  private val context = new SparkContext(exec)
  context.setCheckpointDir("checkPoint")
  /**
   * 测试广播机制操作
   * */
  @Test
  def testBroadTest(): Unit ={
    val v = Map("Spark" -> "http://spark.apache.org/", "Scala" -> "http://www.scala-lang.org/")
    val pws = context.parallelize(Seq("Spark","Scala"))
    // 涉及到数据的分发操作的。需要给每一个task传递数据的item=>v(item) 这样的话，数据量就有点大了，需要使用广播的方式实现优化的
     pws.map(item=>v(item)).collect()
  }

  /**
   * 广播的方式实现分发操作的
   * 代码的改进操作如下实现的。使用广播的方式实现创建操作的
   * */
  @Test
  def  testBroadCast(): Unit ={
    val v = Map("Spark" -> "http://spark.apache.org/", "Scala" -> "http://www.scala-lang.org/")
    val pws = context.parallelize(Seq("Spark","Scala"))
    //  数据通过广播的方式实现数据的传递操作的。在算子中使用广播变量代替直接数据的。数据只会复制和executor数量相等的数据的。
    //  数据广播之前，复制的数量和task的数量相同
    //  使用广播之后，复制的数量和executor的数量一样的。 真实的情况下，task的数量比executor的数量多的多的。
    val bc: Broadcast[Map[String, String]] = context.broadcast(v)
    pws.map(item=>bc.value(item)).collect()
  }
}
