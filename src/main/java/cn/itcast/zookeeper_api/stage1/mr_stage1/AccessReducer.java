package cn.itcast.zookeeper_api.stage1.mr_stage1;

import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;


/**
 * 在mapreduce中使用集合等的数据结构进行操作
 * 这里面的话，需要对于text的内容进行排序操作实现的,需要text实现排序接口的。已经实现了。
 */
public class AccessReducer extends Reducer<AccessLogBean, Text, AccessLogBean, NullWritable> {

    @Override
    protected void setup(Context context) throws IOException, InterruptedException {
        System.out.println("======123=====");
        super.setup(context);
    }

    /**
     * 执行reduce的业务逻辑处理.我需要直到每一个url访问的次数的？
     */
    @Override
    protected void reduce(AccessLogBean key, Iterable<Text> values, Context context) throws IOException, InterruptedException {
        //  对应的将相同的可以分配到一组这种进行处理操作。ip相同的分到一组的。根据url的顺序排列的
        String url = null;
        //  需要判断values的长度的。
        List<AccessLogBean> accessLogBeanList = new ArrayList<>();
        AccessLogBean accessLogBean = null;
        for (Text value : values
        ) {
            String content = value.toString();
            String[] split = content.split(",");
            if (url == null) {
                url = split[2];
                accessLogBean = new AccessLogBean();
                accessLogBean.setIp(key.getIp());
                accessLogBean.setUrl(url);
                accessLogBean.setCount(1);
            } else {
                // 此时url不等于
                if (url.equals(split[2])) {
                    accessLogBean.setCount(accessLogBean.getCount() + 1);
                } else {
                    //  此时split的元素对应的不是原来的元素的。url发生改变了
                    accessLogBeanList.add(accessLogBean);
                    accessLogBean = new AccessLogBean();
                    accessLogBean.setIp(key.getIp());
                    accessLogBean.setUrl(split[2]);
                    accessLogBean.setCount(1);
                    url = split[2];
                }
            }
        }
        if (accessLogBean.getCount() == 1) {
            accessLogBeanList.add(accessLogBean);
        }
        //  进行key的排序操作
        Collections.sort(accessLogBeanList, new Comparator<AccessLogBean>() {
            @Override
            public int compare(AccessLogBean o1, AccessLogBean o2) {
                return o2.getCount() - o1.getCount();
            }
        });
        //进行topN的排序操作。
        if (accessLogBeanList.size() >= 3) {
            //  进行top2的操作
            List<AccessLogBean> accessLogBeans = accessLogBeanList.subList(0, 3);
            for (int i = 0; i < accessLogBeans.size(); i++) {
                context.write(accessLogBeans.get(i), NullWritable.get());
            }
        } else {
            // 执行全部输出操作
            for (int i = 0; i < accessLogBeanList.size(); i++) {
                context.write(accessLogBeanList.get(i), NullWritable.get());
            }
        }
    }
}
