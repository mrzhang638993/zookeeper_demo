package cn.itcast.zookeeper_api.topn;

import org.apache.hadoop.io.WritableComparable;
import org.apache.hadoop.io.WritableComparator;

/**
 * 实现自定义分组
 * 1.继承WritableComparator
 * 2.调用父类的有参构造
 * 3.指定分组的规则(重写方法)
 */
public class OrderGroup extends WritableComparator {

    public OrderGroup() {
        //  指定javaBean的实例，以及是否允许创建对应的实例
        super(OrderBean.class, true);
    }

    /**
     * 重写父类的方法和实现逻辑
     */
    @Override
    public int compare(WritableComparable a, WritableComparable b) {
        //3.1对形参做强制类型转换
        //3.2指定分组规则
        OrderBean first = (OrderBean) a;
        OrderBean second = (OrderBean) b;
        // 指定分组规则。
        return first.getOrderId().compareTo(second.getOrderId());
    }
}
