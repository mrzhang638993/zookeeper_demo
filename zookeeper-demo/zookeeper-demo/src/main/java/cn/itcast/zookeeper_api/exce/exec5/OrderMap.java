package cn.itcast.zookeeper_api.exce.exec5;

import org.apache.hadoop.mapreduce.Mapper;

import java.io.IOException;

/**
 * KEYIN,LongWritable
 * VALUEIN,Text
 * KEYOUT,Text
 * VALUEOUT DoubleWritable
 */
public class OrderMap extends Mapper<LongWritable, Text, Text, DoubleWritable> {
    /**
     * 执行map的业务逻辑和实现操作,订单号加上总金额。
     */
    @Override
    protected void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {
        String[] split = value.toString().split(" ");
        // 总的金额
        DoubleWritable val = new DoubleWritable();
        val.set(Double.parseDouble(split[2]));
        context.write(new Text(split[0]), val);
    }
}
