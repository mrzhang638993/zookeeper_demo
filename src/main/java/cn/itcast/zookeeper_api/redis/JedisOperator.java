package cn.itcast.zookeeper_api.redis;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * jedis实际的具体的操作类
 * */
public class JedisOperator {
    private JedisPool jedisPool;
    private Jedis jedis;

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
        jedisPool=new JedisPool(jedisPoolConfig,"192.168.1.203",6379);
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
        //  修改hash的数据结构
        Long hset = jedis.hset("key1", "field1", "linevalue");
        System.out.println("修改hash数据结构"+hset);
        //  获取所有的key
        Set<String> key11 = jedis.hkeys("key1");
        for(String key:key11){
            System.out.println(key);
        }
        //  获取所有的value
        List<String> key12 = jedis.hvals("key1");
        key12.stream().forEachOrdered(x->{
            System.out.println("=====获取所有的数值====="+key12);
        });
    }

    /**
     * 对于set集合进行操作实现
     * */
    @Test
    public void listOperate(){
        jedis = jedisPool.getResource();
        //  list元素会不断的增加的，需要进行关注的。
        jedis.lpush("listkey","listvalue1","listvalue2","listvalue3");
        // 获取下标为0,1的.listvalue3,listvalue2
        List<String> listkey = jedis.lrange("listkey", 0, 1);
        listkey.forEach(x->{
            System.out.println(x);
        });
        String listkey1 = jedis.rpop("listkey");
        System.out.println(listkey1+"从右边弹出元素");
        String listkey2 = jedis.lpop("listkey");
        System.out.println(listkey2+"从左边弹出元素");
        //  lpushx，linsert等的操作的。
    }

    /***
     * 对set集合进行操作
     */
    @Test
     public void testSet(){
        jedis=jedisPool.getResource();
        jedis.sadd("setkey","setvalue1","setvalue2","setvalue3","setvalue4");
         Set<String> setkey = jedis.smembers("setkey");
         for (String value:setkey
              ) {
             System.out.println(value);
         }
         // 移除元素setvalue1
        Long srem = jedis.srem("setkey1", "setvalue1");
        jedis.sadd("setkey","setvalue1","setvalue2","setvalue3","setvalue4");
        Set<String> setkey1 = jedis.smembers("setkey");
        for (String value:setkey1
        ) {
            System.out.println(value);
        }
        //  还有其他的操作方式实现的。
    }


    @After
    public void close(){
        if(jedis!=null){
            jedis.close();
        }
    }
}