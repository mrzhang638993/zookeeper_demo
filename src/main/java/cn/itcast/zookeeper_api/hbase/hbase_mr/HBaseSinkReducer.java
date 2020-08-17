package cn.itcast.zookeeper_api.hbase.hbase_mr;

import org.apache.hadoop.hbase.client.Put;
import org.apache.hadoop.hbase.io.ImmutableBytesWritable;
import org.apache.hadoop.hbase.mapreduce.TableReducer;
import org.apache.hadoop.io.Text;

import java.io.IOException;

/**
 * 负责将数据写入到myuser2中
 * <p>
 * KEYIN, k2 对应的是text
 * VALUEIN, v2 对应的是put对象
 * KEYOUT  k3  ImmutableBytesWritable  对应的是字节数组相关的。
 */
public class HBaseSinkReducer extends TableReducer<Text, Put, ImmutableBytesWritable> {

    /**
     * 执行reducer的相关的业务逻辑
     */
    @Override
    protected void reduce(Text key, Iterable<Put> values, Context context) throws IOException, InterruptedException {
        //  一个reduce对应的只有一个values的
        for (Put put : values) {
            context.write(new ImmutableBytesWritable(key.toString().getBytes()), put);
        }
    }
}
