package cn.itcast.zookeeper_api.kafka.stream;

import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.KafkaStreams;
import org.apache.kafka.streams.StreamsConfig;
import org.apache.kafka.streams.kstream.KStreamBuilder;

import java.util.Properties;

/**
 * 实现stream的流式编程
 */
public class Stream {

    public static void main(String[] args) {
        //  获取到kafka的stream的builder
        KStreamBuilder kStreamBuilder = new KStreamBuilder();
        //从test对应的topic中获取数据
        // 调用mapvalues表示将每一行的value获取出来的。ValueMapper。line对应的是取出来的一行行的数据的
        kStreamBuilder.stream("test").mapValues(line -> line.toString().toUpperCase()).to("test2");
        // 通过 kStreamBuilder 对应的转化为KafkaStream，通过KafkaStream来实现流式的处理逻辑。
        Properties properties = new Properties();
        properties.put(StreamsConfig.APPLICATION_ID_CONFIG, "bigger");
        properties.put(StreamsConfig.BOOTSTRAP_SERVERS_CONFIG, "node01:9092,node02:9092,node03:9092");
        // 设置key，以及value的序列化和反序列化操作
        properties.put(StreamsConfig.KEY_SERDE_CLASS_CONFIG, Serdes.String().getClass());
        properties.put(StreamsConfig.VALUE_SERDE_CLASS_CONFIG, Serdes.String().getClass());
        //  创建kafka的流式API
        KafkaStreams kafkaStreams = new KafkaStreams(kStreamBuilder, properties);
        // 启动流式api
        kafkaStreams.start();
    }
}
