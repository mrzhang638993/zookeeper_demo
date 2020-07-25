package cn.itcast.zookeeper_api.partition;

import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;

import java.io.IOException;

/**
 * 处理(k2,v2) 转化为(k3,v3)
 * KEYIN, k2 对应的是Text
 * VALUEIN,  v2  对应的是NullWriteAble
 * KEYOUT,  k3 对应的是对应的是Text
 * VALUEOUT  v3 对应的是NullWriteAble
 */
public class MyReducer extends Reducer<Text, NullWritable, Text, NullWritable> {

    /**
     * 重写reducer的方法
     */
    @Override
    protected void reduce(Text key, Iterable<NullWritable> values, Context context) throws IOException, InterruptedException {
        //  计数器的方式二：使用枚举的方式实现计数器的增加和处理
        org.apache.hadoop.mapreduce.Counter counter = context.getCounter(Counter.MY_INPUT_RECORDS);
        counter.increment(1L);
        context.write(key, NullWritable.get());
    }

    public static enum Counter {
        MY_INPUT_RECORDS, MY_INPUT_BYTES
    }
}
