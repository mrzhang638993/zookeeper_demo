package cn.itcast.zookeeper_api.stage1.mr_stage2;


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

public class AccessMain extends Configured implements Tool {

    public static void main(String[] args) throws Exception {
        int run = ToolRunner.run(new Configuration(), new AccessMain(), args);
        System.exit(run);
    }

    @Override
    public int run(String[] args) throws Exception {
        // 编写主要的业务逻辑
        Job orderJob = Job.getInstance(super.getConf(), "accessJob");
        orderJob.setJarByClass(AccessMain.class);

        //  对应的执行相关的业务逻辑实现
        orderJob.setInputFormatClass(TextInputFormat.class);
        TextInputFormat.addInputPath(orderJob, new Path("file:///F:\\works\\hadoop1\\zookeeper-demo\\src\\main\\java\\cn\\itcast\\zookeeper_api\\stage1\\data\\visit.log"));
        //  设置mapper极段的参数
        orderJob.setMapperClass(AccessMapper.class);
        orderJob.setMapOutputKeyClass(Text.class);
        orderJob.setMapOutputValueClass(AccessLogBean.class);
        //  设置shuffle阶段的3-7操作的
        // 指定分区
        orderJob.setPartitionerClass(AccessPartition.class);
        // 设置分区个数的话,对应的输出结果会指定n个文件存放，不指定的话，对应的是一个分区的，只会形成一个文件的。
        //  指定分组
        orderJob.setGroupingComparatorClass(AccessGroup.class);
        //orderJob.setNumReduceTasks(0);
        //  设置reducer
        orderJob.setReducerClass(AccessReducer.class);
        orderJob.setOutputKeyClass(AccessLogBean.class);
        orderJob.setOutputValueClass(NullWritable.class);
        //设置输出的参数和路径
        orderJob.setOutputFormatClass(TextOutputFormat.class);
        TextOutputFormat.setOutputPath(orderJob, new Path("file:///F:\\works\\hadoop1\\zookeeper-demo\\src\\main\\java\\cn\\itcast\\zookeeper_api\\stage1\\mr_stage2\\output"));
        boolean res = orderJob.waitForCompletion(true);
        return res ? 0 : 1;
    }
}
