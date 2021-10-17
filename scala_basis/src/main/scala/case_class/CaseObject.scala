package case_class
//测试样例类对象信息
object CaseObject {
  //定义样例类对象信息,定义成员变量信息,默认的是不可变的样例类对象的
  case class Person(var name: String, var age: Int)
  //创建样例类对象信息
  def main(args: Array[String]): Unit = {
    val zhangsan: Person = Person("张三", 20)
    println(zhangsan.name)
    println(zhangsan.age)
  }
}
