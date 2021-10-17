package moduleMatch
//匹配集合元素信息
object MatchArrsy {
  def main(args: Array[String]): Unit = {
    //匹配集合元素信息
    val num: Array[Int] = Array(1, 2, 3, 4, 5)
    val zero: Array[Int] = Array(0)
    val zeroBegin: Array[Int] = Array(0, 7, 8, 9, 10)
    zeroBegin match {
      case Array(1,_*)=>println("匹配到的是元素1开头的数组元素")
      case Array(0)=>println("匹配单个元素0的集合")
      case Array(0,_*)=>println("匹配到了元素是0开头的元素信息")
      case _=>println("没有匹配到任何元素")
    }
  }
}
