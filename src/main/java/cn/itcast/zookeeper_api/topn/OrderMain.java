package cn.itcast.zookeeper_api.topn;

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

public class OrderMain extends Configured implements Tool {

    public static void main(String[] args) throws Exception {
        int run = ToolRunner.run(new Configuration(), new OrderMain(), args);
        System.exit(run);
    }

    @Override
    public int run(String[] args) throws Exception {
        Job orderJob = Job.getInstance(super.getConf(), "orderJob");
        orderJob.setJarByClass(OrderMain.class);

        //  对应的执行相关的业务逻辑实现
        orderJob.setInputFormatClass(TextInputFormat.class);
        TextInputFormat.addInputPath(orderJob, new Path("file:///F:\\works\\hadoop1\\zookeeper-demo\\src\\main\\java\\cn\\itcast\\zookeeper_api\\topn\\input\\orders.txt"));
        //  设置mapper极段的参数
        orderJob.setMapperClass(OrderMap.class);
        orderJob.setMapOutputKeyClass(OrderBean.class);
        orderJob.setMapOutputValueClass(Text.class);
        //  设置shuffle阶段的3-7操作的
        // 指定分区
        orderJob.setPartitionerClass(OrderPartition.class);
        // 设置分区个数的话,对应的输出结果会指定n个文件存放，不指定的话，对应的是一个分区的，只会形成一个文件的。
        //  指定分组
        orderJob.setGroupingComparatorClass(OrderGroup.class);
        //  设置reducer
        orderJob.setReducerClass(OrderReduce.class);
        orderJob.setOutputKeyClass(OrderBean.class);
        orderJob.setOutputValueClass(NullWritable.class);
        //设置输出的参数和路径
        orderJob.setOutputFormatClass(TextOutputFormat.class);
        TextOutputFormat.setOutputPath(orderJob, new Path("file:///E:\\BaiduNetdiskDownload\\zookeeper\\day02\\day02\\code\\day02_zookeeper_api_demo\\src\\main\\java\\cn\\itcast\\zookeeper_api\\topn\\output"));
        boolean res = orderJob.waitForCompletion(true);
        return res ? 0 : 1;
    }
}
