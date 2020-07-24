package cn.itcast.zookeeper_api.exce.exce4;

import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;

import java.io.IOException;

/**
 * 实现年份的过滤操作
 * KEYIN, k1 对应的是
 * VALUEIN, KEYOUT, VALUEOUT
 */
public class TempCombinder extends Reducer<Text, IntWritable, Text, IntWritable> {

    /**
     * 实现资源的整合操作实现,在规约阶段统计出来每一个年份的最大的温度数值
     */
    @Override
    protected void reduce(Text key, Iterable<IntWritable> values, Context context) throws IOException, InterruptedException {
        Integer max = Integer.MIN_VALUE;
        for (IntWritable val : values
        ) {
            if (val.get() > max) {
                max = val.get();
            }
        }
        context.write(key, new IntWritable(max));
    }
}
