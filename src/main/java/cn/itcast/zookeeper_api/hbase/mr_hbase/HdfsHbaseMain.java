package cn.itcast.zookeeper_api.hbase.mr_hbase;

import cn.itcast.zookeeper_api.hbase.hbase_mr.HBaseMain;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.conf.Configured;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.mapreduce.TableMapReduceUtil;
import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.input.TextInputFormat;
import org.apache.hadoop.util.Tool;
import org.apache.hadoop.util.ToolRunner;

public class HdfsHbaseMain  extends Configured implements Tool {
    @Override
    public int run(String[] args) throws Exception {
        Job hdfsMain = Job.getInstance(super.getConf(), "hdfsMain");
        hdfsMain.setJarByClass(HdfsHbaseMain.class);
        hdfsMain.setMapperClass(HDFSMapper.class);
        hdfsMain.setInputFormatClass(TextInputFormat.class);
        TextInputFormat.addInputPath(hdfsMain,new Path("hdfs://node01:8020/hbase/input"));
        hdfsMain.setMapOutputKeyClass(Text.class);
        hdfsMain.setMapOutputValueClass(NullWritable.class);
        //  分区，排序，规约，分组全部省略
        //  下面设置reduce类对象
        TableMapReduceUtil.initTableReducerJob("myuser2",HBASEWriteReducer.class,hdfsMain);
        boolean b = hdfsMain.waitForCompletion(true);
        return b?0:1;
    }

    public static void main(String[] args) throws Exception {
        Configuration configuration = HBaseConfiguration.create();
        configuration.set("hbase.zookeeper.property.clientPort", "2181");
        configuration.set("hbase.zookeeper.quorum", "node01,node02,node03");
        int run = ToolRunner.run(configuration, new HdfsHbaseMain(), args);
        System.exit(run);
    }
}
