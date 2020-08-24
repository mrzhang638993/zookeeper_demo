package com.itheima.scala.wordCount

case object WordCountUtil {
  def reduce(wordCountList: List[(String, Int)]): Map[String, Int] = {
    val keyTuples: Map[String, List[(String, Int)]] = wordCountList.groupBy((_._1))
    val stringToInt1: Map[String, Int] = keyTuples.map {
      keyValue => keyValue._1 -> keyValue._2.map(_._2).sum
    }
    // 最终进行聚合计算
    stringToInt1
  }
}
