package cn.itcast.zookeeper_api.kafka.consumer;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.TopicPartition;

import java.util.*;

/**
 * 消费topic的指定分区的数据
 * */
public class ConsumerMannualPartition {

    public static void main(String[] args) {
        Properties props = new Properties();
        props.put("bootstrap.servers", "node01:9092,node02:9092:node03:9092");
        props.put("group.id", "test");
        // 不自定提交
        props.put("enable.auto.commit", "false");
        props.put("auto.commit.interval.ms", "1000");
        props.put("session.timeout.ms", "30000");
        //指定key和value的反序列化操作。
        props.put("key.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
        props.put("value.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
        KafkaConsumer<String, String> consumer = new KafkaConsumer<String, String>(props);
        //  指定消费topic中指定分区的数据的
        //Collection<TopicPartition> partitions
        // String topic, int partition
        // 订阅topic中的指定分区的数据
        consumer.assign(Arrays.asList(new TopicPartition("mypartitioner",0),new TopicPartition("mypartitioner",1)));
        while (true) {
            //  主动拉取数据,1000毫秒的超时时间
            ConsumerRecords<String, String> records = consumer.poll(1000);
            for (ConsumerRecord<String, String> record : records) {
                System.out.println("数据值为===="+record.value()+"=====offset====="+record.offset());
            }
            // 手动提交确认offset
            consumer.commitSync();
        }
    }
}
