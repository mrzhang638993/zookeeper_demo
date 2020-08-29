package com.itheima.spark.common

/**
 * 心跳检测消息
 * */
case class WorkerHeartBeatMessage(workId:String,cpu:Int,mem:Int)

