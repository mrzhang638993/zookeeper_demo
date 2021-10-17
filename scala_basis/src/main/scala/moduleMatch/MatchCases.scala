package moduleMatch
//匹配样例类数据信息
object MatchCases {
  def main(args: Array[String]): Unit = {
      //匹配样例类对象信息
    case class Customer(name:String,age:Int)
    case class Order(id:String)
    val  cust:Any=Customer("zhangsan",20)
    val  order:Any=Order("34567")
    //匹配样例类数据信息
    order match {
      case Customer(name,age)=>println(s"${name}:${age}")
      case Order(id)=>println(s"${id}")
      case _=>println("good baby")
    }
  }
}
