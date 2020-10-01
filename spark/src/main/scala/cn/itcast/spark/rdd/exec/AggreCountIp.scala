package cn.itcast.spark.rdd.exec

import org.apache.commons.lang.StringEscapeUtils
import org.apache.commons.lang3.StringUtils
import org.apache.spark.rdd.RDD
import org.apache.spark.storage.StorageLevel
import org.apache.spark.{SparkConf, SparkContext}
import org.junit.Test

/**
 * 需求：统计访问次数最多的ip以及访问次数最少的ip
 * */
class AggreCountIp {

  private val exec: SparkConf = new SparkConf().setMaster("local[6]").setAppName("exec2")
  private val context = new SparkContext(exec)
  //  需要spark整合hdfs才可以实现的。
  // 直接在工程下面创建checkpoint的dir的，也可以设置为hdfs的文件目录上面的。
  context.setCheckpointDir("checkPoint")


  /**
   * 缓存的方法：
   * 1.cache:
   * 2.persist:可以修改缓存的存储级别。
   * */
  @Test
  def  cacheTest(): Unit ={
    val sourceFile: RDD[String] = context.textFile("F:\\works\\hadoop1\\zookeeper-demo\\spark\\src\\main\\scala\\cn\\itcast\\spark\\rdd\\exec\\access_log_sample.txt")
    val value: RDD[(String, Int)] = sourceFile.map(item => (item.split(" ")(0), 1))
      .filter(item => StringUtils.isNotEmpty(item._1))
      .reduceByKey((curr, agg) => curr + agg)
    //  推荐使用first不推荐使用take进行操作的
    /**
     * lessIp:执行了2次的shuffle操作的，包括reduceByKey操作以及sortBy操作的
     * mostIp：对应的执行了2次的shuffle操作的，包括了reduceByKey操作以及sortBy的操作的
     * 全局总共操作了4次的shuffle操作的，代码是存在问题的。存在大量的磁盘io操作的，需要进行优化操作实现。
     * 执行多次job操作的时候，不要每一次都计算相同的数据的。
     * 缓存存在的意义：
     * 1.减少shuffle操作；
     * 2.减少其他算子的执行，直接缓存执行的结果
     * 3.实现容错操作
     * */
    //def cache(): this.type = persist()
    // 缓存RDD操作，得到缓存之后的RDD操作的。
    //val value1: value.type = value.cache()
    //  使用方法之二：在cache之后执行chekpoint操作。
    val value1: value.type = value.persist(StorageLevel.MEMORY_ONLY)
    value1.checkpoint()
    val lessIp: (String, Int) = value1.sortBy(item => item._2, ascending = true).first()
    val mostIp: (String, Int) = value1.sortBy(item => item._2, ascending = false).first()
    println("访问次数最少的ip："+lessIp._1+"次数是:"+lessIp._2)
    println("访问次数最多的ip："+mostIp._1+"次数是:"+mostIp._2)
    context.stop()
  }


  /**
   * 操作建议：可以将数据序列化之后存储到文件中的，减少数据量的传输操作的。提高数据的读写速度的。
   * 限于单个的应用之内的。
   * */
  /**
   * RDD的缓存级别:使用RDD的缓存和设置缓存级别
   *1.是否使用磁盘缓存
   *2.是否使用内存缓存
   *3.使用使用堆外缓存：一般的不适用。
   *4.缓存之前是否需要序列化：数据特别大的话，序列化之后存储的话比较的好的。使用反序列化存储的话，对应的存储的是对象的，否则存储的是序列化的，对应的是二进制数据的。
   * 内存中使用序列化存储的话，减少内存的占用操作。
   *5.是否需要副本
   *spark的缓存级别对应的是上面的5个参数进行管理的。
   * MEMORY_ONLY:没有序列化和反序列的操作的，内存的使用效率最高的。
   * MEMORY_ONLY_SER：序列化操作，可以减少数据的传输量的
   * MEMORY_AND_DISK：内存不足了，需要数据写入到磁盘的。
   * MEMORY_ONLY_2： 带有参数2的可以缓存2份的，提高数据的可用性的。
   * 数据默认使用MEMORY_ONLY，数据比较多的话使用MEMORY_ONLY_SER，
   * 数据比较昂贵的话，可以使用MEMORY_AND_DISK。数据特别重要的话，可以使用2个备份的。
   * */
  @Test
  def cacheLevel(): Unit ={

  }


  /**
   * rdd的chckpoint的机制很重要的。 数据的快速回复和容错的。
   * checkpoint的主要的作用是斩断RDD的依赖链。避免依赖过程恢复速度太慢。不用计算斩断之前的rdd的链条的
   * checkpoint使用两种方式：
   * 1.可靠的，存储于可靠的存储引擎中的；将rdd中的数据缓存到存储引擎中。
   * 2.本地的：数据存储在本地。
   *
   *
   * namenode的2个存储：
   * 1.fsimages文件；
   * 2.edits文件。每一次修改文件的话，都会向edit是新增记录的。
   * 某一段时间全部擦掉的。生成新的fsimage的，在一定条件下生成新的edits的。避免了edits过大之后，
   * 查看当前系统的文件数的话，速度太慢了。斩掉前面的ids的话，增加速度的同时增强稳定性。
   *
   *
   *
   * cache和checkpoint的区别：
   * 1.cache是将rdd计算的结果保存在内存和本地存储中的，但是对应的依赖链的数据是不变的。
   * 出错了的话并且cache损坏的话，还是需要计算整个的rdd的依赖链的。
   * 2.checkpoint直接将数据保存到HDFS这一类的存储中，存储是可靠的。可以斩断依赖链，复制hdfs中的文件就可以实现容错的。
   * 所以checkpoint是没有向上的依赖链的，程序结束之后还是会存在的，不会被删除的，而cache和persist会在程序结束之后立刻被销毁的。
   * 最大的区别的是：checkpoint数据存储到外部可靠的存储中，
   * 所以可以斩断依赖链的，cache以及persist是存储在不可靠的存储中的，所以不能斩断存储链的
   * */
    @Test
  def testCheckPoint(): Unit ={
      val value: RDD[Int] = context.parallelize(Seq(1, 2, 3, 4, 5, 6, 7))
      // Unit 对应的是没有返回值的。调用了checkpoint的话，会重新计算一次rdd的。将计算结果保存到hdfs中的或者是本地的目录中的。
      //  正确的操作是在checkpoint之前执行cache操作的
      val value1: value.type = value.cache()
      value1.checkpoint()
      context.stop()
  }
}
