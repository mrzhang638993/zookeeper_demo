package src.main.scala.case_class
//测试样例类对象信息
object TestCaseClass {
//样例类的默认的属性是val的,是不允许修改变量的属性值信息的。
//样例类对象中定义的成员默认是val类型的数据的，是不可以更改的数据类型的。
case class  Person(var name:String,var age:Int)
//测试样例类信息
  def main(args: Array[String]): Unit = {
    val zhangsan = new Person("zhangsan", 20)
    println(zhangsan.age)
    println(zhangsan.name)
  }
}
