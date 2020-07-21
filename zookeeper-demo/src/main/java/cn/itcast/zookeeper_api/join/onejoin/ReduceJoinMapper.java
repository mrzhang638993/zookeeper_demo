package cn.itcast.zookeeper_api.join.onejoin;

import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.lib.input.FileSplit;

import java.io.IOException;

/**
 * KEYIN,  k1  LongWritable
 * VALUEIN,  v1  Text
 * KEYOUT,  k2   Text
 * VALUEOUT v2   Text
 */
public class ReduceJoinMapper extends Mapper<LongWritable, Text, Text, Text> {

    /**
     * 对应的实现map转换逻辑,
     * map阶段怎么处理不同类型的map的切割和转换操作逻辑
     */
    @Override
    protected void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {
        //  判断数据来自哪个文件，
        FileSplit inputSplit = (FileSplit) context.getInputSplit();
        // 获取文件的路径
        Path path = inputSplit.getPath();
        // 获取文件的名称
        String name = path.getName();
        // 对应的实现不同文件的key以及value的转换操作逻辑
        if ("orders.txt".equals(name)) {
            // 来自订单的数据
            String[] split = value.toString().split(",");
            Text keys = new Text(split[2]);
            context.write(keys, value);
        } else {
            // 数据来自于product.txt 可以获取product的数据信息
            String[] split = value.toString().split(",");
            Text keys = new Text(split[0]);
            context.write(keys, value);
        }
    }
}
