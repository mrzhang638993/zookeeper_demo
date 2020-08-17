package cn.itcast.zookeeper_api.kafka.consumer;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.clients.consumer.OffsetAndMetadata;
import org.apache.kafka.common.TopicPartition;

import java.util.*;

/**
 * 处理完成每个分区的数据执行一次提交操作
 */
public class ConsumerPartition {


    /**
     * 消费每一个分区里面的数据，然后一个个的分区进行提交的。
     * 这样的话安全性更加的高的，推荐使用这种方式提交的。
     * 减少数据丢失的可能性的。
     */
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
        //  指定消费topic的中的数据
        consumer.subscribe(Arrays.asList("mypartitioner"));
        while (true) {
            //  主动拉取数据,1000毫秒的超时时间
            ConsumerRecords<String, String> records = consumer.poll(1000);
            //  获取mypartitioner的所有的分区
            Set<TopicPartition> partitions = records.partitions();
            // 循环遍历每一个分区里面的数据
            for (TopicPartition partition : partitions) {
                //  获取分区里面的所有的数据
                List<ConsumerRecord<String, String>> records1 = records.records(partition);
                for (ConsumerRecord<String, String> record : records1) {
                    System.out.println(record.value() + "=====" + record.offset());
                }
                // 分区里面的数据处理完成了
                // 获取分区最后一条记录的offset的。
                long offset = records1.get(records1.size() - 1).offset();
                // 提交offset,并且给offset加上1。表示下一次从没有消费的那一条数据开始消费的。
                consumer.commitSync(Collections.singletonMap(partition, new OffsetAndMetadata(offset + 1)));
            }
        }
    }
}
