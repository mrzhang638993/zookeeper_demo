package traits
//对应的trait继承class,获取和使用相关class的数据信息
object TraitExtendedClass {
   class MyUtils{
      def printMsg(msg:String):Unit=println(msg)
   }
  trait  Logger extends MyUtils{
     def log(msg:String):Unit=super.printMsg(msg)
  }
  class Person(name:String) extends Logger {
    def sayHello(): Unit = {
      super.log(s"${name}")
    }
  }
//相关的代码实现逻辑
  def main(args: Array[String]): Unit = {
    val good = new Person("good")
    good.sayHello()
  }
}
