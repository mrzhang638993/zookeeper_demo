package src.main.scala.case_class
//完成相关的样例类代码测试
object CaseClassDemo {
case class  Person(var name:String,var age:Int)
//进行样例类代码实现操作
//样例类的创建方式
def main(args: Array[String]): Unit = {
  val zhangsan: Person = Person("zhangsan", 20)
  zhangsan.age=30
  println(zhangsan.age)
}
}
