package cn.itcast.zookeeper_api.exce.exec6;

import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;

import java.io.IOException;

/**
 * KEYIN,  LongWritable
 * VALUEIN, Text
 * KEYOUT,  SalaryBean
 * VALUEOUT, NullWritable
 */
public class SalaryMap extends Mapper<LongWritable, Text, SalaryBean, NullWritable> {

    /**
     * map 阶段操作(k1,v1) 转化为(k2,v2)
     */
    @Override
    protected void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {
        String[] split = value.toString().split(" ");
        SalaryBean salaryBean = new SalaryBean();
        salaryBean.setName(split[0]);
        salaryBean.setAge(Integer.valueOf(split[1]));
        salaryBean.setSalary(Integer.valueOf(split[2]));
        context.write(salaryBean, NullWritable.get());
    }
}
