package cn.itcast.zookeeper_api.kafka;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;

/**
 * 手动提交消息
 * */
public class ManualCommit {


    public static void main(String[] args) {
        Properties props=new Properties();
        props.put("bootstrap.servers", "node01:9092,node02:9092:node03:9092");
        props.put("group.id", "test");
        // 手动提交
        props.put("enable.auto.commit", "false");
        props.put("auto.commit.interval.ms", "1000");
        props.put("session.timeout.ms", "30000");
        //指定key和value的反序列化操作。
        props.put("key.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
        props.put("value.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
        KafkaConsumer<String,String> consumer=new KafkaConsumer<String, String>(props);
        //  指定消费topic的中的数据
        consumer.subscribe(Arrays.asList("test"));
        final int minBatchSize = 200;
        List<ConsumerRecord<String, String>> buffer = new ArrayList<>();
        while (true) {
            //  主动拉取数据
            ConsumerRecords<String, String> records = consumer.poll(100);
            for (ConsumerRecord<String, String> record : records) {
                buffer.add(record);
            }
            if (buffer.size() >= minBatchSize) {
                //insertIntoDb(buffer);
                System.out.println("手动提交代码");
                //  异步提交代码的效率会更加的高的，不会影响代码的运行。
                consumer.commitSync();
                buffer.clear();
            }
        }
    }
}
