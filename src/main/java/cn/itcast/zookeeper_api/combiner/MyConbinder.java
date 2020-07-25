package cn.itcast.zookeeper_api.combiner;

import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;

import java.io.IOException;
import java.util.Iterator;

/**
 * KEYIN, k1 对应的是map阶段的输入
 * VALUEIN,v1 对应的是map阶段的输出
 * KEYOUT,  k2  对接的是reduce阶段的输入
 * VALUEOUT  v2   对接的是reduce阶段的输入
 */
public class MyConbinder extends Reducer<Text, LongWritable, Text, LongWritable> {

    /**
     * 实现规约逻辑处理，提前实现reduce阶段逻辑处理操作
     */
    @Override
    protected void reduce(Text key, Iterable<LongWritable> values, Context context) throws IOException, InterruptedException {
        Iterator<LongWritable> it = values.iterator();
        LongWritable write = new LongWritable();
        int count = 0;
        while (it.hasNext()) {
            LongWritable next = it.next();
            count += next.get();
        }
        write.set(count);
        context.write(key, write);
    }
}
