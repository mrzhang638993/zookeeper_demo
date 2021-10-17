package moduleMatch
//匹配元祖数据信息
object MatchTurple {
//匹配元祖数据信息
  def main(args: Array[String]): Unit = {
    //匹配元祖数据信息
    val tuple: (Int, Int, Int) = (1, 2, 3)
    val zero:(Int) = (0)
    val tuple1: (Int, Int, Int) = (0, 3, 5)
    zero match {
      case (0)=>println("匹配到了单个元素的元祖0")
      //case (0,_*)=>println("匹配到了0开始的元素信息")
      case _=>println("没有匹配到任何的元素")
    }
  }
}
