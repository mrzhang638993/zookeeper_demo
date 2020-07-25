package cn.itcast.zookeeper_api.join.map;


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

public class JobMain extends Configured implements Tool {
    public static void main(String[] args) throws Exception {
        int run = ToolRunner.run(new Configuration(), new JobMain(), args);
        System.exit(run);
    }

    @Override
    public int run(String[] args) throws Exception {
        // 获取job对象
        Job mapJoinJob = Job.getInstance(super.getConf(), "map_join_job");
        mapJoinJob.setJarByClass(JobMain.class);
        // 设置job对象(将小表放置到分布式缓存中)
        //  将小表放到分布式缓存中。早期使用的是DistributedCache，需要知道即可。
        // DistributedCache.addCacheFile(new URI("hdfs://node01:8020/cache_file/product.txt"),super.getConf());
        //  小表放置到分布式缓存中
        mapJoinJob.addCacheFile(new URI("hdfs://node01:8020/cache_file/product.txt"));
        //  设置输入类和输入路径：
        mapJoinJob.setInputFormatClass(TextInputFormat.class);
        TextInputFormat.addInputPath(mapJoinJob, new Path("file:///E:\\BaiduNetdiskDownload\\zookeeper\\day02\\day02\\代码\\day02_zookeeper_api_demo\\src\\main\\java\\cn\\itcast\\zookeeper_api\\join\\input\\orders.txt"));
        //  设置map类，以及k2,v2的类型
        mapJoinJob.setMapperClass(MapJoinMapper.class);
        mapJoinJob.setMapOutputKeyClass(Text.class);
        mapJoinJob.setMapOutputValueClass(Text.class);
        //  第八步：设置输出类和输出路径
        mapJoinJob.setOutputFormatClass(TextOutputFormat.class);
        TextOutputFormat.setOutputPath(mapJoinJob, new Path("file:///E:\\BaiduNetdiskDownload\\zookeeper\\day02\\day02\\代码\\day02_zookeeper_api_demo\\src\\main\\java\\cn\\itcast\\zookeeper_api\\join\\map\\output"));
        // 等待任务结束
        boolean res = mapJoinJob.waitForCompletion(true);
        return res ? 0 : 1;
    }
}
