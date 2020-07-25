package cn.itcast.zookeeper_api.partition;

import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Counter;
import org.apache.hadoop.mapreduce.Mapper;

import java.io.IOException;

/**
 * KEYIN, k1行偏移量 LongWriteable
 * VALUEIN, v1 行文本数据 Text
 * KEYOUT,   k2   Text  一行的文本数据
 * VALUEOUT  v2  NUllWriteable  占位符
 */
public class PartitionMapper extends Mapper<LongWritable, Text, Text, NullWritable> {

    /**
     * 重写map方法，将(k1,v1)转化为(k2,v2)
     */
    @Override
    protected void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {
        //  方式一：自定义计数器,MR_COUNTER对应的是计数器的类型，partition_counter对应的是计数器的名称
        Counter counter = context.getCounter("MR_COUNTER", "partition_counter");
        counter.increment(1L);
        context.write(value, NullWritable.get());
    }
}
