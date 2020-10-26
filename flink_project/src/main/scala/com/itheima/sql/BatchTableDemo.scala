package com.itheima.sql

import org.apache.flink.api.scala.ExecutionEnvironment
import org.apache.flink.table.api.{Table, TableEnvironment, Types}
import org.apache.flink.table.api.scala.BatchTableEnvironment
import org.apache.flink.api.scala._
import org.apache.flink.core.fs.FileSystem.WriteMode
import org.apache.flink.table.sinks.CsvTableSink
import org.apache.flink.table.sources.CsvTableSource
/**
 * 执行批处理操作实现
 * */
object BatchTableDemo {

  def main(args: Array[String]): Unit = {
    // 获取流处理环境执行操作实现
    val env: ExecutionEnvironment = ExecutionEnvironment.getExecutionEnvironment
    //  设置并行度
    env.setParallelism(1)
    //  获取table的运行环境
    val tableEnv: BatchTableEnvironment = TableEnvironment.getTableEnvironment(env)
    //3. 加载外部CSV文件
    val source: CsvTableSource = CsvTableSource.builder().path("./dataset/score.csv")
      .field("id",Types.INT)
      .field("name",Types.STRING)
      .field("subjectId",Types.INT)
      .field("score",Types.DOUBLE)
      .fieldDelimiter(",")  // 属性间的分隔符
      .lineDelimiter("\n")  // 换行符进行操作实现
      .ignoreParseErrors()
      .build()
    //4. 将外部数据构建成表
    tableEnv.registerTableSource("score",source)
    //5. 使用table方式查询数据
    val table: Table = tableEnv.scan("score").select("id,name,subjectId,score")
      .where("name=='张三'")
    //6. 打印表结构
    table.printSchema()
    //7. 将数据落地到新的CSV文件中
    // * @param path The output path to write the Table to.
    //  * @param fieldDelim The field delimiter
    //  * @param numFiles The number of files to write to
    //  * @param writeMode The write mode to specify whether existing files are overwritten or not.
    //  */
    table.writeToSink(new CsvTableSink("./dataset/new_score.csv",",",1,WriteMode.OVERWRITE))
    //8. 执行任务
    env.execute()
  }
}
