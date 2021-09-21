package traits
//定义相关的特质操作实现
trait Loggeres {
   def log(msg:String)
}
class UserServerviceLogger
object TestHunru{
  def main(args: Array[String]): Unit = {
    //定义对象的时候混入相关的特质的操作逻辑
    //其本质有点类似于抽象类的实现逻辑的。
    val logger = new UserServerviceLogger with Loggeres {
      override def log(msg: String): Unit = {
         println(s"测试相关的混入操作逻辑和实现:${msg}")
      }
    }
    //执行数据的打印操作实现逻辑和相关的体现
    logger.log("hello world")
  }
}
