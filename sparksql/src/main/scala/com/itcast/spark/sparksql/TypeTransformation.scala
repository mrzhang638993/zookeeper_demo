package com.itcast.spark.sparksql

import java.lang

import org.apache.spark.sql.{Column, Dataset, KeyValueGroupedDataset, SparkSession}
import org.junit.Test

class TypeTransformation {

  val spark: SparkSession = SparkSession.builder()
    .appName("typed")
    // 集群模式下面是不需要设置master的，集群里面是有自己的master的。
    .master("local[6]")
    .getOrCreate()
  import spark.implicits._
  /**
   * 执行filter操作实现
   * */
  @Test
  def  testFilterRdd(): Unit ={
    val ds: Dataset[Person] = Seq(Person("zhangsan", 20), Person("lisi", 15), Person("wangwu", 25)).toDS()
    ds.filter(person=>person.age>15).show()
  }

  /**
   * 执行聚合操作.groupBy
   * */
  @Test
  def testAggr(): Unit ={
    val ds: Dataset[Person] = Seq(Person("zhangsan", 20), Person("zhangsan", 15), Person("wangwu", 25)).toDS()
    //  根据person.name作为key执行group计算的。
    // String 对应的是key的类型的，Person对应的是value的类型的
    val df: KeyValueGroupedDataset[String, Person] = ds.groupByKey(person => person.name)
    // 执行聚合操作
    val group: Dataset[(String, Long)] = df.count()
    // 执行有类型的操作的
    group.show()
  }

  /**
   * 测试split算子执行操作
   * */
  @Test
  def  execSplit(): Unit ={
    // 执行的是split的操作实现的。
    val range: Dataset[lang.Long] = spark.range(15)
    // randowSplit的操作的.根据权重切割为多少份的数据的。
    // weights: Array[Double], seed: Long
    val array: Array[Dataset[lang.Long]] = range.randomSplit(Array(0.6, 0.2, 0.2))
    // 显示每一个数据集进行数据操作的
    array.foreach(_.show())

    // 抽样进行操作实现和管理操作的。false对应的代表的是无放回的操作的。fraction对应的是采样比的操作的。
    range.sample(false,0.6).show()
  }

  /**
   * orderBy  sort排序操作
   * */
  @Test
  def  testSort(): Unit ={
    val ds: Dataset[Person] = Seq(Person("zhangsan", 12), Person("zhangsan", 8), Person("wangwu", 15)).toDS()
    //  指定升序和降序操作的
    ds.orderBy(new Column("name").desc,new Column("age").asc).show()
    //  还可以使用sort方法剩下相关的操作的
    ds.sort(new Column("name").desc,new Column("age").asc).show()
  }

  /**
   * 去重操作的算子
   * dictinct
   * dropDuplicates
   * */
  @Test
  def quitDouble(): Unit ={
    // 执行去重操作实现
    val ds: Dataset[Person] = Seq(Person("zhangsan", 15), Person("zhangsan", 15), Person("lisi", 15)).toDS()
    // 去重重复row的数据的。每一行对应的是一个Person对象的。
    ds.distinct().show()
    //  删除操作执行colNames。根据name操作去重。
    // ds.dropDuplicates(Seq("name")).show()
    //ds.dropDuplicates(Seq("name","age")).show()
    ds.dropDuplicates(Seq("age")).show()
  }

  /**
   * 集合操作，交集，差集，并集以及limit执行操作
   * */
  @Test
  def collection(): Unit ={
    //  创建集合操作1
    val set1: Dataset[lang.Long] = spark.range(15)
    // 创建集合操作2
    val set2: Dataset[lang.Long] = spark.range(10, 20)

    // 求解差集操作.set1中的数据去掉set2中的数据的
    set1.except(set2).show()

    // 求解交集的操作实现.10,11,12,13,14
    set1.intersect(set2).show()

    // 求解并集的操作.10,11,12,13,14都是包含的
    set1.union(set2).show()

    // limit操作
    set1.limit(3).show()
  }


