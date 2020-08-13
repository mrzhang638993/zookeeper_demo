package cn.itcast.zookeeper_api.hbase.hbase_mr;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.conf.Configured;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.client.Put;
import org.apache.hadoop.hbase.client.Scan;
import org.apache.hadoop.hbase.mapreduce.TableMapReduceUtil;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.util.Tool;
import org.apache.hadoop.util.ToolRunner;

public class HBaseMain  extends Configured implements Tool {
    @Override
    public int run(String[] args) throws Exception {
        Job hbase = Job.getInstance(super.getConf(), "hbase");
        //  使用hadoop上传代码操作实现，打包上传到hadoop的话，必须设置这个选项的。
        hbase.setJarByClass(HBaseMain.class);
        //  定义mapper类和reducer类的数据
        Scan scan=new Scan();
        TableMapReduceUtil.initTableMapperJob("myuser",scan,HBaseSourceMapper.class, Text.class, Put.class,hbase,false);
        TableMapReduceUtil.initTableReducerJob("myuser2",HBaseSinkReducer.class,hbase);
        boolean b = hbase.waitForCompletion(true);
        return b?0:1;
    }
     //  程序的入口类
    public static void main(String[] args) throws Exception {
        Configuration configuration = HBaseConfiguration.create();
        configuration.set("hbase.zookeeper.property.clientPort", "2181");
        configuration.set("hbase.zookeeper.quorum", "node01,node02,node03");
        int run = ToolRunner.run(configuration, new HBaseMain(), args);
        System.exit(run);
    }
}
