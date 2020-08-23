package com.itheima.scala.matchStu

object  _07TurpleObject {
  /**
   * 元祖进行匹配操作
   * */
  def main(args: Array[String]): Unit = {
    // 定义元祖
    val tuple = ("zhangsan"->20,"lisi"->30,"wangwu"->40)
    val tuple1 = ("beijing" -> 50, "nanjing" -> 60, "taiji" -> 70)
    tuple match {
        //  注意在这里面不能使用"zhangsan"->20代替元祖的。进行复杂的匹配的。匹配最后面的元素以及匹配无法的数据
      case (("zhangsan",20),x,y)=>println("匹配zhangsan-20开头,后面元素位置不固定的元素")
      // 匹配元素开头的，后面任意多个元祖的元祖。元祖匹配
      // case (("zhangsan",20),*)=>println("测试多个元素的匹配的")
      //case (x,y,("taiji",70))=>println(s"匹配三个元素，最后一个元素是taiji->70的元素的,${x}:${y}")
      case _x=>println("没有匹配到任意的操作的")
    }
  }
}
