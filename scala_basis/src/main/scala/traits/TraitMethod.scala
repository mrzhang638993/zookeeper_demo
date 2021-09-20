package traits
//接口中定义实现方法
trait TraitMethod {
  //在特质中定义具体的实现方法
  def log(msg:String)={
     println(msg)
  }
}
//定义一个类实现相关的特质操作
class UserService extends TraitMethod{
   def add(): Unit ={
      log("添加用户")
   }
}
//增加对应的测试类信息
//添加用户信息
object TestTraitMethod{
  def main(args: Array[String]): Unit = {
    val service = new UserService
    service.add()
  }
}


