package cn.itcast.zookeeper_api.phoneCalc.phonepartition;

import cn.itcast.zookeeper_api.seriableCompare.SortJobMain;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.conf.Configured;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.input.TextInputFormat;
import org.apache.hadoop.mapreduce.lib.output.TextOutputFormat;
import org.apache.hadoop.util.Tool;
import org.apache.hadoop.util.ToolRunner;

public class FlowJobMain extends Configured implements Tool {
    public static void main(String[] args) throws Exception {
        int run = ToolRunner.run(new Configuration(), new FlowJobMain(), args);
        System.exit(run);
    }

    @Override
    public int run(String[] args) throws Exception {
        Job flowSortJobMain = Job.getInstance(super.getConf(), "flowSortJobMain");
        flowSortJobMain.setJarByClass(SortJobMain.class);
        //  设置输入
        flowSortJobMain.setInputFormatClass(TextInputFormat.class);
        //  指定目录的话，会将目录下面所有的文件全部加载进行计算操作的。
        //TextInputFormat.addInputPath(job, new Path("hdfs://node01:8020/input/sort_input"));
        //  可以使用前面模型的输出结果来作为输入文件进行操作的。
        TextInputFormat.addInputPath(flowSortJobMain, new Path("file:///E:\\BaiduNetdiskDownload\\zookeeper\\day02\\day02\\代码\\day02_zookeeper_api_demo\\src\\main\\java\\cn\\itcast\\zookeeper_api\\phoneCalc\\data_flow.dat"));
        //  设置map阶段的参数
        flowSortJobMain.setMapperClass(FlowMap.class);
        flowSortJobMain.setMapOutputKeyClass(FlowBean.class);
        flowSortJobMain.setOutputValueClass(NullWritable.class);
        //  设置shuffle阶段的排序逻辑参数处理逻辑,主要包括分区，排序，规约，分组，此处不需要进行设置操作
        flowSortJobMain.setPartitionerClass(PhonePartition.class);
        flowSortJobMain.setNumReduceTasks(4);
        // 设置reduce阶段的参数处理
        flowSortJobMain.setReducerClass(FlowReduce.class);
        flowSortJobMain.setOutputKeyClass(FlowBean.class);
        flowSortJobMain.setOutputValueClass(NullWritable.class);
        flowSortJobMain.setOutputFormatClass(TextOutputFormat.class);
        //TextOutputFormat.setOutputPath(flowJobMain, new Path("hdfs://node01:8020/out/sort_out"));
        TextOutputFormat.setOutputPath(flowSortJobMain, new Path("file:///E:\\BaiduNetdiskDownload\\zookeeper\\day02\\day02\\代码\\day02_zookeeper_api_demo\\src\\main\\java\\cn\\itcast\\zookeeper_api\\phoneCalc\\phone\\"));
        boolean res = flowSortJobMain.waitForCompletion(true);
        return res ? 0 : 1;
    }
}
