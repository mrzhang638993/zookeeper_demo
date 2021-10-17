package partialFunction

object PartialFunctionDemo {
  def main(args: Array[String]): Unit = {
     //定义偏函数,实现偏函数的调用操作实现。可以实现偏函数的相关的语句操作实现
     val partialFunction: PartialFunction[Int,String] = {
       case 1 => "一"
       case 2 => "二"
       case 3 => "三"
       case _ => "其他"
     }
    //偏函数的调用操作
    println(partialFunction(1))
    println(partialFunction(2))
    println(partialFunction(4))
  }
}
