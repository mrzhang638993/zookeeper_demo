package com.itcast.spark.sparktest.rt

import org.apache.commons.pool2.impl.GenericObjectPoolConfig
import org.apache.kafka.common.TopicPartition
import redis.clients.jedis.JedisPool


class RedisUtils extends Serializable {
  @transient private var pool: JedisPool = null

  def makePool(redisHost: String, redisPort: Int, redisTimeout: Int,
               maxTotal: Int, maxIdle: Int, minIdle: Int): Unit = {
    makePool(redisHost, redisPort, redisTimeout, maxTotal, maxIdle, minIdle, true, false, 10000)
  }

  def makePool(redisHost: String, redisPort: Int, redisTimeout: Int,
               maxTotal: Int, maxIdle: Int, minIdle: Int, testOnBorrow: Boolean,
               testOnReturn: Boolean, maxWaitMillis: Long): Unit = {
    if (pool == null) {
      val poolConfig: GenericObjectPoolConfig = new GenericObjectPoolConfig
      poolConfig.setMaxTotal(maxTotal)
      poolConfig.setMaxIdle(maxIdle)
      poolConfig.setMinIdle(minIdle)
      poolConfig.setTestOnBorrow(testOnBorrow)
      poolConfig.setTestOnReturn(testOnReturn)
      poolConfig.setMaxWaitMillis(maxWaitMillis)
      // 无密码
      pool = new JedisPool(poolConfig, redisHost, redisPort, redisTimeout)
      // 有密码
      // pool = new JedisPool(poolConfig, redisHost, redisPort, redisTimeout, "qwer1234")

      val hook = new Thread {
        override def run = pool.destroy()
      }
      sys.addShutdownHook(hook.run)
    }
  }

  def getPool: JedisPool = {
    assert(pool != null)
    pool
  }

  /** *
   * 初始化redis配置
   */
  def initRedisPool() = {
    val maxTotal = 20
    val maxIdle = 10
    val minIdle = 1
    val redisHost = "xc-online-redis"
    val redisPort = 6379
    val redisTimeout = 30000
    makePool(redisHost, redisPort, redisTimeout, maxTotal, maxIdle, minIdle)
  }


  /** *
   * 存储结果集
   *
   * @param key
   * @param keyinfo
   * @param value
   */
  def storeResultRedis(key: String, keyinfo: String, value: String): Unit = {
    val jedis = getPool.getResource
    val ppl = jedis.pipelined()
    ppl.multi() //开启事务
    ppl.hset(key, keyinfo, value)
    ppl.exec() //提交事务
    ppl.sync //关闭pipeline
    jedis.close()
  }

  /**
   * 异常重启的情况, 获取redis中已有的数据
   */
  def getResultRedis(key: String, keyinfo: String): String = {
    val jedis = getPool.getResource
    val ppl = jedis.pipelined()
    val value = ppl.hget(key, keyinfo)
    jedis.close()
    value.get()
  }

  /**
   * 从redis中获取上次消费的offset
   *
   * @param rt_offset_key redis中offset key
   * @param topicName     kafka中topicName
   * @param partitions    kafka分区参数
   * @return
   */
  def getLastCommittedOffsets(rt_offset_key: String, topicName: String, partitions: Int): Map[TopicPartition, Long] = {
    // 获取Jedis
    val jedis = getPool.getResource
    // 初始化map,保存结果
    val offsetMap = collection.mutable.HashMap.empty[TopicPartition, Long]
    // 对分区遍历
    for (partition <- 0 to partitions - 1) {
      // 组装字段
      val topic_partition_key = topicName + "_" + partition
      // 获取具体分区值
      val lastOffsetTmp = jedis.hget(rt_offset_key, topic_partition_key)
      // 判断值 是否存在， 不存在给默认值 0
      val lastOffset = if (lastOffsetTmp == null) 0L else lastOffsetTmp.toLong
      // 保存到map
      offsetMap += (new TopicPartition(topicName, partition) -> lastOffset)
    }
    offsetMap.toMap
  }


  /** *
   * 存储offset
   *
   * @param key     redis中offset key
   * @param keyinfo top分区key
   * @param value   offset值
   */
  def storeOffsetRedis(key: String, keyinfo: String, value: Long): Unit = {
    // 获取Jedis
    val jedis = getPool.getResource
    // 开启事务
    val ppl = jedis.pipelined()
    ppl.multi()
    // 保存
    ppl.hset(key, keyinfo, value.toString)
    // 提交事务
    ppl.exec()
    ppl.sync()
    // 关流
    jedis.close()

  }
}
