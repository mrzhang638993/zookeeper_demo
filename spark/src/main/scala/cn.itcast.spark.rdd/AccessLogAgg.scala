package cn.itcast.spark.rdd

import org.apache.commons.lang3.StringUtils
import org.apache.spark.rdd.RDD
import org.apache.spark.{SparkConf, SparkContext}
import org.junit.Test

class AccessLogAgg {

      // 1.创建sparkcontext
      val testMaster: SparkConf = new SparkConf().setMaster("local[6]").setAppName("testMaster")
      val context = new SparkContext(testMaster)
      @Test
      def ipAgg(): Unit ={
            val value: RDD[String] = context.textFile("file:///F:\\works\\hadoop1\\zookeeper-demo\\spark\\src\\main\\scala\\cn.itcast.spark.rdd\\access_log_sample.txt")
            //  item对应的数据是一行数据的.需要去掉空的行信息的
            val value1: RDD[(String, Int)] = value.map(item => (item.split(" ")(0), 1))
            //  获取每一个ip对应的访问次数
            val value2: RDD[(String, Int)] = value1.filter(item => StringUtils.isNotEmpty(item._1)).reduceByKey((cur, agg) => cur + agg)
            // 根据出现的次数降序排列,取出前面的10个。
            //(91.145.130.78,17)
            //(82.112.192.66,9)
            //(198.53.219.177,8)
            //(36.80.85.184,8)
            //(122.52.122.210,7)
            //(175.158.232.158,7)
            //(110.54.146.228,7)
            //(162.97.9.49,7)
            //(80.98.215.175,6)
            //(130.105.24.240,6)
            value2.sortBy(item=>item._2,ascending = false).collect().take(10).foreach(println(_))
            context.stop()
      }
}
