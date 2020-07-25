package cn.itcast.zookeeper_api.phoneCalc.flowsort;

import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.mapreduce.Reducer;

import java.io.IOException;

/**
 * KEYIN,
 * VALUEIN,
 * KEYOUT,
 * VALUEOUT
 */
public class FlowReduce extends Reducer<FlowBean, NullWritable, FlowBean, NullWritable> {
    /**
     * 执行reduce的业务逻辑
     */
    @Override
    protected void reduce(FlowBean key, Iterable<NullWritable> values, Context context) throws IOException, InterruptedException {
        context.write(key, NullWritable.get());
    }
}
