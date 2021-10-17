package moduleMatch

import scala.io.StdIn

//实现守卫代码操作
object Guart {
  def main(args: Array[String]): Unit = {
     //读取数据信息
     val num: Int = StdIn.readInt()
     num match {
       case x if x>=0 && x<=3 =>println("[0,3]")
       case x if x>=4 && x<=8=>println("[4,8]")
       case _=>println("other things")
     }
  }
}
