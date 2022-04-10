import com.alibaba.fastjson.JSONObject;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.cluster.routing.allocation.decider.EnableAllocationDecider;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.kafka.core.KafkaTemplate;

import java.io.IOException;

public class TestMain {
    public static void main(String[] args) throws IOException {
        String content="123";
        Object parse = JSONObject.parse(content);
        KafkaTemplate kafkaTemplate=new KafkaTemplate(null);
        kafkaTemplate.send(null,null);
        RestHighLevelClient restHighLevelClient=new RestHighLevelClient(null);
        restHighLevelClient.bulk(null,null,null);
        //解决数据重平衡和倾斜的问题？
        //redis的事务操作实现
        RedisTemplate redisTemplate=new RedisTemplate();
        redisTemplate.opsForList().set("hello",0,String.valueOf("many world"));
        redisTemplate.opsForValue().set("world","good");
        redisTemplate.opsForSet().add("many","world");
        redisTemplate.multi();
        redisTemplate.delete("hello");
        redisTemplate.exec();
    }
}
