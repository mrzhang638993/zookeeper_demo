package cn.itcast.zookeeper_api.exce.exec14;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.conf.Configured;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.input.TextInputFormat;
import org.apache.hadoop.mapreduce.lib.output.TextOutputFormat;
import org.apache.hadoop.util.Tool;
import org.apache.hadoop.util.ToolRunner;

public class SortMain extends Configured implements Tool {
    public static void main(String[] args) throws Exception {
        int run = ToolRunner.run(new Configuration(), new SortMain(), args);
        System.exit(run);
    }

    @Override
    public int run(String[] args) throws Exception {
        Job orderJob = Job.getInstance(super.getConf(), "orderJob");
        orderJob.setJarByClass(SortMain.class);

        //  对应的执行相关的业务逻辑实现
        orderJob.setInputFormatClass(TextInputFormat.class);
        TextInputFormat.addInputPath(orderJob, new Path("file:///E:\\BaiduNetdiskDownload\\zookeeper\\day02\\day02\\code\\day02_zookeeper_api_demo\\src\\main\\java\\cn\\itcast\\zookeeper_api\\exce\\exec14\\input\\data.txt"));
        //  设置mapper极段的参数
        orderJob.setMapperClass(SortMap.class);
        orderJob.setMapOutputKeyClass(SortBean.class);
        orderJob.setMapOutputValueClass(Text.class);
        //  设置shuffle阶段的3-7操作的
        // 指定分区
        //  对应的输出文件信息,对应的这个5怎么计算出来的，这个需要后续的逻辑和校验的。
        // 设置分区个数的话,对应的输出结果会指定n个文件存放，不指定的话，对应的是一个分区的，只会形成一个文件的。
        //  指定分组
        orderJob.setGroupingComparatorClass(MyGroup.class);
        //  设置reducer
        orderJob.setReducerClass(SortReduce.class);
        orderJob.setOutputKeyClass(Text.class);
        orderJob.setOutputValueClass(Text.class);
        //设置输出的参数和路径
        orderJob.setOutputFormatClass(TextOutputFormat.class);
        TextOutputFormat.setOutputPath(orderJob, new Path("file:///E:\\BaiduNetdiskDownload\\zookeeper\\day02\\day02\\code\\day02_zookeeper_api_demo\\src\\main\\java\\cn\\itcast\\zookeeper_api\\exce\\exec14\\output"));
        boolean res = orderJob.waitForCompletion(true);
        return res ? 0 : 1;
    }
}
