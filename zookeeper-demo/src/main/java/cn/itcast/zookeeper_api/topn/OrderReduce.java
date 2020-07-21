package cn.itcast.zookeeper_api.topn;

import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;

import java.io.IOException;

/**
 * KEYIN,  k2 OrderBean
 * VALUEIN,  v2   NullWritable
 * KEYOUT,  k3   OrderBean
 * VALUEOUT  v3  NullWritable
 */
public class OrderReduce extends Reducer<OrderBean, Text, Text, NullWritable> {

    @Override
    protected void reduce(OrderBean key, Iterable<Text> values, Context context) throws IOException, InterruptedException {
        //  获取 topN的数据执行操作逻辑实现
        int i = 0;  //  求解top1的操作逻辑和实现
        for (Text value : values) {
            context.write(value, NullWritable.get());
            i++;
            if (i >= 2) {
                break;
            }
        }
    }
}
