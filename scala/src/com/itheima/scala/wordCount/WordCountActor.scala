package com.itheima.scala.wordCount


import java.io.{BufferedOutputStream, BufferedReader, File}

import scala.actors.Actor
import scala.io.Source

class WordCountActor extends  Actor {
  override def act(): Unit = {
      loop(
         react{
           case WordCountTask(fileName)=>{
             //  获取到文件名称。开始执行文件的count统计操作。将任务的信息返回给MainActor
             //1 .读取文件转换为列表  hadoop sqoop hadoop
             val list = Source.fromFile(fileName).getLines().toList
             //2.切割字符串转换为一个个的列表 (hadoop,sqoop)
             val words = list.flatMap(x => x.split(" "))
             //3.将单词转化为元祖  (hadoop,1)
             val tuples = words.map(x => x -> 1)
             //  分组之后进行聚合操作[单词分组] = {hadoop->List(hadoop->1, hadoop->1, hadoop->1), spark->List(spark ->1)}
             val unit: Map[String, Int] = WordCountUtil.reduce(tuples)
             //  将统计结果封装到样例类中返回给mainRator,mainRactor等到多有的actor返回结果。
             sender ! WordCountResult(unit)
           }
         }
      )
  }
}
