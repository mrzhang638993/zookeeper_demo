package cn.itcast.zookeeper_api.redis;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import redis.clients.jedis.*;

import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * jedis实际的具体的操作类
 * */
public class JedisOperator {
    private JedisPool jedisPool;
    private Jedis jedis;
    private  JedisSentinelPool jedisSentinelPool;
    private JedisCluster jedisCluster;

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


    /**
     * 哨兵模式下面的redis代码逻辑
     * */
    @Test
    public  void testSentinel(){
        //String masterName, Set<String> sentinels.存储的是host以及port的数据
        HashSet<String> nodes = new HashSet<>();
        nodes.add("192.168.1.201:26379");
        nodes.add("192.168.1.202:26379");
        nodes.add("192.168.1.203:26379");
        JedisPoolConfig poolConfig=new JedisPoolConfig();
        //  默认超时时间是2000秒
        poolConfig.setMinIdle(5);
        poolConfig.setMaxTotal(30);
        poolConfig.setMaxWaitMillis(3000);
        poolConfig.setMaxIdle(10);
        jedisSentinelPool=new JedisSentinelPool("mymaster",nodes,poolConfig);
        //  获取jedis连接
        jedis = jedisSentinelPool.getResource();
        // 使用哨兵模式加入key进行操作。
        jedis.set("jediskey","jedisvalue");
    }

    /**
     * java  api 操作redis集群
     * */
    @Test
    public void testCluster(){
        // Set<HostAndPort> nodes, final GenericObjectPoolConfig poolConfig
        HostAndPort hostAndPort=new HostAndPort("192.168.1.201",6379);
        HostAndPort hostAndPort1=new HostAndPort("192.168.1.201",6380);
        HostAndPort hostAndPort2=new HostAndPort("192.168.1.201",6381);
        HostAndPort hostAndPort3=new HostAndPort("192.168.1.201",6382);
        HostAndPort hostAndPort4=new HostAndPort("192.168.1.202",6379);
        HostAndPort hostAndPort5=new HostAndPort("192.168.1.202",6380);
        HostAndPort hostAndPort6=new HostAndPort("192.168.1.203",6379);
        HostAndPort hostAndPort7=new HostAndPort("192.168.1.203",6380);
        Set<HostAndPort> nodes=new HashSet<>();
        nodes.add(hostAndPort);
        nodes.add(hostAndPort1);
        nodes.add(hostAndPort2);
        nodes.add(hostAndPort3);
        nodes.add(hostAndPort4);
        nodes.add(hostAndPort5);
        nodes.add(hostAndPort6);
        nodes.add(hostAndPort7);
        JedisPoolConfig poolConfig=new JedisPoolConfig();
        poolConfig.setMinIdle(5);
        poolConfig.setMaxTotal(30);
        poolConfig.setMaxWaitMillis(3000);
        poolConfig.setMaxIdle(10);
        jedisCluster=new JedisCluster(nodes,poolConfig);
        String set = jedisCluster.set("cluster1", "clustervalue");
        System.out.println("jedisCluster  set key  result==="+set);
    }

    @After
    public void close() throws IOException {
        if(jedis!=null){
            jedis.close();
        }
        if (jedisSentinelPool!=null){
            jedisSentinelPool.close();
        }
        if (jedisCluster!=null){
            jedisCluster.close();
        }
    }
}