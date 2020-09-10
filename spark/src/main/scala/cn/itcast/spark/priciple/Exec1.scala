package cn.itcast.spark.priciple

import org.apache.spark.broadcast.Broadcast
import org.apache.spark.{SparkConf, SparkContext}
import org.junit.Test


/**
 * 假如有一个200M的数据集我们已将其转换为一个map，其格式如下
 *
 * Map("马云" -> "阿里巴巴", "马化腾" -> "腾讯")
 *
 * 要求使用广播变量计算出马云和马化腾对应的公司
 * */
class Exec1 {
  val testMaster: SparkConf = new SparkConf().setMaster("local[6]").setAppName("testMaster")
  val context = new SparkContext(testMaster)

  @Test
  def getMap(): Unit ={
    val strMap = Map("马云" -> "阿里巴巴", "马化腾" -> "腾讯")
    // 执行广播变量的操作逻辑
    val broadCastValue: Broadcast[Map[String, String]] = context.broadcast(strMap)
    broadCastValue.value.foreach(item=>println(item._1,item._2))
    context.stop()
  }
}
