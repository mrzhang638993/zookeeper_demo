package cn.itcast.zookeeper_api.stage1.mr_stage3;

import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Partitioner;

/**
 * 根据ip进行分区操作
 */
public class AccessPartition extends Partitioner<Text, Text> {
    @Override
    public int getPartition(Text key, Text text2, int i) {
        String ip = key.toString();
        return (ip.hashCode() & 2147483647) % i;
    }
}
