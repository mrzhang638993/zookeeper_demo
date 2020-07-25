package cn.itcast.zookeeper_api.exce.exec12;

import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.mapreduce.Mapper;

import java.io.IOException;

/**
 * KEYIN,  k1
 * VALUEIN,  v1
 * KEYOUT,   k2
 * VALUEOUT  v2
 */
public class MyRecordMap extends Mapper<ScoreWritable, NullWritable, ScoreWritable, NullWritable> {

    @Override
    protected void map(ScoreWritable key, NullWritable value, Context context) throws IOException, InterruptedException {
        context.write(key, value);
    }
}
