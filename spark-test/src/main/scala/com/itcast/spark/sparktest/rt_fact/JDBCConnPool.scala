package com.itcast.spark.sparktest.rt_fact

import org.apache.commons.dbcp.BasicDataSource
import org.apache.log4j.Logger

import java.sql.{Connection, PreparedStatement}


object JDBCConnPool {
  val log: Logger = Logger.getLogger(JDBCConnPool.getClass)
  var dataSource: BasicDataSource = null

  def getDataSource(): BasicDataSource = {
    if (dataSource == null) {
      dataSource = new BasicDataSource()
      dataSource.setDriverClassName(ConfigConstants.driver)
      dataSource.setUrl(ConfigConstants.url)
      dataSource.setUsername(ConfigConstants.user)
      dataSource.setPassword(ConfigConstants.password)
      //dataSource.(50)
      dataSource.setInitialSize(3)
      dataSource.setMinIdle(3)
      dataSource.setMaxIdle(10)
      //dataSource.setMaxWaitMillis(2 * 10000)
      dataSource.setRemoveAbandonedTimeout(180)
      //dataSource.setRemoveAbandonedOnBorrow(true)
      //dataSource.setRemoveAbandonedOnMaintenance(true)
      dataSource.setTestOnReturn(true)
      dataSource.setTestOnBorrow(true)
    }
    dataSource
  }

  /**
   * 释放数据源
   */
  def closeDataSource() = {
    if (dataSource != null) {
      dataSource.close()
    }
  }

  /**
   * 获取数据库连接
   *
   * @return
   */
  def getConnection(): Connection = {
    var conn: Connection = null
    try {
      if (dataSource != null) {
        conn = dataSource.getConnection()
      } else {
        conn = getDataSource().getConnection()
      }
    } catch {
      case e: Exception =>
        log.error(e.getMessage(), e)
    }
    conn
  }

  /**
   * 关闭连接
   */
  def closeConnection(ps: PreparedStatement, conn: Connection) {
    if (ps != null) {
      try {
        ps.close();
      } catch {
        case e: Exception =>
          log.error("预编译SQL语句对象PreparedStatement关闭异常！" + e.getMessage(), e);
      }
    }
    if (conn != null) {
      try {
        conn.close();
      } catch {
        case e: Exception =>
          log.error("关闭连接对象Connection异常！" + e.getMessage(), e);
      }
    }
  }
}
