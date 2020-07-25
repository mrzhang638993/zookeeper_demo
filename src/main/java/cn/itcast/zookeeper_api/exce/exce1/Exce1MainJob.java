package cn.itcast.zookeeper_api.exce.exce1;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.conf.Configured;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.input.TextInputFormat;
import org.apache.hadoop.mapreduce.lib.output.TextOutputFormat;
import org.apache.hadoop.util.Tool;
import org.apache.hadoop.util.ToolRunner;

public class Exce1MainJob extends Configured implements Tool {
    public static void main(String[] args) throws Exception {
        int run = ToolRunner.run(new Configuration(), new Exce1MainJob(), args);
        System.exit(run);
    }

    @Override
    public int run(String[] args) throws Exception {
        Job exce1Job = Job.getInstance(super.getConf(), "exce1Job");
        exce1Job.setJarByClass(Exce1MainJob.class);
        //  设置输入
        exce1Job.setInputFormatClass(TextInputFormat.class);
        TextInputFormat.addInputPath(exce1Job, new Path("hdfs://node01:8020/exce1"));
        //  设置map阶段的参数
        exce1Job.setMapperClass(Exce1Mapper.class);
        exce1Job.setMapOutputKeyClass(Text.class);
        exce1Job.setMapOutputValueClass(LongWritable.class);
        //  设置shuffle阶段的参数
        exce1Job.setPartitionerClass(Exce1Partition.class);
        //  动态的随机的reduceTasks的配置，这个地方需要关注，动态的需要怎么进行处理的？
        exce1Job.setNumReduceTasks(3);
        // 设置reduce阶段的参数处理
        exce1Job.setReducerClass(Exce1Reduce.class);
        exce1Job.setOutputKeyClass(Text.class);
        exce1Job.setOutputValueClass(LongWritable.class);
        exce1Job.setOutputFormatClass(TextOutputFormat.class);
        TextOutputFormat.setOutputPath(exce1Job, new Path("hdfs://node01:8020/exce1_output"));
        boolean res = exce1Job.waitForCompletion(true);
        return res ? 0 : 1;
    }
}
