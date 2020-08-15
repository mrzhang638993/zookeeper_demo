package cn.itcast.zookeeper_api.kafka;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;

import java.util.Properties;

/**
 * 生产数据到kafka的topic中生产数据
 * */
public class MyProducer {

    public static void main(String[] args) {
        Properties props=new Properties();
        props.put("bootstrap.servers", "node01:9092,node02:9092,node03:9092");
        props.put("acks", "all");
        props.put("retries", 0);
        props.put("batch.size", 16384);
        props.put("linger.ms", 1);
        props.put("buffer.memory", 33554432);
        props.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        props.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        KafkaProducer kafkaProducer = new KafkaProducer<>(props);
        for (int i=0;i<100;i++){
            ProducerRecord<String ,String > producerRecord=new ProducerRecord("test","message"+i);
            kafkaProducer.send(producerRecord);
        }
        kafkaProducer.close();
    }
}
