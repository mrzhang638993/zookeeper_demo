package cn.itcast.zookeeper_api.seriableCompare;

import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;

import java.io.IOException;

/**
 * KEYIN, LongWritable
 * VALUEIN,  Text
 * KEYOUT,  Text
 * VALUEOUT  NullWritable
 */
public class SortMapper extends Mapper<LongWritable, Text, SortBean, NullWritable> {

    /**
     * 重写map相关的数据方法
     * 需要将0,(a 3)  转化为 SortBean对应的操作
     */
    @Override
    protected void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {
        String[] values = value.toString().split(" ");
        SortBean sortBean = new SortBean();
        System.out.println(values);
        sortBean.setWord(values[0]);
        sortBean.setNum(Integer.parseInt(values[1]));
        context.write(sortBean, NullWritable.get());
    }
}
