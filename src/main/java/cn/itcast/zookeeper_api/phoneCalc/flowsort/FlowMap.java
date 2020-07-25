package cn.itcast.zookeeper_api.phoneCalc.flowsort;

import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;

import java.io.IOException;

public class FlowMap extends Mapper<LongWritable, Text, FlowBean, NullWritable> {

    @Override
    protected void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {
        String[] split = value.toString().split("\t");
        FlowBean flowBean = new FlowBean();
        flowBean.setPhoneNum(split[1]);
        flowBean.setUpFlow(Integer.valueOf(split[6]));
        flowBean.setDownFlow(Integer.valueOf(split[7]));
        flowBean.setUpCountFlow(Integer.valueOf(split[8]));
        flowBean.setDownCountFlow(Integer.valueOf(split[9]));
        context.write(flowBean, NullWritable.get());
    }
}
