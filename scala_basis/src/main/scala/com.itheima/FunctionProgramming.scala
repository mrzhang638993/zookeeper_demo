package com.itheima

/**
 * 函数式编程操作实现
 * */
object FunctionProgramming {
  def main(args: Array[String]): Unit = {
    /*// 函数式变成以及变成方案和相关的变成实践
    val ints = List(1, 2, 3, 4, 5)
    //f: A => U  其中f是形参，代表的是函数。A => U代表的是函数的描述。其中A是入参,U代表的是出参
    ints.foreach(i => println(i))*/
    //列表中包含多个元素，需要进行元素的迭代遍历操作实现
    //val ints1 = List(5, 6, 7, 8)
    //ints1.foreach(println(_))
    // 函数式编程操作实现逻辑和特性
    // 集合的映射操作是spark以及flink代码编写的核心操作实现的。
    //ints1.map(i=>i+1).foreach(println(_))
    //ints1.map(_+1).foreach(println(_))
    // 扁平化操作:flatMap实现相关的flatMap实现操作管理flatten扁平化操作实现
    //val strMap = List("hadoop hive spark flink flume", "kudu hbase sqoop storm")
    // flattern会对应的将元素拆开的，string对应的会拆分成为多个char信息的
     //strMap.flatMap(_.split(" ")).foreach(println(_))
    // 使用map转化成为集合元素，然后使用flatten实现拆分操作的，不要使用flatmap后继续使用flatten的操作的
    // flattern 扁平化操作实现。map+flatten实现统一的扁平化操作实现。等价于flatMap操作的。
    //strMap.map(_.split(" ")).flatten.foreach(println(_))
    //  函数的过滤操作实现管理
    /*val ints = List(1, 2, 3, 4, 5, 6, 7, 8, 9)
    ints.filter(_%2==0).foreach(println(_))*/
    // Scala排序操作实现
    // sorted 默认排序，sortBy：根据指定字段排序;sortWith自定义排序操作实现
    //val ints = List(3, 1, 2, 9, 7)
    //  使用默认的排序操作实现机制
    //ints.sorted.foreach(println(_))
    // 使用sorted进行排序操作实现
    //val test = List("01 hadoop", "02 flume", "03 hive", "04 spark")
    // 根据指定的字段进行数据排序操作实现。sortBy实现操作对应的使用的字段进行排序操作实现的
    //test.sortBy(_.split(" ")(1)).foreach(println(_))  // 根据元素的某一个属性进行排序操作实现
    //sort with 根据指定的字段进行排序操作实现
    //test.sortWith((x,y)=>x<y) //实现升序排序操作 test.sortWith((x,y)=>x>y) 实现相关的降序排序操作实现
    // 实现更加复杂的降序排序操作实现
    //test.sortWith((_ > _))  //实现第一个元素大于第二个元素的操作，对应的是降序操作的。
    // 排序的本质是根据位置实现相关的排序操作的。
    //test.sortWith(_<_)     //实现降序排序操作实现
    //  分组操作实现 group by 实现分组操作实现
    /*val tuples = List("zhangsan" -> "男", "lisi" -> "女", "王五" -> "男")
    // 对应的使用相关的map操作实现逻辑的。map可以对分组之后的数据进行过滤操作实现的。
    val stringToTuples: Map[String, List[(String, String)]] = tuples.groupBy(_._2)
    stringToTuples.map(x=>x._1->x._2.size).foreach(println(_))*/
    // 聚合操作实现
    /*val nums = List(1, 2, 3, 4, 5, 6, 7, 8, 9, 10)
    //val result: Int = nums.reduce((x, y) => x + y)
    val result: Int = nums.reduce(_ + _) // 参数只是出现一次，并且只是执行简单的操作实现的。
    println(result)*/
    //fold折叠操作。对应的和reduce的操作是相类似的，对个元素合成为一个元素实现和管理操作的

  }
}
