package com.itheima

/**
 * map映射操作逻辑
 * */
object MapOperation {

  def main(args: Array[String]): Unit = {
    //获取对应姓名下面的年龄信息，对应的默认的是不可变的数据集合的。
   /* val nameAge = Map("zhangsan" -> 30, "lisi" -> 40)
    println(nameAge.get("zhangsan").getOrElse(0))
    println(nameAge.get("lisi").getOrElse(0))
    // 获取map中的元素信息，获取map中对应的元素的实例
    println(nameAge("zhangsan"))*/
    // 使用可变的map操作,对应的是map中的数据结构信息的.可变的map是可以赋值操作的。
    /*val changeMap: mutable.Map[String, Int] = scala.collection.mutable.Map("zhangsan" -> 20, "lisi" -> 30)
    changeMap("zhangsan")=30 // 给可变的map赋值操作实现
    println(changeMap("zhangsan"))*/
    //迭代器数据信息,迭代遍历和访问元素信息
    val ints = List(3, 4, 5)
    /*val iterator: Iterator[Int] = ints.iterator*/
    //  迭代器迭代完成之后,是会对应的位于元素的最后的位置的。
   /* while(iterator.hasNext){
      val i: Int = iterator.next()
      println(i)
    }*/
    // 还可以使用for循环来获取得到对应的迭代器的操作实现的。需要重新获取到新的迭代器实现的
    for(i <- ints.iterator)println(i)
  }
}
