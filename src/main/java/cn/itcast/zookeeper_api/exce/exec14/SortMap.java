package cn.itcast.zookeeper_api.exce.exec14;

import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;

import java.io.IOException;

/**
 * KEYIN,LongWritable
 * VALUEIN,  Text
 * KEYOUT,   SortBean
 * VALUEOUT   Text
 */
public class SortMap extends Mapper<LongWritable, Text, SortBean, Text> {

    @Override
    protected void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {
        String[] split = value.toString().split(" ");
        SortBean sortBean = new SortBean();
        sortBean.setSort(split[0]);
        sortBean.setSortValue(Integer.valueOf(split[1]));
        context.write(sortBean, value);
    }
}
