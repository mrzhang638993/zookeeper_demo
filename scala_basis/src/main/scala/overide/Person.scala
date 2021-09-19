package overide

class Person {
  val name:String="hello"
  def getName():String=name
}
//需要明白一整个的体系结构才可以认识到overide的机制和相关的体系的。
class Student extends Person{
  //scala中属性的覆盖是有要求的，只能覆盖val修饰的属性的。
  //在overide中，子类是无法直接访问到父类的覆盖属性的。需要使用方法来完成相关的调用操作的。
  override val name: String = "good morning"
  override def getName(): String = {
      super.getName()+"===="+"morning"
  }
}
//测试方法以及属性的重写操作实现
object StudentObject{
  def main(args: Array[String]): Unit = {
    // 对应的获取父类的信息
    val person = new Person
    println(person.name)
    // 处理子类的数据信息
    val student = new Student
    println(student.getName())
  }
}