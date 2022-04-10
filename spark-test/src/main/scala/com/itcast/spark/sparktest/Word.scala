package com.itcast.spark.sparktest

import org.apache.spark.sql.{DataFrame, Dataset, SparkSession}
import org.apache.spark.storage.StorageLevel

object Word {
  def main(args: Array[String]): Unit = {
    val spark: SparkSession = SparkSession.builder().master("local[2]")
      .appName("app")
      .getOrCreate()
    val wordDataset: Dataset[String] = spark.read.textFile("spark-test/word.txt").persist(StorageLevel.MEMORY_AND_DISK_SER_2)
    import spark.implicits._
    val maxCount: Int = wordDataset.map(word => word.split(" ").size).reduce((a, b) => if (a > b) a else b)
    println(maxCount)
    wordDataset.show()
    //使用flatmap算子实现其他特别好的操作方式和实现管理的
    val value: Dataset[String] = wordDataset.flatMap(word => word.split(" "))
    //使用默认的属性字段value可以完成相关的属性配置的。
    val frame: DataFrame = value.groupBy("value").count()
    //collect对应的会将所有的数据移动到driver上的,会存在内存溢出的风险的。
    val collects: Array[String] = value.collect()
    //集合元素组装成为单个的字符串的操作方式和实现管理的。
    val str: String = collects.toList.mkString("$$$")
    println(str)
    //对于cache的时候需要后期手动的进行资源的释放和管理操作的。手动的消除缓存操作实现,这个过程是需要执行的,以免导致内存的溢出操作问题的出现的。
    wordDataset.unpersist()
    //对应的是内存的一个解决方案的。
    /*frame.show()
    value.printSchema()
    value.show()
    spark.stop()*/
  }
}
