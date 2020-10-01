package cn.itcast.zookeeper_api.stage1.mr_stage3.topN;

import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;

import java.io.IOException;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

/**
 * 统计出来每一个url对应的独立的用户数信息
 */
public class VistsUrlReducer extends Reducer<Text, Text, VistsUrlBean, NullWritable> {


    @Override
    protected void reduce(Text key, Iterable<Text> values, Context context) throws IOException, InterruptedException {
        //  key 对应的是url信息
        Set<VistsUrlBean> vistsUrlBeanList = new HashSet<>();
        // 获取到url下面的所有的独立访客数信息
        for (Text value : values) {
            // 192.168.1.11	2017-11-20 10:02	url1	1	tom	null	null
            VistsUrlBean vistsUrlBean1 = new VistsUrlBean();
            String content = value.toString();
            String[] split = content.split("\t");
            boolean result = checkIfAlreadyExists(vistsUrlBeanList, split);
            if (!result) {
                //  元素不存在的话，需要增加元素的。
                vistsUrlBean1.setUrl(key.toString());
                vistsUrlBean1.setUser(split[4]);
                vistsUrlBean1.setCount(1);
                vistsUrlBeanList.add(vistsUrlBean1);
            }
        }
        // 下面构建每一个url对应的独立访客的数据
        VistsUrlBean vistsUrlBean = new VistsUrlBean();
        vistsUrlBean.setUrl(key.toString());
        vistsUrlBean.setCount(vistsUrlBeanList.size());
        //  获取到topN的操作
        context.write(vistsUrlBean, NullWritable.get());
    }

    /**
     * 检查元素是否已经存在
     */
    private boolean checkIfAlreadyExists(Set<VistsUrlBean> vistsUrlBeanList, String[] split) {
        if (vistsUrlBeanList.size() == 0) {
            return false;
        } else {
            Iterator<VistsUrlBean> iterator = vistsUrlBeanList.iterator();
            while (iterator.hasNext()) {
                VistsUrlBean next = iterator.next();
                String url = split[2];
                String user = split[4];
                if (url.equals(next.getUrl()) && user.equals(next.getUser())) {
                    return true;
                }
            }
            return false;
        }
    }
}
