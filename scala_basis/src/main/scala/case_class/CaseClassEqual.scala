package src.main.scala.case_class
//测试样例类数据是否相等操作

object CaseClassEqual {
case class Person(name:String,age:Int)
  def main(args: Array[String]): Unit = {
    val zhangsan: Person = Person("zhangsan", 20)
    val zhangsan1: Person = Person("zhangsan", 20)
    //样例类的相等,比较的是样例类的相关的属性信息,属性相等则整个的样例类都相等。
    println(zhangsan.equals(zhangsan1))
    //eq对应的比较的是两个对象的对象引用是否相等的操作
    //比较对象引用的话,对象的引用值是不相等的操作的。
    println(zhangsan.eq(zhangsan1))
  }
}
