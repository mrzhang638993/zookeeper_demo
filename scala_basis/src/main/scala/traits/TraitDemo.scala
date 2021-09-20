package traits
//测试相关的特质的代码和实践操作实现的
trait Loggers12 {
   def log(msg:String)
   def info(msg:String)=log("信息:"+msg)
   def warn(msg:String)=log("警告:"+msg)
   def error(msg:String)=log("错误:"+msg)
}
//定义具体的实现操作
//使用对应的模板设计模式来实现代码的操作和实现的。这个是正常的操作实现的。
class ConsoleLoggers extends Loggers12{
  //实现log的相关的语句操作和实现
  override def log(msg: String): Unit = println(msg)
}
//实现相关的代码逻辑实现
//使用模板设计模式来实现代码更加高效的可扩展操作实现的。
object ConsoleDemo{
  def main(args: Array[String]): Unit = {
    val loggers = new ConsoleLoggers
    loggers.info("Call action method")
    loggers.warn("person 变量没有被使用")
    //提示空指针异常数据信息
    loggers.error("Null Pointer Exception")
  }
}

