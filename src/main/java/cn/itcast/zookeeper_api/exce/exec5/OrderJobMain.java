package cn.itcast.zookeeper_api.exce.exec5;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.conf.Configured;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.DoubleWritable;
import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.input.TextInputFormat;
import org.apache.hadoop.mapreduce.lib.output.TextOutputFormat;
import org.apache.hadoop.util.Tool;
import org.apache.hadoop.util.ToolRunner;

public class OrderJobMain extends Configured implements Tool {
    public static void main(String[] args) throws Exception {
        int run = ToolRunner.run(new Configuration(), new OrderJobMain(), args);
        System.exit(run);
    }

    @Override
    public int run(String[] args) throws Exception {
        Job orderbMain = Job.getInstance(super.getConf(), "orderbMain");
        orderbMain.setJarByClass(OrderJobMain.class);
        //  设置输入
        orderbMain.setInputFormatClass(TextInputFormat.class);
        TextInputFormat.addInputPath(orderbMain, new Path("file:///E:\\BaiduNetdiskDownload\\zookeeper\\day02\\day02\\代码\\day02_zookeeper_api_demo\\src\\main\\java\\cn\\itcast\\zookeeper_api\\exce\\input\\order.txt"));
        //  设置map阶段的参数
        orderbMain.setMapperClass(OrderMap.class);
        orderbMain.setMapOutputKeyClass(Text.class);
        orderbMain.setMapOutputValueClass(DoubleWritable.class);

        orderbMain.setReducerClass(OrderReduce.class);
        orderbMain.setOutputKeyClass(OrderBean.class);
        orderbMain.setOutputValueClass(NullWritable.class);
        orderbMain.setOutputFormatClass(TextOutputFormat.class);
        TextOutputFormat.setOutputPath(orderbMain, new Path("file:///E:\\BaiduNetdiskDownload\\zookeeper\\day02\\day02\\代码\\day02_zookeeper_api_demo\\src\\main\\java\\cn\\itcast\\zookeeper_api\\exce\\order\\"));
        boolean res = orderbMain.waitForCompletion(true);
        return res ? 0 : 1;
    }
}
