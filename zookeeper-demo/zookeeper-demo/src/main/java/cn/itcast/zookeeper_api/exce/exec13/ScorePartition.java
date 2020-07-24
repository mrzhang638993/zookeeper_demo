package cn.itcast.zookeeper_api.exce.exec13;

import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Partitioner;

/**
 * KEY, ScoreBean
 * VALUE  NullWritable
 */
public class ScorePartition extends Partitioner<ScoreBean, Text> {

    /**
     * 执行分区操作实现,根据班级进行分区操作实现
     */
    @Override
    public int getPartition(ScoreBean scoreBean, Text text, int i) {
        return (scoreBean.getStuClass().hashCode() & 2147483647) % i;
    }
}
