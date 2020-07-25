package cn.itcast.zookeeper_api.exce.exce1;

import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;

import java.io.IOException;

/**
 * KEYIN,  k2 对应的是Text
 * VALUEIN, v2 对应的是 LongWritable
 * KEYOUT, k3 对应的是 Text
 * VALUEOUT  v3 对应的是 LongWritable
 */
public class Exce1Reduce extends Reducer<Text, LongWritable, Text, LongWritable> {

    /**
     * 执行reduce阶段的逻辑处理
     */
    @Override
    protected void reduce(Text key, Iterable<LongWritable> values, Context context) throws IOException, InterruptedException {
        long count = 0;
        for (LongWritable writable :
                values) {
            count += writable.get();
        }
        LongWritable writable = new LongWritable();
        writable.set(count);
        context.write(key, writable);
    }
}
