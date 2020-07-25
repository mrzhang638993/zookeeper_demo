package cn.itcast.zookeeper_api.exce.exce2;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.conf.Configured;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.input.TextInputFormat;
import org.apache.hadoop.mapreduce.lib.output.TextOutputFormat;
import org.apache.hadoop.util.Tool;
import org.apache.hadoop.util.ToolRunner;


/**
 * 数据去重的原理，最终经过map，reduce之后对应的key是保证唯一的，对于value是不要求的。
 */
public class Exce2MainJob extends Configured implements Tool {
    /**
     * 执行具体的业务逻辑实现和代码处理
     */
    public static void main(String[] args) throws Exception {
        int result = ToolRunner.run(new Configuration(), new Exce2MainJob(), args);
        System.exit(result);
    }

    //  对应的实现相关的业务逻辑实现
    @Override
    public int run(String[] args) throws Exception {
        Job exce1Job = Job.getInstance(super.getConf(), "exce2Job");
        exce1Job.setJarByClass(Exce2MainJob.class);
        //  设置输入
        exce1Job.setInputFormatClass(TextInputFormat.class);
        TextInputFormat.addInputPath(exce1Job, new Path("hdfs://node01:8020/exce2"));
        //TextInputFormat.addInputPath(exce1Job,new Path("file:///E:\\BaiduNetdiskDownload\\zookeeper\\day02\\day02\\代码\\day02_zookeeper_api_demo\\src\\main\\java\\cn\\itcast\\zookeeper_api\\exce\\input\\"));
        //  设置map阶段的参数
        exce1Job.setMapperClass(Exce2Mapper.class);
        exce1Job.setMapOutputKeyClass(Text.class);
        exce1Job.setOutputValueClass(NullWritable.class);
        // 设置reduce阶段的参数处理
        exce1Job.setReducerClass(Exce2Reduce.class);
        exce1Job.setOutputKeyClass(Text.class);
        exce1Job.setOutputValueClass(NullWritable.class);
        exce1Job.setOutputFormatClass(TextOutputFormat.class);
        TextOutputFormat.setOutputPath(exce1Job, new Path("hdfs://node01:8020/exce2_output"));
        //TextOutputFormat.setOutputPath(exce1Job,new Path("file:///E:\\BaiduNetdiskDownload\\zookeeper\\day02\\day02\\代码\\day02_zookeeper_api_demo\\src\\main\\java\\cn\\itcast\\zookeeper_api\\exce\\output\\"));
        boolean res = exce1Job.waitForCompletion(true);
        return res ? 0 : 1;
    }
}
