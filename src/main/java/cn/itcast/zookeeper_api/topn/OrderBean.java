package cn.itcast.zookeeper_api.topn;

import org.apache.hadoop.io.WritableComparable;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

/**
 * 封装bean,序列化，反序列化以及排序操作实现
 */
public class OrderBean implements WritableComparable<OrderBean> {

    private String orderId;
    private Double price;

    @Override
    public int compareTo(OrderBean o) {
        //  指定对应的排序规则，bean封装可以指定复杂的排序规则和技巧
        int result = this.orderId.compareTo(o.orderId);
        if (result == 0) {
            //  设置订单价格的降序排列
            return o.price.compareTo(this.price);
        } else {
            return result;
        }
    }

    @Override
    public void write(DataOutput out) throws IOException {
        out.writeUTF(this.orderId);
        out.writeDouble(this.price);
    }

    @Override
    public void readFields(DataInput in) throws IOException {
        this.orderId = in.readUTF();
        this.price = in.readDouble();
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    @Override
    public String toString() {
        return
                "orderId='" + orderId + '\'' +
                        ", price=" + price;

    }
}
