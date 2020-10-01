package com.itheima.scala.matchStu

object _07MatchDemo {

  /**
   * 使用模式匹配获取数组中的元素，获取属性值
   * */
  def main(args: Array[String]): Unit = {
     // 获取数组中的元素
     val array = (0 to 10).toArray
     val Array(_,x,y,z,_*)=array
     println(x)
     println(y)
     println(z)
    //  获取元祖中的元素
    var tuple=("zhangsan"->20,"lisi"->30,"wangwu"->40,"zhaoliu"->50)
    // _代表的是单个的元素。*代表的是任意多个的
    //var (_,t1,t2,_*)=tuple
    //println(t1)
    //println(t2)
    //println(t3)
    //  定义列表
    val list = (1 to 10).toList
    //  使用模式匹配，匹配list中的元素
    var x1::y1::tail=list
    println(x1)
    println(y1)
    //  匹配,模式匹配中不能使用tail，需要的是模式
    var List(x2,y2,_*)=list
    println(x2)
    println(y2)
  }
}
