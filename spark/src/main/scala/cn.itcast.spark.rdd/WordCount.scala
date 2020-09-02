import org.apache.spark.rdd.RDD
import org.apache.spark.{SparkConf, SparkContext}
import org.junit.Test

class WordCount{

  def main(args: Array[String]): Unit = {
      //创建sparkContext对象
    var conf=new SparkConf().setMaster("local[6]").setAppName("word_count")
    var sc=new SparkContext(conf)
    // 读取文件
    //val file: RDD[String] = sc.textFile("dataset\\wordcount.txt")
    val file: RDD[String] = sc.textFile("hdfs://node01:8020/data/wordcount.txt")
    // 加载数据文件
    val value: RDD[String] = file.flatMap(item => item.split(" "))
    val value1: RDD[(String, Int)] = value.map(item => (item, 1))
    //  进行文件处理,key对应的是key的上一个值，value对应的是key的下一个value值的。
    val value2: RDD[(String, Int)] = value1.reduceByKey((key, value) => key + value)
    val tuples: Array[(String, Int)] = value2.collect()
     tuples.foreach(x=>println(x._1+":"+x._2))
    //  得到最终结果
  }
  val testMaster: SparkConf = new SparkConf().setMaster("local[6]").setAppName("testMaster")
  val context = new SparkContext(testMaster)
  @Test
  def sparkContext(): Unit ={
    //context.stop()
  }
  /**
   * 从本地文件加载
   * */
    @Test
  def  rddCreateLocal(): Unit ={
      //  本地数据源计算的话，可以指定分区数
    var seq=Seq(1,2,3)
    //  指定并行数量 numSlices指定，RDD对应的也是存在类型的.parallelize是可以不指定分区数的。
    val rdd1: RDD[Int] = context.parallelize(seq,2)
    //context.makeRDD(seq)
    val rdd2: RDD[Int] = context.makeRDD(seq, 2)
  }
  /**
   * 从hdfs创建
   * */
    @Test
  def rddCreateHDFS(): Unit ={
      //  使用外部的数据源进行计算的话，分区数取决于外部的数据源。可以手动指定的
     context.textFile("hdfs://node01:8020/")
  }
  /**
   * 根据RDD创建
   * */
  def rddCreateFromRDD(): Unit ={
    var seq=Seq(1,2,3)
    val rdd1: RDD[Int] = context.parallelize(seq,2)
    //  使用RDD生成新的rdd的，rdd是不可变的。
    val value: RDD[Int] = rdd1.map(item => item + 1)
  }

  /**
   * map算子
   * */
    @Test
  def mapRdd(): Unit ={
    val ints = context.parallelize(Seq(1, 2, 3))
      val value: RDD[Int] = ints.map(item => item * 10)
      val ints1: Array[Int] = value.collect()
      ints1.foreach(item=>println(item))
  }

  /**
   * flatMap算子.flatmap将list或者是map展开。
   * */
    @Test
  def  flatMapTest(): Unit ={
    val value: RDD[String] = context.parallelize(List("Hello lily", "Hello lucy", "Hello tim"))
    val strings: Array[String] = value.flatMap(item => item.split(" ")).collect()
    for (elem <- strings) {
      println(elem)
    }
  }

  /**
   * reduceByKey按照key进行规约操作.
   * 根据key进行分组，然后对每一组进行聚合操作的。
   * */
    @Test
  def  reduceByKeyTest(): Unit ={
    val value: RDD[String] = context.parallelize(List("Hello lily", "Hello lucy", "Hello tim"))
    // agg保存的是叠加之后的数值的。curr代表的是当前的数值的
    value.flatMap(item=>item.split(" "))
      .map(item=>(item,1))
      .reduceByKey((curr,agg)=>curr+agg)
      .collect().foreach(item=>println(item))
  }


  /**
   *
   * map对于每一条数据进行处理的，如果在map阶段需要创建数据库连接的话，这样的话存在问题的，效率很多的。
   * 接收一整个分区的数据执行计算的
   * mapPartition算子，mapPartitions可以根据分区执行操作的，mappartitions的粒度对应的是分区的，
   * map对应的粒度是每一条数据的
   * */
  @Test
  def testMapPartition(): Unit ={
    // 数据生成
    //  算子使用
    //  获取结果
    //  指定2个分区执行操作
    val values: RDD[Int] = context.parallelize(Seq(1, 2, 3, 4, 5, 6), numSlices = 2)
    // preservesPartitioning 保留parent的分区信息。参数是一个集合的。mapPartition一次处理的是一个分区的数据的
    // item 对应的是一个集合的
     values.mapPartitions(iter =>{
       iter.foreach(item=>println(item))
      iter
    }, preservesPartitioning = false).collect()
    context.stop()
  }


  @Test
  def  mapPartitions2(): Unit ={
    val value: RDD[Int] = context.parallelize(Seq(1, 2, 3, 4, 5, 6),numSlices=2)
    value.mapPartitions(iter=>{
      // iter的算子map对应的是scala中的算子的，和spark中的算子没有任何的关系的。
      val iterator: Iterator[Int] = iter.map(item => item * 10)
      iterator
    }).collect().foreach(println(_))
    context.stop()
  }

  /**
   * 根据索引执行分区操作实现
   * index对应的是原始分区的index数据
   * */
  @Test
  def  mapPartitionsWithPartitions(): Unit ={
    val value: RDD[Int] = context.parallelize(Seq(1, 2, 3, 4, 5, 6), numSlices = 2)
    // 需要书写collect的，map是不会执行计算的，是惰性计算的
    value.mapPartitionsWithIndex((index,iter)=>{
      // 算子里面的操作对应的是并行的操作的
      println("index:"+index)
      iter.foreach(item=>println(item))
      iter
    }).collect()
    context.stop()
  }

  /**
   * 数据清洗：清理掉数据中不合法的数据实现过滤操作的
   * 过滤得到偶数数据
   * */
    @Test
  def  filterTest(): Unit ={
    val value: RDD[Int] = context.parallelize(Seq(1, 2, 3, 4, 5, 6,7,8,9,10))
     value.filter(item=>item%2==0).collect().foreach(println(_))
    context.stop()
  }
}