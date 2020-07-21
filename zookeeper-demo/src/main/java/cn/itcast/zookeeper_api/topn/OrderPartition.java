package cn.itcast.zookeeper_api.topn;

import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Partitioner;

/**
 * 自定义分区操作
 * orderBean  k2
 * nullWritable  v2
 * i      对应的分区的个数
 */
public class OrderPartition extends Partitioner<OrderBean, Text> {

    /**
     * 返回分区的编号。默认的分区是hash分区操作的
     */
    @Override
    public int getPartition(OrderBean orderBean, Text text, int i) {
        // 根据订单id实现分区操作
        String orderId = orderBean.getOrderId();
        return (orderId.hashCode() & 2147483647) % i;
    }
}
