package cn.itcast.zookeeper_api.self_input_format;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.conf.Configured;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.BytesWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.output.SequenceFileOutputFormat;
import org.apache.hadoop.util.Tool;
import org.apache.hadoop.util.ToolRunner;

/**
 * job的主类数据
 */
public class JobMain extends Configured implements Tool {
    public static void main(String[] args) throws Exception {
        int run = ToolRunner.run(new Configuration(), new JobMain(), args);
        System.exit(run);
    }

    @Override
    public int run(String[] args) throws Exception {
        //  获取job对象
        Job jobMain = Job.getInstance(super.getConf(), "jobMain");
        //  设置job任务(总的包括8个步骤的)
        jobMain.setInputFormatClass(MyInputFormat.class);
        MyInputFormat.addInputPath(jobMain, new Path("file:///E:\\BaiduNetdiskDownload\\zookeeper\\day02\\day02\\code\\day02_zookeeper_api_demo\\src\\main\\java\\cn\\itcast\\zookeeper_api\\self_input_format\\input"));
        jobMain.setMapperClass(SequenceFileMapper.class);
        jobMain.setMapOutputKeyClass(Text.class);
        jobMain.setMapOutputValueClass(BytesWritable.class);
        //  shuffle阶段不做处理和实现
        //  reduce阶段对应的不会执行处理逻辑的，本案例的话不需要设置reduce类型的。当时对应的输入和输出必须进行设置
        jobMain.setOutputKeyClass(Text.class);
        jobMain.setOutputValueClass(BytesWritable.class);
        //设置输出路径和输出文件
        jobMain.setOutputFormatClass(SequenceFileOutputFormat.class);
        SequenceFileOutputFormat.setOutputPath(jobMain, new Path("file:///E:\\BaiduNetdiskDownload\\zookeeper\\day02\\day02\\code\\day02_zookeeper_api_demo\\src\\main\\java\\cn\\itcast\\zookeeper_api\\self_input_format\\output"));
        //  等待job执行结果
        //  生成的二进制文件对应的可以作为中间文件进行操作管理
        boolean res = jobMain.waitForCompletion(true);
        return res ? 0 : 1;
    }
}
