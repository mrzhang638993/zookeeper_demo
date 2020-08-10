package cn.itcast.zookeeper_api.redis;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

import java.util.Map;
import java.util.Set;

/**
 * jedis实际的具体的操作类
 * */
public class JedisOperator {

    private   JedisPool jedisPool;
    private   Jedis  jedis;

    /**
     *  连接redis
     * */
    @Before
    public  void  connectJedis(){
        // 创建redis的连接池对象
        JedisPoolConfig jedisPoolConfig=new JedisPoolConfig();
        // 设置最大空闲数为10个
        jedisPoolConfig.setMaxIdle(10);
        // 连接超时的最大时间，ms
        jedisPoolConfig.setMaxWaitMillis(5000);
        //  设置redis连接的最大的客户端数
        jedisPoolConfig.setMaxTotal(50);
        jedisPool=new JedisPool(jedisPoolConfig,"node03",6379);
    }

    @Test
    public  void stringOperate(){
        jedis = jedisPool.getResource();
        jedis.set("jediskey","jedisvalue");
        String jediskey = jedis.get("jediskey");
        System.out.println(jediskey);
        // 计数器操纵,key不存在的话也是会创建的。
        jedis.incr("jincr");
        String jincr = jedis.get("jincr");
        System.out.println(jincr);
        Long strlen = jedis.strlen(jediskey);
        System.out.println(strlen);
    }

    /**
     * 测试hash列表，hash表的操作
     * */
    @Test
    public void hashTest(){
        jedis = jedisPool.getResource();
        jedis.hset("key1","field1","fieldValue1");
        jedis.hset("key1","field2","fieldValue2");
        String hget = jedis.hget("key1", "field1");
        String hget1 = jedis.hget("key1", "field2");
        System.out.println(hget);
        System.out.println(hget1);
        // 对应的获取所有的key和value的
        Map<String, String> key1 = jedis.hgetAll("key1");
        key1.entrySet().forEach((x)->{System.out.println(x.getKey()+"=="+x.getValue());});
        //获取所有的key以及所有的value操作逻辑
        Set<String> key11 = jedis.hkeys("key1");
        
    }

    @After
    public void close(){
        if(jedis!=null){
            jedis.close();
        }
    }
}