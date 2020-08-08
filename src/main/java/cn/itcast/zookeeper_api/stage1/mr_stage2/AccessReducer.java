package cn.itcast.zookeeper_api.stage1.mr_stage2;

import com.alibaba.fastjson.JSON;
import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;
import org.hamcrest.core.IsInstanceOf;
import org.junit.Assert;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;


/**
 * 在mapreduce中使用集合等的数据结构进行操作
 * 这里面的话，需要对于text的内容进行排序操作实现的,需要text实现排序接口的。已经实现了。
 */
public class AccessReducer extends Reducer<Text, AccessLogBean, AccessLogBean, NullWritable> {

    @Override
    protected void setup(Context context) throws IOException, InterruptedException {
        System.out.println("======123=====");
        super.setup(context);
    }

    /**
     * 执行reduce的业务逻辑处理.我需要直到每一个url访问的次数的？
     * 计算结果失败，存在key对应的value被覆盖的问题的。
     */
    @Override
    protected void reduce(Text key, Iterable<AccessLogBean> values, Context context) throws IOException, InterruptedException {
        //  获取分组内部的topN问题,分组内部进行排序操作实现
        //  对应的将相同的可以分配到一组这种进行处理操作。ip相同的分到一组的。根据url的顺序排列的
        String url = null;
        //  需要判断values的长度的。
        List<AccessLogBean> accessLogBeanList = new ArrayList<>();
        List<AccessLogBean> init = new ArrayList<>();
        AccessLogBean accessLogBean = null;
        // values的数值在转换成为value的时候，对应的内容已经被覆盖了，整个的逻辑存在问题的。
        values.forEach(x->{
            init.add(x);
        });
        //  根据url进行排序操作
        Collections.sort(init, new Comparator<AccessLogBean>() {
            @Override
            public int compare(AccessLogBean o1, AccessLogBean o2) {
                return o1.getUrl().compareTo(o2.getUrl());
            }
        });
        for (AccessLogBean value : init
        ) {
            if (url == null) {
                url = value.getUrl();
                accessLogBean = new AccessLogBean();
                accessLogBean.setIp(value.getIp());
                accessLogBean.setUrl(url);
                accessLogBean.setCount(1);
                accessLogBeanList.add(accessLogBean);
            } else {
                // 此时url不等于
                if (url.equals(value.getUrl())) {
                    accessLogBean.setCount(accessLogBean.getCount() + 1);
                } else {
                    //  此时split的元素对应的不是原来的元素的。url发生改变了
                    accessLogBean = new AccessLogBean();
                    accessLogBean.setIp(value.getIp());
                    accessLogBean.setUrl(value.getUrl());
                    accessLogBean.setCount(1);
                    url = value.getUrl();
                    accessLogBeanList.add(accessLogBean);
                }
            }
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
