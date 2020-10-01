package com.itheima.spark.common

/**
 * workerActor注册信息
 * */
case class WorkRegisterMessage(workId:String,
                               cpu:Int,
                               mem:Int)
