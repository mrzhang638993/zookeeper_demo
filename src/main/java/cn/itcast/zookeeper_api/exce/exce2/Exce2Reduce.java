package cn.itcast.zookeeper_api.exce.exce2;

import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;

import java.io.IOException;

public class Exce2Reduce extends Reducer<Text, NullWritable, Text, NullWritable> {

    /**
     * 执行reduce阶段的业务逻辑实现
     */
    @Override
    protected void reduce(Text key, Iterable<NullWritable> values, Context context) throws IOException, InterruptedException {
        context.write(key, NullWritable.get());
    }
}
