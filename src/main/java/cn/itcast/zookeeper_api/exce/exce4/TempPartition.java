package cn.itcast.zookeeper_api.exce.exce4;

import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Partitioner;


/**
 * 根据年份进行分区操作实现
 */
public class TempPartition extends Partitioner<Text, IntWritable> {

    @Override
    public int getPartition(Text text, IntWritable intWritable, int i) {
        return Integer.valueOf(text.toString()) % i;
    }
}
