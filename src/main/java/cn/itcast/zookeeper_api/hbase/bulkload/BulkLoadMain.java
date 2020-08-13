package cn.itcast.zookeeper_api.hbase.bulkload;

import cn.itcast.zookeeper_api.hbase.hbase_mr.HBaseMain;
import cn.itcast.zookeeper_api.hbase.hbase_mr.HBaseSinkReducer;
import cn.itcast.zookeeper_api.hbase.hbase_mr.HBaseSourceMapper;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.conf.Configured;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.TableName;
import org.apache.hadoop.hbase.client.*;
import org.apache.hadoop.hbase.io.ImmutableBytesWritable;
import org.apache.hadoop.hbase.mapreduce.HFileOutputFormat2;
import org.apache.hadoop.hbase.mapreduce.TableMapReduceUtil;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.input.TextInputFormat;
import org.apache.hadoop.util.Tool;
import org.apache.hadoop.util.ToolRunner;

public class BulkLoadMain extends Configured implements Tool {
    @Override
    public int run(String[] args) throws Exception {
        Job hbase = Job.getInstance(super.getConf(), "hbase");
        //  使用hadoop上传代码操作实现，打包上传到hadoop的话，必须设置这个选项的。
        hbase.setJarByClass(BulkLoadMain.class);
        //  定义mapper类和reducer类的数据
        hbase.setMapperClass(HDFSMapper.class);
        hbase.setInputFormatClass(TextInputFormat.class);
        TextInputFormat.addInputPath(hbase,new Path("hdfs://node01:8020/hbase/input"));
        hbase.setMapOutputKeyClass(ImmutableBytesWritable.class);
        hbase.setMapOutputValueClass(Put.class);
      //  文件输出为hfile格式
        // Job job, Table table, RegionLocator regionLocator
        Connection connection = ConnectionFactory.createConnection(super.getConf());
        TableName tableName = TableName.valueOf("myuser2");
        Table myuser2 = connection.getTable(tableName);
        // 配置增量增加数据
        HFileOutputFormat2.configureIncrementalLoad(hbase,myuser2,connection.getRegionLocator(tableName));
        //  设置输出类型，决定了输出文件的格式是什么类型的。
        hbase.setOutputFormatClass(HFileOutputFormat2.class);
        HFileOutputFormat2.setOutputPath(hbase,new Path("hdfs://node01:8020/hbase/hfile_out"));
        boolean b = hbase.waitForCompletion(true);
        return b?0:1;
    }

    public static void main(String[] args) throws Exception {
        System.setProperty("HADOOP_USER_NAME", "root");
        Configuration configuration = HBaseConfiguration.create();
        configuration.set("hbase.zookeeper.property.clientPort", "2181");
        configuration.set("hbase.zookeeper.quorum", "node01,node02,node03");
        int run = ToolRunner.run(configuration, new BulkLoadMain(), args);
        System.exit(run);
    }
}
