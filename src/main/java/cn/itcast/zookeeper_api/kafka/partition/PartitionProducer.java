package cn.itcast.zookeeper_api.kafka.partition;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;

import java.util.Properties;

/**
 * 分区规则测试使用的producer代码
 * 1.如果指定了分区号的话，数据落入到指定的分区中的；
 * 2.没有指定分区号码，出现了数据的key的话，通过key的hshcode决定数据落入到那个partition中的。
 * 3.没有分区号，也没有指定数据的key，根据轮询的机制来确定数据对应的partition的。
 * */
public class PartitionProducer {

    /**
     * 通过不同的方式，将数据写入到不同的分区中。
     * */
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
        //  指定自定义分区的类
        props.put("partitioner.class","cn.itcast.zookeeper_api.kafka.partition.MyPartitioner");
        KafkaProducer kafkaProducer = new KafkaProducer<>(props);
        for (int i=0;i<100;i++){
            // 第一种分区策略：没有指定分区号，也没有指定key的操作，采用的是轮询的方式的String topic, V value
            //ProducerRecord<String, String> producerRecord = new ProducerRecord<>("mypartitioner", "mymessage" + i);
            //  第二种分区策略： String topic, K key, V value  根据key的hash值来进行分区操作的. key.hashcode()%numPartitions。
            //  如果数据的key没有变化的，那么key.hashcode()%numPartitions=固定值，数据会向固定分区发送数据的。使用这个操作的话，需要保证key是变化的。
            //ProducerRecord<String, String> producerRecord = new ProducerRecord<>("mypartitioner", "mykey", "mymessage" + i);
            // 第三种分区策略：指定分区号的，数据会写入到指定的分区中的。下面的操作，数据会写入到指定的0号分区的。
            // String topic, Integer partition, K key, V value
            //ProducerRecord<String, String> producerRecord = new ProducerRecord<String, String>("mypartitioner",0,"mykey", "mymessage" + i);
            // 第4种分区策略：自定义分区策略，不自定义分区规则的话，数据使用默认的DefaultPartitioner的轮询策略的.数据会全部到2号分区的
            ProducerRecord<String, String> producerRecord = new ProducerRecord<String, String>("mypartitioner","mymessage" + i);
            kafkaProducer.send(producerRecord);
        }
        kafkaProducer.close();
    }
}
