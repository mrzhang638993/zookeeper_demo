package com.itheima.broad

import java.io.File

import org.apache.commons.io.FileUtils
import org.apache.flink.api.common.functions.RichMapFunction
import org.apache.flink.api.scala.{ExecutionEnvironment, _}
import org.apache.flink.configuration.Configuration

/**
 * 使用分布式缓存进行操作实现
 **/
object CacheFile {

  def main(args: Array[String]): Unit = {
    //  定义执行环境和相关的操作实现
    val env: ExecutionEnvironment = ExecutionEnvironment.createLocalEnvironment(2)
    // 加载集合数据执行操作实现
    val sourceData: DataSet[String] = env.fromCollection(List("a", "b", "c", "d"))
    // 注册分布式缓存文件
    env.registerCachedFile("F:\\works\\hadoop1\\zookeeper-demo\\flink_project\\src\\main\\scala\\dataset\\data.txt", "data.txt")
    // 下面显示使用分布式缓存操作技术实现
    val sinkValue: DataSet[String] = sourceData.map(new RichMapFunction[String, String] {
      var file: File = null;

      //  注册和获取分布式缓存数据
      override def map(value: String): String = {
        value
      }

      override def open(parameters: Configuration): Unit = {
        file = getRuntimeContext.getDistributedCache.getFile("data.txt")
        val fileContent: String = FileUtils.readFileToString(file)
        println(fileContent)
      }
    })
    sinkValue.writeAsText("./dataset/distributeCache")
    // 文件执行操作实现
    env.execute("fileContent")
  }
}
