package cn.itcast.zookeeper_api.kafka;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;

import java.util.Properties;

/**
 * 生产数据到kafka的topic中生产数据
 */
public class MyProducer {

    public static void main(String[] args) {
        Properties props = new Properties();
        props.put("bootstrap.servers", "node01:9092,node02:9092,node03:9092");
        //  消息的确认机制，可以保证消息的不丢失的，消息丢失的话，会报错的。这个参数很关键的。
        props.put("acks", "all");
        //  确定消息发送失败之后的尝试尝试，可以设置为3的。
        props.put("retries", 0);
        props.put("batch.size", 16384);
        props.put("linger.ms", 1);
        // 异步确认的时候，可以设置buffer的大小的。
        props.put("buffer.memory", 33554432);
        props.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        props.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        KafkaProducer kafkaProducer = new KafkaProducer<>(props);
        for (int i = 0; i < 100; i++) {
            ProducerRecord<String, String> producerRecord = new ProducerRecord("mypartitioner", "messageabcde" + i);
            kafkaProducer.send(producerRecord);
        }
        kafkaProducer.close();
    }
}
