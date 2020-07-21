package cn.itcast.zookeeper_api.exce.exec14;

import org.apache.hadoop.io.WritableComparable;
import org.apache.hadoop.io.WritableComparator;

/**
 * 自定义分组实现逻辑
 */
public class MyGroup extends WritableComparator {

    public MyGroup() {
        super(SortBean.class, true);
    }

    @Override
    public int compare(WritableComparable a, WritableComparable b) {
        SortBean sortBean = (SortBean) a;
        SortBean bean = (SortBean) b;
        return sortBean.getSort().compareTo(bean.getSort());
    }
}
