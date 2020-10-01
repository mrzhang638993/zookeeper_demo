package cn.itcast.zookeeper_api.phoneCalc.flow;

import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;

import java.io.IOException;

/**
 * 对应的实现map阶段的逻辑操作实现
 */
public class FlowMap extends Mapper<LongWritable, Text, Text, FlowBean> {

    /**
     * 对应的实现(k1,v1)转化为(k2,v2)的业务逻辑
     */
    @Override
    protected void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {
        String[] values = value.toString().split("\t");
        Text keys = new Text();
        keys.set(values[1]);
        FlowBean flowBean = new FlowBean();
        flowBean.setUpFlow(Integer.valueOf(values[6]));
        flowBean.setDownFlow(Integer.valueOf(values[7]));
        flowBean.setUpCountFlow(Integer.valueOf(values[8]));
        flowBean.setDownCountFlow(Integer.valueOf(values[9]));
        context.write(keys, flowBean);
    }
}
