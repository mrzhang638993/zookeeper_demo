package cn.itcast.zookeeper_api.phoneCalc.flow;

import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;

import java.io.IOException;

/**
 * 实现reduce阶段的业务逻辑处理
 */
public class FlowReduce extends Reducer<Text, FlowBean, Text, FlowBean> {

    /**
     * 实现(k2,v2)转化为(k3,v3)
     */
    @Override
    protected void reduce(Text key, Iterable<FlowBean> values, Context context) throws IOException, InterruptedException {
        int upFlow = 0;
        int downFlow = 0;
        int upCountFlow = 0;
        int downCountFlow = 0;
        for (FlowBean flowBean : values) {
            upFlow += flowBean.getUpFlow();
            downFlow += flowBean.getDownFlow();
            upCountFlow += flowBean.getUpCountFlow();
            downCountFlow += flowBean.getDownCountFlow();
        }
        FlowBean flowBean = new FlowBean();
        flowBean.setUpFlow(upFlow);
        flowBean.setDownFlow(downFlow);
        flowBean.setUpCountFlow(upCountFlow);
        flowBean.setDownCountFlow(downCountFlow);
        context.write(key, flowBean);
    }
}
