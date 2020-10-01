package com.itheima.scala.wordCount
import java.io.File
object MainActor {

  def getDataFile(path:String):List[String]={
    // list放回目录下面文件的名称
    val files = new File(path).list().toList
    files
  }

  def main(args: Array[String]): Unit = {
    val  path="F:\\works\\hadoop1\\zookeeper-demo\\scala\\src\\com\\itheima\\scala\\wordCount\\data"
    val files:List[String] = getDataFile(path)
    //  将文件名称拼接为完整的文件名称，包含路径的名称.
    val paths = files.map(x => path + "\\" + x)
    // 每一个路径对应的创建一个actor对象
    val actors = paths.map {
      fileName => new WordCountActor()
    }
    //  将文件路径名称和actor拉链关联在一起，匹配形成(actor,name)这样的数据结构
    val tuples = actors.zip(paths)
    // 启动actor，发送信息和接收信息。将文件名作为消息的形式发送给actor
    val futures = tuples.map(x => {
      //  启动actor
      x._1.start()
      //  对应的是mainActor发送信息，x._1对应的actor接收的。
      val value = x._1 !!  WordCountTask(x._2)
      //  发送消息到元祖中，发送的是异步有返回值的消息
      value
    })
    //  futhur的数据还没有完成，需要继续等待
    while(futures.filter(p=>(!p.isSet)).size!=0){}
   //  将所有的结果转换成为WordCountResult对象。List(WordCountResult(List[Map[String, Int]),WordCountResult(List[Map[String, Int]))
    val results = futures.map(_.apply().asInstanceOf[WordCountResult])
    // 取出list中的每一个WordCountResult的数据
    val stringToInts1: List[Map[String, Int]] = results.map(_.wordCountMap)
    //  获取样例中的单词统计的结果。获取统计结果List[Map[String, Int]]--->Map[String, Int].将全部的(key，value)散列开来
    val stringToInt: Map[String, Int] = WordCountUtil.reduce(stringToInts1.flatten)
    //  进行结果合并操作
   println(stringToInt)
  }
}
