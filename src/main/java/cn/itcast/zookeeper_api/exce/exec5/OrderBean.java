package cn.itcast.zookeeper_api.exce.exec5;


import org.apache.hadoop.io.WritableComparable;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

/**
 * 对应的封装相关的业务bean对象
 */
public class OrderBean implements WritableComparable<OrderBean> {
    private String key;
    private double num;

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public double getNum() {
        return num;
    }

    public void setNum(double num) {
        this.num = num;
    }

    @Override
    public void write(DataOutput out) throws IOException {
        out.writeUTF(this.key);
        out.writeDouble(this.num);
    }

    @Override
    public void readFields(DataInput in) throws IOException {
        this.key = in.readUTF();
        this.num = in.readDouble();
    }

    @Override
    public String toString() {
        return String.format("%s\t%.2f", key, num);
    }

    /**
     * 根据订单号实现升序排列
     */
    @Override
    public int compareTo(OrderBean o) {
        return this.key.compareTo(o.getKey());
    }
}
