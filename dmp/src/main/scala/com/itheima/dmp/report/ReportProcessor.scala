package com.itheima.dmp.report

import org.apache.kudu.Schema
import org.apache.spark.sql.DataFrame

/**
 * 报表处理的基类，
 * 所有的报表处理都需要继承这个类来实现的。
 **/
trait ReportProcessor {

  /**
   * 对外提供原表的表名称
   **/
  def sourceTableName(): String

  /**
   * 数据处理的操作实现
   *
   * @param
   *
   **/
  def process(dataFrame: DataFrame): DataFrame

  /**
   * 返回目标表的名称
   **/
  def targetTableName(): String

  /**
   * 提供目标表的schema信息
   **/
  def targetSchema(): Schema

  /**
   * 提供目标表的分区键
   **/
  def targetTableKeys(): List[String]

}
