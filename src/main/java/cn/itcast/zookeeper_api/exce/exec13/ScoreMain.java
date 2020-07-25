package cn.itcast.zookeeper_api.exce.exec13;

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

public class ScoreMain extends Configured implements Tool {
    public static void main(String[] args) throws Exception {
        int run = ToolRunner.run(new Configuration(), new ScoreMain(), args);
        System.exit(run);
    }

    @Override
    public int run(String[] args) throws Exception {
        Job orderJob = Job.getInstance(super.getConf(), "orderJob");
        orderJob.setJarByClass(ScoreMain.class);

        //  对应的执行相关的业务逻辑实现
        orderJob.setInputFormatClass(TextInputFormat.class);
        TextInputFormat.addInputPath(orderJob, new Path("file:///E:\\BaiduNetdiskDownload\\zookeeper\\day02\\day02\\code\\day02_zookeeper_api_demo\\src\\main\\java\\cn\\itcast\\zookeeper_api\\exce\\exec13\\input\\score.txt"));
        //  设置mapper极段的参数
        orderJob.setMapperClass(ScoreMap.class);
        orderJob.setMapOutputKeyClass(ScoreBean.class);
        orderJob.setMapOutputValueClass(Text.class);
        //  设置shuffle阶段的3-7操作的
        // 指定分区
        orderJob.setPartitionerClass(ScorePartition.class);
        //  对应的输出文件信息,对应的这个5怎么计算出来的，这个需要后续的逻辑和校验的。
        orderJob.setNumReduceTasks(5);
        // 设置分区个数的话,对应的输出结果会指定n个文件存放，不指定的话，对应的是一个分区的，只会形成一个文件的。
        //  指定分组
        orderJob.setGroupingComparatorClass(ScoreGroup.class);
        //  设置reducer
        orderJob.setReducerClass(ScoreReduce.class);
        orderJob.setOutputKeyClass(ScoreBean.class);
        orderJob.setOutputValueClass(NullWritable.class);
        //设置输出的参数和路径
        orderJob.setOutputFormatClass(TextOutputFormat.class);
        TextOutputFormat.setOutputPath(orderJob, new Path("file:///E:\\BaiduNetdiskDownload\\zookeeper\\day02\\day02\\code\\day02_zookeeper_api_demo\\src\\main\\java\\cn\\itcast\\zookeeper_api\\exce\\exec13\\output"));
        boolean res = orderJob.waitForCompletion(true);
        return res ? 0 : 1;
    }
}
