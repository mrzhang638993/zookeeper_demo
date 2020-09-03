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

  /**
   * 数据抽样：将大数据集转化为小一点的数据集，
   * 大数据集的规律在小数据集中需要得到体现的
   * sample算子，从大数据集中抽取出来数据的，并且不保证数据的丢失操作的。
   * */
  @Test
  def sampleTest(): Unit ={
    val value: RDD[Int] = context.parallelize(Seq(1, 2, 3, 4, 5, 6, 7, 8, 9, 10))
    //withReplacement: Boolean,  数据是否有放回操作。false对应的是无放回操作。true对应的是放回操作的。下一次还可以抽取到的
    //      fraction: Double,  采用比例
    //      seed: Long = Utils.random.nextLong   随机数种子的，seed一般的不指定的
    value.sample(false,0.6).collect().foreach(println(_))
    context.stop()
  }

  /**
   * mapValues：对应的只是操作nap的value数据。
   * item对应的是map的value的
   * */
    @Test
  def assembleMapValues(): Unit ={
      val value: RDD[(String, Int)] = context.parallelize(Seq(("a", 1), ("b", 2), ("c", 3)))
      value.mapValues(item=>item*10).collect().foreach(println(_))
      context.stop()
  }

  /**
   * 下面对应的是spark的集合操作，对应的体现出来的是交集，差集以及并集的操作
   * */
  @Test
  def testIntersection(): Unit ={
    val value: RDD[Int] = context.parallelize(Seq(1, 3, 5))
    val value1: RDD[Int] = context.parallelize(Seq(3, 6, 8))
    value.intersection(value1).collect().foreach(println(_))
    context.stop()
  }

  /**
   * 并集的操作实现
   * */
    @Test
  def testUnion(): Unit ={
      val value: RDD[Int] = context.parallelize(Seq(1, 3, 5))
      val value1: RDD[Int] = context.parallelize(Seq(2, 3, 6))
      // 并集操作结果 135236 对应的是所有的结果的
      value.union(value1).collect().foreach(println(_))
      context.stop()
  }

  /**
   * 求解集合中的差集数据
   * */
    @Test
  def testSubtract(): Unit ={
      val value: RDD[Int] = context.parallelize(Seq(1, 3, 5, 7, 8))
      val value1: RDD[Int] = context.parallelize(Seq(1, 2, 4, 6, 7, 8))
      //  得到差集结构是3,5的数据集的
      value.subtract(value1).collect().foreach(println(_))
      context.stop()
  }


  /**
   * 聚合操作实现  reduceByKey
   * groupByKey：根据key进行分组操作.本质是一个shuffle的过程的。
   * groupByKey的运算结果是：
   * (a,CompactBuffer(1, 1))
   * (b,CompactBuffer(1))
   *
   * reduceByKey：是否可以在map端实现conbinder是否可以减少io。可以在map端减少io操作的
   * groupByKey:没有减少io的次数的。
   * reduceByKey以及groupBykey底层都是通过combineByKey实现的。
   */
  @Test
  def groupByKey(): Unit ={
      context.parallelize(Seq(("a",1),("a",1),("b",1)))
        .groupByKey().collect().foreach(println(_))
    context.stop()
  }
  /**
   * 求解得到总分数和科目数
   * */
  @Test
  def  testCombinedByKey(): Unit ={
    val value: RDD[(String, Double)] = context.parallelize(Seq(("zhangsan", 99.0),
      ("zhangsan", 96.0),
      ("lisi", 97.0),
      ("lisi", 98.0),
      ("zhangsan", 97.0)))
    //  createCombinder对应的转换数据
    // createCombiner: V => C,  需要学会理解参数中的参数的含义信息的
    //      mergeValue: (C, V) => C,  转换数据的函数。只作用于第一条数据的，用于开启计算
    //      mergeCombiners: (C, C) => C,
    //      numPartitions: Int
    // (zhangsan,97.33333333333333)
    //(lisi,97.5)
    val value1: RDD[(String, (Double, Int))] = value.combineByKey(
      //  item对应的是96.0的数据的 96.0=>(96.0,1) .nextValue对应的是97.0的
      createCombiner = (item: Double) => (item, 1), mergeValue = (item: (Double, Int),
                                                                  nextValue: Double) => (item._1 + nextValue, item._2 + 1),
      mergeCombiners = (curr: (Double, Int), nextValue: (Double, Int)) => (curr._1 + nextValue._1, curr._2 + nextValue._2),
      numPartitions = 2
    )
    value1.map(item=>(item._1,item._2._1/item._2._2)).collect().foreach(println(_))
    //  mergeValue实现分区上的聚合操作
    // mergeCombiners 把所有分区上的数据再次聚合，生成最终结果。
   context.stop()
  }

  /**
   * foldByKey:相比较于reduceByKey而言存在初始值，fold对应的是折叠的意思
   * foldByKey和reduceByKey的区别在于foldByKey是可以指定初始值的。这个初始值作用于整个的数据的。
   * foldByKey的底层对应的是aggregateByKey实现的。
   * */
    @Test
  def  testFoldByKey(): Unit ={
      //(b,11)
      //(a,22)
    val value: RDD[(String, Int)] = context.parallelize(Seq(("a", 1), ("a", 1), ("b", 1)))
    //  函数的柯理化参数操作实现。初始值会作用于每一个元素的。
    value.foldByKey(10,numPartitions=2)((prev,next)=>prev+next).collect().foreach(println(_))
    context.stop()
  }

  /**
   * aggregateByKey的操作实现
   * (zeroValue: U)(seqOp: (U, V) => U,
   * combOp: (U, U) => U
   * 对所有的商品打折，然后计算商品的总价
   * zeroValue 代表的是初始值。seqOp作用于每一条数据，combOp实现元素的聚合操作。
   * aggregateByKey(0.8)((zeroValue,item)=>zeroValue*item,(curr,agg)=>curr+agg)
   * */
    @Test
  def testAggregateByKey(): Unit ={
      val value: RDD[(String, Double)] = context.parallelize(Seq(("手机", 10.0), ("手机", 15.0), ("电脑", 20.0)))
      // curr代表的是当前的数据的，agg代表的是聚合的数据的
      // (手机,20.0)
      //(电脑,16.0)
      value.aggregateByKey(0.8)((zeroValue,item)=>zeroValue*item,(curr,agg)=>curr+agg).collect().foreach(println(_))
      context.stop()
  }

  /**
   * join算子操作。对两个数据集执行连接操作
   * */
    @Test
  def testJoin(): Unit ={
      val value: RDD[(String, Int)] = context.parallelize(Seq(("a", 1), ("a", 2), ("b", 1)))
      val value1: RDD[(String, Int)] = context.parallelize(Seq(("a", 1), ("a", 10)))
      //  根据key相同实现join操作的,join操作默认是根据key执行操作的。
      // (a,(1,1))
      //(a,(1,10))
      //(a,(2,1))
      //(a,(2,10))
      value.join(value1).collect().foreach(println(_))
      context.stop()
  }
}