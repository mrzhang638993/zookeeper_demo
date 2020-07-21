package cn.itcast.zookeeper_api.exce.exec6;

import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.mapreduce.Reducer;

import java.io.IOException;

/**
 * KEYIN,  SalaryBean
 * VALUEIN, NullWritable
 * KEYOUT,  SalaryBean
 * VALUEOUT, NullWritable
 */
public class SalaryReduce extends Reducer<SalaryBean, NullWritable, SalaryBean, NullWritable> {

    @Override
    protected void reduce(SalaryBean key, Iterable<NullWritable> values, Context context) throws IOException, InterruptedException {
        context.write(key, NullWritable.get());
    }
}
