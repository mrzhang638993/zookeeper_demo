package traits
import java.text.SimpleDateFormat
import java.util.Date
//测试特质的属性字段信息
// 没有实现的方法对应的是抽象方法的,不需要使用abstract来进行标注的。
object  TestTraitFields {
  def main(args: Array[String]): Unit = {
    val logger = new ConsoleLogger2
    logger.log("test demo")
  }
}
//定义特质实现相关的特质操作和实现逻辑
//在特质中定义属性和方法来完成相关的功能实现的
trait Loggers{
    // 定义了具体字段,给出了就提实现
    val format:SimpleDateFormat=new SimpleDateFormat("yyyy-MM-dd HH:mm")
    //属性是不存在抽象的,底层对应的是需要重写操作实现的
    //定义抽象字段，没有给出相关的实现和赋值操作
    val types:String
    def log(msg:String)
}
//实现类
class ConsoleLogger2 extends Loggers{
  override  val types: String ="控制台消息"
  override def log(msg: String): Unit = {
     //使用差值表达式俩完成更加复杂的字符串的拼接操作实现的。
     println(s"${types}:${format.format(new Date())}")
  }
}

