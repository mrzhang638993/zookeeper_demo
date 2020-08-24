package com.itheima.scala.wordCount

case object WordCountUtil {
  def reduce(wordCountList: List[(String, Int)]): Map[String, Int] = {
    //  根据key进行分组，得到的结果是Map[key, List[(key, Int)]]
    val keyTuples: Map[String, List[(String, Int)]] = wordCountList.groupBy((_._1))
    val stringToInt1: Map[String, Int] = keyTuples.map {
          //  得到的结果是(key,count)
      keyValue => keyValue._1 -> keyValue._2.map(_._2).sum
    }
    // 最终进行聚合计算
    stringToInt1
  }
}
