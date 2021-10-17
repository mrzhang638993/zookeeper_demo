package moduleMatch
//匹配列表数据信息
object MatchList {
  //匹配元素信息,包含了相关的元素信息
  def main(args: Array[String]): Unit = {
    val nums = List(1, 2, 3, 4, 5)
    val first = List(1)
    val ints = List(1, 2, 3)
    ints match {
      //需要注意元素匹配的顺序和相关的操作逻辑。_*对应的是需要放到的是末尾的元素的，那样的话才可以更加的准确的。
      case List(1,_*)=>println("匹配元素1开头的元素")
      case List(1)=>println("匹配元素1")
      case List(1,x,y)=>println(s"匹配元素1开始的3个原始,${x}:${y}")
      case _=>println("未匹配到任何元素")
    }
  }
}
