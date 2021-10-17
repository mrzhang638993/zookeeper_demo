package partialFunction


//偏函数实现操作机制
object PartialFunctionDemo1 {
  def main(args: Array[String]): Unit = {
    val list: List[Int] = (1 to 10).toList
    //定义偏函数实现相关语句的操作和实现
    //偏函数中是不能使用守卫操作的
    val stringList: List[String] = list.map(
      //内部使用偏函数操作,一个数据的输入,一个数据的输出操作的。使用偏函数实现更加简洁的操作
      {
        case x if x >= 1 && x <= 3 => "[1-3]"
        case x if x >= 4 && x <= 8 => "[4-8]"
        case _ => "[8-*]"
      }
    )
    println(stringList)
  }
}
