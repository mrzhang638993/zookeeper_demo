package com.itheima.spark.common

/**
 * 保存workActor的基本信息
 * */
case class WorkInfo(workId:String,
                    cpu:Int,
                    mem:Int,
                    lastHeartBeatTime:Long)
