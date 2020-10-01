package cn.itcast.zookeeper_api.stage1.mr_stage1;

import com.alibaba.fastjson.JSON;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;

import java.io.IOException;

/**
 * 这是mapper阶段的业务逻辑处理
 */
public class AccessMapper extends Mapper<LongWritable, Text, AccessLogBean, Text> {

    @Override
    protected void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {
        System.out.println("=======" + JSON.toJSONString(value.toString()) + "=========");
        String[] split = value.toString().split(",");
        //  截取字符串的第一个ip信息作为可以进行分许操作的
        AccessLogBean accessLogBean = new AccessLogBean();
        accessLogBean.setIp(split[0]);
        accessLogBean.setTime(split[1]);
        accessLogBean.setUrl(split[2]);
        accessLogBean.setCount(1);
        context.write(accessLogBean, value);
    }
}
