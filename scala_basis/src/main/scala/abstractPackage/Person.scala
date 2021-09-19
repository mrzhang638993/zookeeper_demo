package abstractPackage

//定义抽象类
abstract  class Person {
    def sayHello()
}
object TestAbstract{
  //定义抽象内部类,实现相关的抽象方法
  def main(args: Array[String]): Unit = {
    //定义抽象的匿名内部类实现相关的操作
    val person: Person = new Person {
      override def sayHello(): Unit = {
        println("abstract hello")
      }
    }
    person.sayHello()
  }
}
