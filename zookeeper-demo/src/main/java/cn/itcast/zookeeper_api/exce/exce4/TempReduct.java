package cn.itcast.zookeeper_api.exce.exce4;

import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;

import java.io.IOException;

public class TempReduct extends Reducer<Text, IntWritable, Text, IntWritable> {

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
