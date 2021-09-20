package traits
//测试object继承了相关的trait的操作实现
//对象继承了相关的trait特质的操作实现的,这个是需要进行关注和理解操作的
object ObjectTrait {
  def main(args: Array[String]): Unit = {
    println(ConsoleLogger1.log("测试object message"))
  }
}
//定义特质的相关操作
trait  Logger1{
    def log(msg:String):String
}
object ConsoleLogger1 extends Logger1{
  override def log(msg: String): String = {
      "控制台消息:"+msg
  }
}
