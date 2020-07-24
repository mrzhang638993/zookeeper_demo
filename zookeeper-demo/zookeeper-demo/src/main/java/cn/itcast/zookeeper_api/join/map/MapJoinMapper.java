package cn.itcast.zookeeper_api.join.map;

import org.apache.hadoop.fs.FSDataInputStream;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;

import java.net.URI;
import java.util.HashMap;
import java.util.Map;

/**
 * KEYIN, LongWritable
 * VALUEIN, Text
 * KEYOUT,  Text
 * VALUEOUT  Text
 */
public class MapJoinMapper extends Mapper<LongWritable, Text, Text, Text> {

    private Map map = new HashMap<String, String>();

    /**
     * 需要执行如下的情况：
     * 1.将分布式缓存中的小表的缓存数据读取到本地的map集合中(只需要做一次,不适合在map方法中使用的)
     * 2.对大表的处理的业务逻辑，实现大表和小表的join操作。
     */
    @Override
    protected void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {
        // 第一步：获取对应的productId中的key数据
        String[] split = value.toString().split(",");
        String productId = split[2];
        // 从map中的数据提取数据，获取到产品数据
        String content = (String) map.get(productId);
        String finalContent = content + "\t" + value.toString();
        //  对应的是每一个订单的全部的信息
        context.write(new Text(productId), new Text(finalContent));
    }

    /**
     * 对应的map中的setup对应的只会执行一次操作的
     */
    @Override
    protected void setup(Context context) throws IOException, InterruptedException {
        //  获取分布式文件列表
        URI[] cacheFiles = context.getCacheFiles();
        //获取指定的分布式缓存文件的文件系统，filesystem
        FileSystem fs = FileSystem.get(cacheFiles[0], context.getConfiguration());
        //  获取文件的输入流，读取文件的内容
        FSDataInputStream open = fs.open(new Path(cacheFiles[0]));
        // 读取文件内存,并将数据存入到map集合，将字节缓冲流转化为字符缓冲流
        //  这个方法比较的话，InputStreamReader 操作很合适，比较好
        BufferedReader br = new BufferedReader(new InputStreamReader(open.getWrappedStream()));
        String content = null;
        while ((content = br.readLine()) != null) {
            String[] split = content.split(",");
            map.put(split[0], content);
        }
        // 关闭流操作，先产生的流后面关闭
        br.close();
        fs.close();
    }
}
