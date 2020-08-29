package com.itheima.spark.master

import com.typesafe.config.{Config, ConfigFactory}

object ConfigUtil {

  private val config: Config = ConfigFactory.load("application")
  // 多长时间执行一次心跳检查的机制
  val `master.check.heartbeat.interval`: Int = config.getInt("master.check.heartbeat.interval")
  // 时间间隔多长时间认为是超时了
   val `master.check.heartbeat.timeout`: Int = config.getInt("master.check.heartbeat.timeout")


}
