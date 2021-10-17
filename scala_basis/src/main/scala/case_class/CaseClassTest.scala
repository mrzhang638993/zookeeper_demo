package src.main.scala.case_class
//样例类测试代码和实现机制
object CaseClassTest {
//创建样例类对象信息
trait Sex //定义特质
case object  Female extends Sex//创建样例对象,对应的是枚举的操作
case object  Male   extends Sex//创建样例对象,对应的枚举的操作
case class  Person(sex:Sex,name:String)//创建样例类对象,定义相关的属性信息
  def main(args: Array[String]): Unit = {
    val zhangsan: Person = Person(Male, "张三")
    val lisi: Person = Person(Female, "lisi")
    println(zhangsan.sex)
    println(lisi.sex)
  }
}
