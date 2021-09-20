package traits

//创建trait
trait Logger {
   def log(msg:String)
}
// 实现trait,实现自定义的方法操作和实现
class ConsoleLogger extends Logger{
   override def log(msg: String): Unit = {
       println(msg)
   }
}

