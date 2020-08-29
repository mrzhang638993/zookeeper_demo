package com.itheima.spark.worker

import com.typesafe.config.{Config, ConfigFactory}

/**
 * 读取配置文件的内容
 */
object ConfigUtil {
  private val config: Config = ConfigFactory.load("application")
   val `worker.heartbeat.interval`=config.getInt("worker.heartbeat.interval")
}
