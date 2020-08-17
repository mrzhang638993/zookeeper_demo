package cn.itcast.zookeeper_api.stage1.mr_stage3;


import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.lib.input.FileSplit;

import java.io.IOException;


public class AccessMapper extends Mapper<LongWritable, Text, Text, Text> {

    /**
     * 实现join操作
     */
    @Override
    protected void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {
        FileSplit inputSplit = (FileSplit) context.getInputSplit();
        // 获取文件的路径
        Path path = inputSplit.getPath();
        // 获取文件的名称
        String name = path.getName();
        if (name.equals("visit.log")) {
            //根据访问日志实现切割操作的
            String[] split = value.toString().split(",");
            context.write(new Text(split[0]), value);
        } else {
            //  对应的是登录日志login.log，登录日志需要找出完整的登录日志的。
            String[] split = value.toString().split(",");
            context.write(new Text(split[1]), value);
        }
    }
}
