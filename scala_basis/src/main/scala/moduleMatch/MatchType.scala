package src.main.scala.moduleMatch
//switch模式匹配,匹配数据类型
//根据类型匹配，不同的类型匹配不同的数据结果。
object MatchType {
  def main(args: Array[String]): Unit = {
    //定义Any类型的变量信息,"hadoop",1,1.0
    val a:Any="hadoop"
    /*val b:Any=1
    val c:Any=1.0*/
    //对应的显示:你是字符串信息
    a match {
      //根据类型进行匹配操作,后面不需要引用变量的话,可以使用下划线来简化操作
      //case x:String=>println(s"${x}你是字符串")
        //变量不需要在后续的代码中使用到的话,可以使用_来代替变量的
      case _:String=>println("你是字符串类型的数据")
      case x:Int=>println(s"${x}你是整形数据")
      case x:Double=>println(s"${x}你是double数据类型")
      case _=>println(s"其他的类型信息")
    }
  }
}
