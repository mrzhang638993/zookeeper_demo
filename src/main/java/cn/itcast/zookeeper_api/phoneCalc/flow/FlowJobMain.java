package cn.itcast.zookeeper_api.phoneCalc.flow;

import cn.itcast.zookeeper_api.seriableCompare.SortJobMain;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.conf.Configured;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.Text;
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
        Job flowJobMain = Job.getInstance(super.getConf(), "flowJobMain");
        flowJobMain.setJarByClass(SortJobMain.class);
        //  设置输入
        flowJobMain.setInputFormatClass(TextInputFormat.class);
        //  指定目录的话，会将目录下面所有的文件全部加载进行计算操作的。
        //TextInputFormat.addInputPath(job, new Path("hdfs://node01:8020/input/sort_input"));
        TextInputFormat.addInputPath(flowJobMain, new Path("file:///E:\\BaiduNetdiskDownload\\zookeeper\\day02\\day02\\代码\\day02_zookeeper_api_demo\\src\\main\\java\\cn\\itcast\\zookeeper_api\\phoneCalc\\data_flow.dat"));
        //  设置map阶段的参数
        flowJobMain.setMapperClass(FlowMap.class);
        flowJobMain.setMapOutputKeyClass(Text.class);
        flowJobMain.setOutputValueClass(FlowBean.class);
        //  设置shuffle阶段的排序逻辑参数处理逻辑,主要包括分区，排序，规约，分组，此处不需要进行设置操作
        // 设置reduce阶段的参数处理
        flowJobMain.setReducerClass(FlowReduce.class);
        flowJobMain.setOutputKeyClass(Text.class);
        flowJobMain.setOutputValueClass(FlowBean.class);
        flowJobMain.setOutputFormatClass(TextOutputFormat.class);
        //TextOutputFormat.setOutputPath(flowJobMain, new Path("hdfs://node01:8020/out/sort_out"));
        TextOutputFormat.setOutputPath(flowJobMain, new Path("file:///E:\\BaiduNetdiskDownload\\zookeeper\\day02\\day02\\代码\\day02_zookeeper_api_demo\\src\\main\\java\\cn\\itcast\\zookeeper_api\\phoneCalc\\output\\"));
        boolean res = flowJobMain.waitForCompletion(true);
        return res ? 0 : 1;
    }
}
