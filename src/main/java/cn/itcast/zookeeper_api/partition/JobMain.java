package cn.itcast.zookeeper_api.partition;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.conf.Configured;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.input.TextInputFormat;
import org.apache.hadoop.mapreduce.lib.output.TextOutputFormat;
import org.apache.hadoop.util.Tool;
import org.apache.hadoop.util.ToolRunner;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

public class JobMain extends Configured implements Tool {
    private static void judgePathIsExists(Path path) throws URISyntaxException, IOException, InterruptedException {
        //  本地的hdfs的配置
        //FileSystem fs=FileSystem.getLocal(new Configuration());
        //  集群模式配置
        FileSystem fs = FileSystem.get(new URI("hdfs://node01:8020"), new Configuration());
        fs.deleteOnExit(path);
        fs.close();
    }

    public static void main(String[] args) throws Exception {
        int run = ToolRunner.run(new Configuration(), new JobMain(), args);
        //  计算对应的推出状态数据
        System.exit(run);
    }

    @Override
    public int run(String[] args) throws Exception {
        //  步骤一：创建job任务对象
        Job partion = Job.getInstance(super.getConf(), "partion");
        partion.setJarByClass(JobMain.class);
        //  对于job任务配置,需要8个步骤
        //  步骤1：设置输入路径和输入参数
        partion.setInputFormatClass(TextInputFormat.class);
        TextInputFormat.setInputPaths(partion, new Path("hdfs://node01:8020/input"));
        //  步骤2：设置map以及对应的输出数据格式
        partion.setMapperClass(PartitionMapper.class);
        partion.setMapOutputKeyClass(Text.class);
        partion.setMapOutputValueClass(NullWritable.class);
        //  步骤3,4,5,6: 设置分区操作逻辑，以及分区数据
        partion.setPartitionerClass(MyPartition.class);
        partion.setNumReduceTasks(2);
        //  步骤4,5,6: 采用默认操作逻辑
        // 步骤7  指定reducer以及对应的数据格式
        partion.setReducerClass(MyReducer.class);
        partion.setOutputKeyClass(Text.class);
        partion.setOutputValueClass(NullWritable.class);
        // 步骤8 ： 指定输出的类型
        partion.setOutputValueClass(TextOutputFormat.class);
        Path path = new Path("hdfs://node01:8020/output/partition_out");
        TextOutputFormat.setOutputPath(partion, path);
        judgePathIsExists(path);
        boolean res = partion.waitForCompletion(true);
        //  等待任务结束
        return res ? 0 : 1;
    }
}
