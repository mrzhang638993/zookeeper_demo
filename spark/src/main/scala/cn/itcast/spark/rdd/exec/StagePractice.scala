package cn.itcast.spark.rdd.exec

import org.apache.commons.lang3.StringUtils
import org.apache.spark.rdd.RDD
import org.apache.spark.{SparkConf, SparkContext}
import org.junit.Test

class StagePractice {
  private val exec: SparkConf = new SparkConf().setMaster("local[6]").setAppName("exec2")
  private val context = new SparkContext(exec)
  context.setCheckpointDir("checkPoint")

  /**
   * 1.创建sparkContext的上下文环境
   * 2.创建RDD
   * 3.处理RDD
   * 4.行动得到结果
   * */
  @Test
  def  pmProcess(): Unit ={
    //  创建sc的context对象,读取文件
    val source: RDD[String] = context.textFile("F:\\works\\hadoop1\\zookeeper-demo\\spark\\src\\main\\scala\\cn\\itcast\\spark\\rdd\\exec\\BeijingPM20100101_20151231_noheader.csv")
    //  读取文件进行转化操作
    // 统计每年每月的dongsi的pm的总和是多少
    // ((2015,8),36451)
    //((2014,12),37612)
    //((2015,9),38511)
    //((2014,6),41389)
    //((2015,5),42607)
    //((2013,8),43926)
    //((2014,5),45143)
    //((2013,4),45217)
    //((2015,6),46731)
    //((2013,7),47681)
   /* val finalTupleValue: RDD[((String, String), Int)] = source.filter(item => item.split(",")(6) != "NA" && item.split(",")(6) != "").map(item => {
      val valueArray: Array[String] = item.split(",")
      ((valueArray(1), valueArray(2)), valueArray(6))
    }).map(item => (item._1, item._2.toInt))
      .reduceByKey((curr, agg) => curr + agg)
      .sortBy(item => item._2,ascending = false)*/
    /** ((2015,12),119123)
    *((2014,2),92123)
    *((2015,11),89814)
    *((2014,10),85926)
    *((2013,3),79348)
    *((2014,1),76496)
    *((2015,1),73679)
    *((2014,7),73625)
    *((2014,3),73176)
    *((2015,2),72381)
    */
    // 或者是采用如下的语句的
    val finalTupleValue: RDD[((String, String), Int)] = source.map(item => ((item.split(",")(1), item.split(",")(2)), item.split(",")(6)))
      .filter(item => StringUtils.isNotEmpty(item._2))
      .filter(item => !item._2.equalsIgnoreCase("NA"))
      .map(item => (item._1, item._2.toInt))
      .reduceByKey((curr, agg) => curr + agg)
      // 降序排列
      .sortBy(item => item._2,ascending = false)
    finalTupleValue.take(10).foreach(println(_))
    context.stop()
  }
}
