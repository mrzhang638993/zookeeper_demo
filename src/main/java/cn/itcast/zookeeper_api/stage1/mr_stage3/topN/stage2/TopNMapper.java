package cn.itcast.zookeeper_api.stage1.mr_stage3.topN.stage2;

import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;

import java.io.IOException;

public class TopNMapper extends Mapper<LongWritable, Text, TopNBean, Text> {

    @Override
    protected void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {
        String[] split = value.toString().split("\001");
        TopNBean topNBean = new TopNBean();
        topNBean.setUrl(split[0]);
        topNBean.setCount(Integer.parseInt(split[1]));
        context.write(topNBean, value);
    }
}
