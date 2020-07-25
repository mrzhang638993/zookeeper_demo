package cn.itcast.zookeeper_api.exce.exce1;

import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Partitioner;

/**
 * 根据对应的(k2,v2)实现分区操作
 */
public class Exce1Partition extends Partitioner<Text, LongWritable> {

    /**
     * i对应的是传递过来的分区数的数量的
     */
    @Override
    public int getPartition(Text text, LongWritable longWritable, int i) {
        String content = text.toString();
        //  设置分区类型操作逻辑
        int countMax = Integer.parseInt(content) % i;
        return countMax;
    }
}
