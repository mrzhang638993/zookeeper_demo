package cn.itcast.zookeeper_api.stage1.mr_stage2;

import cn.itcast.zookeeper_api.stage1.mr_stage1.AccessLogBean;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Partitioner;

/**
 * mapper阶段之后，可以将mapper阶段根据指定的规则及行分区操作，
 * 这样的话，指定的key对应的会进入到不同的分区中的
 */
public class AccessPartition extends Partitioner<Text, AccessLogBean> {
    @Override
    public int getPartition(Text text, AccessLogBean accessLogBean, int i) {
        // ip进行分区操作的，相同ip的数据获分配到相同的分区中进行计算的。
        String ip = text.toString();
        return (ip.hashCode() & 2147483647) % i;
    }
}