  /**
   * 无类型的算子，对应的是使用相关的schema的信息执行操作的
   * */
  @Test
  def  testSchema(): Unit ={
    val persons = Seq(Person("zhangsan", 12), Person("lisi", 18), Person("zhansgan", 8)).toDS()
    //  sql语句的操作不是应该从select上面的。真正的操作是from进行操作的。
    persons.
      //  spark中在select中是可以执行很多的操作的，不一定是需要选择select的操作的。
      select("name").show()
    // 对应的执行逻辑操作实现
    persons.selectExpr("count(age)").show()
    persons.selectExpr("sum(age)").show()
    import org.apache.spark.sql.functions._
    //
    persons.select(expr("sum(age)")).show()
  }

  /**
   * 无类型的计算算子
   * */
  @Test
  def testNonSchema(): Unit ={
    // 测试新建列，重命名列的数据
    val persons = Seq(Person("zhangsan", 12), Person("lisi", 18), Person("zhansgan", 8)).toDS()
    // 对应的select语句中选择执行sql语句进行操作
    import org.apache.spark.sql.functions._
    // 新建列名称
    persons.withColumn("random",expr("rand()")).show()
    // 增加列的操作.name_column对应的数据是列的数据的
    persons.withColumn("name_column",expr("name")).show()
    // 增加列的操作的，对应的判断每一列的数据是否是等于“”的数据的。增加新的列进行操作的。
    persons.withColumn("name_jok",new Column("name")==="").show()
    //重命名操作实现和管理实现
    persons.withColumnRenamed("name_new","name")
  }

  /**
   *  drop 可以删除对应的数据的列的数据的
   * */

  /**
   * 无类型的转换算子实现操作：测试groupBy执行操作语句
   * */
  @Test
  def testGroupBy(): Unit ={
    val persons = Seq(Person("zhangsan", 12), Person("lisi", 18), Person("zhangsan", 8)).toDS()
    //  为什么groupByKey对应的是有类型的操作的。groupBy对应的是无类型的操作的
    // groupByKey 生成的算子是有类型的。可以使用到Person对象的类型的
   //persons.groupByKey(person=>person.age)
    //  根据指定的列执行操作的.groupby对应的算子生成的是无类型的。
    import org.apache.spark.sql.functions._
    // 根据name进行聚合操作，执行sum统计求和操作
    persons.groupBy("name").agg(mean("age")).show()
    //  进行聚合求和统计操作实现
    persons.groupBy("name").agg(sum("age")).show()
  }

  /**
   * 在使用无类型的对象的使用，使用的是column对象的
   * 下面是column对象的创建和使用操作的
   * */
  @Test
  def testCreateColumn(): Unit ={
      //下面的是column的创建的函数以及操作实现
      val persons = Seq(Person("zhangsan", 15), Person("lisi", 10)).toDS()
    //创建df实现相关的代码的操作实现
     var df=Seq(("zhangsan", 15),("lisi", 10)).toDF("name","age")
      // 方式之一:‘创建操作的. symbol对象最终会转化为column对象的
    //  implicit def symbolToColumn(s: Symbol): ColumnName = new ColumnName(s.name)  对应的Symbol对象会转化成为Colume的对象的
     var column ='name
    //  创建方式之二: $创建对应的操作实现.必须导入spark的隐式转换操作
    var column1=$"name"
    // 创建方式之三：使用 col方式创建操作,需要导入隐世转换操作的实现
    import org.apache.spark.sql.functions._
    var   column2=col("name")
    // 创建方式之四:隐式导入functions的函数的
    import org.apache.spark.sql.functions._
    var   column3=column("name")
    //  ds可以使用column对象的
    persons.select(column).show()
    df.select(column1).show()

    // 可以使用其他的算子执行算子操作实现的.实现条件过滤操作的
    df.where(column==="zhangsan").show()
  }
}
