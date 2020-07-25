package cn.itcast.zookeeper_api.exce.exec10;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.conf.Configured;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.input.TextInputFormat;
import org.apache.hadoop.mapreduce.lib.output.TextOutputFormat;
import org.apache.hadoop.util.Tool;
import org.apache.hadoop.util.ToolRunner;

import java.net.URI;

public class OrderMainJob extends Configured implements Tool {
    public static void main(String[] args) throws Exception {
        int run = ToolRunner.run(new Configuration(), new OrderMainJob(), args);
        System.exit(run);
    }

    @Override
    public int run(String[] args) throws Exception {
        // 获取job对象
        Job orderMainJob = Job.getInstance(super.getConf(), "orderMainJob");
        orderMainJob.setJarByClass(OrderMainJob.class);
        // 设置job对象(将小表放置到分布式缓存中)
        //  将小表放到分布式缓存中。早期使用的是DistributedCache，需要知道即可。
        // DistributedCache.addCacheFile(new URI("hdfs://node01:8020/cache_file/product.txt"),super.getConf());
        //  小表放置到分布式缓存中
        orderMainJob.addCacheFile(new URI("hdfs://node01:8020/exec10/pdts.txt"));
        //  设置输入类和输入路径：
        orderMainJob.setInputFormatClass(TextInputFormat.class);
        TextInputFormat.addInputPath(orderMainJob, new Path("file:///E:\\BaiduNetdiskDownload\\zookeeper\\day02\\day02\\代码\\day02_zookeeper_api_demo\\src\\main\\java\\cn\\itcast\\zookeeper_api\\exce\\exec10\\input\\order.txt"));
        //  设置map类，以及k2,v2的类型
        orderMainJob.setMapperClass(OrderMap.class);
        orderMainJob.setMapOutputKeyClass(Text.class);
        orderMainJob.setMapOutputValueClass(Text.class);
        //  第八步：设置输出类和输出路径
        orderMainJob.setOutputFormatClass(TextOutputFormat.class);
        TextOutputFormat.setOutputPath(orderMainJob, new Path("file:///E:\\BaiduNetdiskDownload\\zookeeper\\day02\\day02\\代码\\day02_zookeeper_api_demo\\src\\main\\java\\cn\\itcast\\zookeeper_api\\exce\\exec10\\output"));
        // 等待任务结束
        boolean res = orderMainJob.waitForCompletion(true);
        return res ? 0 : 1;
    }
}
