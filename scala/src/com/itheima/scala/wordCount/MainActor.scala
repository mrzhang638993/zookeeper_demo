package com.itheima.scala.wordCount
import java.io.File

import scala.concurrent.Future

object MainActor {

  def getDataFile(path:String):List[String]={
    val files = new File(path).list().toList
    files
  }

  def main(args: Array[String]): Unit = {
    val  path="F:\\works\\hadoop1\\zookeeper-demo\\scala\\src\\com\\itheima\\scala\\wordCount\\data"
    val files:List[String] = getDataFile(path)
    val paths = files.map(x => path + "\\" + x)
    val actors = paths.map {
      fileName => new WordCountActor()
    }
    //  将文件路径名称和actor拉链关联在一起
    val tuples = actors.zip(paths)
    println(tuples)
    // 启动actor，发送信息和接收信息。将文件名作为消息的形式发送给actor
    val futures = tuples.map(x => {
      //  发送异步有返回的信息。获取对应的名称
      //  启动actor
      x._1.start()
      val value = x._1 !!  WordCountTask(x._2)
      //  发送消息到元祖中，发送的是异步有返回值的消息
      value
    })
    //  检测所有的actor都已经完成任务返回
    //  futhur的数据还没有完成，需要继续等待
    while(futures.filter(p=>(!p.isSet)).size!=0){}
   //  将所有的结果转换成为WordCountResult对象
    val results = futures.map(_.apply().asInstanceOf[WordCountResult])
    val stringToInts1: List[Map[String, Int]] = results.map(_.wordCountMap)
    //  获取样例中的单词统计的结果。获取统计结果
    val stringToInt: Map[String, Int] = WordCountUtil.reduce(stringToInts1.flatten)
    //  进行结果合并操作
   println(stringToInt)
  }
}
