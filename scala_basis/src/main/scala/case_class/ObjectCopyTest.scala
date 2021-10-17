package src.main.scala.case_class
//测试对象拷贝操作实现
//使用copy完成对象的操作和复制实现的。使用带名参数实现。
object ObjectCopyTest {
  case class Person(name:String,age:Int)
  def main(args: Array[String]): Unit = {
    //多学习一下知识
    val lisi: Person = Person("李四", 21)
    val wangwu: Person = lisi.copy(name = "王五")
    //拷贝之后对应的属性信息是李四,25
    println(wangwu.age)
    println(wangwu.name)
  }
}
