package com.itheima


/**
 * 列表操作实现
 * */
object ListOperation {

  def main(args: Array[String]): Unit = {
      //定义不可变列表的创建方式
      /*val strings = List("hello", "world","good","many")
      strings.foreach(println(_))*/
      //创建一个空的列表.不可变的空的列表执行操作。
      /* val nil: Nil.type = scala.collection.immutable.Nil
      println(nil)*/
      //  创建列表操作实现.创建只有两个元素的列表操作实现。
      /*val dest="good" :: "many" ::scala.collection.immutable.Nil
      dest.foreach(println(_))*/
      // 创建可变列表实现操作和管理实现的
      /*val buffer: ListBuffer[String] = ListBuffer[String]()
      buffer +="hello"
      buffer +="world"
      buffer +="google"
      buffer.foreach(println(_))*/
     // 创建可变列表的第二种方式
     /* val strings: ListBuffer[String] = ListBuffer("mr", "zhang", "test", "good")
      strings.foreach(println(_))*/
    //定义可变列表执行操作
    /*val ints: ListBuffer[Int] = ListBuffer(1, 2, 3)
    println(ints(0))
    ints +=4
    val ints1 = List(5, 6, 7)
    ints ++=ints1
    ints -=7
    ints.toList.foreach(println(_))
    ints.toArray.foreach(println(_))*/
    // 列表常见的操作
    /*val a = List(1, 2, 3, 4)
    println(a.isEmpty)*/
    //拼接列表执行操作
    /*val ints = List(1, 2, 3)
    val ints1 = List(4, 5, 6)
    val ints2=ints++ints1  // 两个列表的拼接操作实现
    ints2.foreach(println(_))*/
    // 获取列表的首个元素以及剩余部分
    // val ints = List(1, 2, 3, 4, 5)
    /* println(ints.head) // 除了首个元素
     println(ints.tail)  // 获取首个元素之外的元素*/
    //val reverse: List[Int] = ints.reverse
    //reverse.foreach(println(_))
    //获取列表的前缀以及后缀的操作实现
    //take 方法对应的获取前缀操纵；drop对应的获取后缀的操作实现
    /* val ints = List(1, 2, 3, 4, 5, 6)
    //获取前面的3个元素
    val ints1: List[Int] = ints.take(3)
    ints1.foreach(_=>println(_))
    //获取前面3个元素之外的元素。
    val ints2: List[Int] = ints.drop(3)
    ints2.foreach(_=>println(_))*/
    //列表的扁平化操作实现。使用flatten执行扁平化操作的
    /*val list = List(List(1, 2), List(3), List(4, 5))
    val flatten: List[Int] = list.flatten
    flatten.foreach(println(_))*/
    // 拉链以及拉开.将两个列表合并成为一个列表，称之为拉链，将单个的列表拉开成为两个列表称之为拉开操作
    /*val ints = List(5, 6, 7, 8)
    val ints1 = List(0, 1, 2, 3)
    val connect=ints  zip  ints1 */ // 拉链操作实现 zip操作，还可以使用更加高效的方式
    //ints.zip(ints1)
    //connect.foreach(println(_))
    // 拉开操作的实现 unzip
    /*val unzip: (List[Int], List[Int]) = connect.unzip
    unzip._1.foreach(println(_))
    unzip._2.foreach(println(_))*/
    //列表的常见操作
    // 将列表转化成为字符串
    //val ints = List(1, 2, 3, 4)
    //  得到list中的字符串
    /*val str: String = ints.toString()
    println(str)*/
    // 生成字符串操作。将元素拼接起来实现相关的代码操作实现
    //val str1: String = ints.mkString(",")
    //println(str1)
    // 集合的并集,交集以及差集元素的。
    val ints = List(1, 2, 3, 4)
    val ints1 = List(3, 4, 5, 6)
    //  交集操作
    ints.intersect(ints1).foreach(println(_))
    println("$$$$$$$$$$$$$$")
    // 并集操作实现.底层的操作是 a ++ b的操作实现的。元素没有进行去重操作的。可以使用distinct
    ints.union(ints1).distinct.foreach(println(_))
    println("$$$$$$$$$$$$$$")
    // 差集操作实现
    ints.diff(ints1).foreach(println(_))
  }
}
