package cn.itcast.zookeeper_api.exce.exce3;

import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.mapreduce.Reducer;

import java.io.IOException;

public class StudentScoreReduce extends Reducer<StudentScoreBean, NullWritable, StudentScoreBean, NullWritable> {

    @Override
    protected void reduce(StudentScoreBean key, Iterable<NullWritable> values, Context context) throws IOException, InterruptedException {
        context.write(key, NullWritable.get());
    }
}
