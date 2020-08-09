package cn.itcast.zookeeper_api.redis;

import org.junit.Test;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

/**
 * jedis实际的具体的操作类
 * */
public class JedisOperator {

    /**
     *  连接redis
     * */
    @Test
    public  void  connectJedis(){
        // 创建redis的连接池对象
        JedisPoolConfig jedisPoolConfig=new JedisPoolConfig();
        // 设置最大空闲数为10个
        jedisPoolConfig.setMaxIdle(10);
        // 连接超时的最大时间，ms
        jedisPoolConfig.setMaxWaitMillis(5000);
        //  设置redis连接的最大的客户端数
        jedisPoolConfig.setMaxTotal(50);
        JedisPool  jedisPool=new JedisPool(jedisPoolConfig,"node03",6379);
        //  获取jedis的客户端操作
        Jedis resource = jedisPool.getResource();
    }
}