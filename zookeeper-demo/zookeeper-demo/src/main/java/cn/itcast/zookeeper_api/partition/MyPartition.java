package cn.itcast.zookeeper_api.partition;

import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Partitioner;


/**
 * 指定分区规则
 */
public class MyPartition extends Partitioner<Text, NullWritable> {

    /**
     * 指定分区规则,判断中奖字段的数值和15之间的关系。大于15指定分区为1，否则的话指定为0分区
     * 返回对应的分区编号信息
     */
    @Override
    public int getPartition(Text text, NullWritable nullWritable, int i) {
        String content = text.toString().split("\t")[5];
        int digit = Integer.valueOf(content);
        if (digit >= 15) {
            return 1;
        } else {
            return 0;
        }
    }
}
