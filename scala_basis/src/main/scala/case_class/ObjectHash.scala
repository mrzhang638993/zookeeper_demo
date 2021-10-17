package src.main.scala.case_class
//对象的hash数值处理逻辑
object ObjectHash {
  case class  Person(var name:String,var age:Int)

  def main(args: Array[String]): Unit = {
    val value: Any = Person("zhangsan", 20)
    val zhangsan: Person = Person("zhangsan", 20)
    println(value.hashCode() ==zhangsan.hashCode())
  }
}
