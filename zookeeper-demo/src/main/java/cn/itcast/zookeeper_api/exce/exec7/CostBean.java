package cn.itcast.zookeeper_api.exce.exec7;

import org.apache.hadoop.io.WritableComparable;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

/**
 * 完成相关的cost排序操作实现逻辑
 */
public class CostBean implements WritableComparable<CostBean> {

    /**
     * 总的支出情况
     */
    private int totalCost;
    /**
     * 总的收入情况
     */
    private int totalIncome;
    /**
     * 总的近利润情况
     */
    private int totalBenifit;

    /**
     * 账户id
     */
    private String key;


    @Override
    public int compareTo(CostBean o) {
        return this.totalBenifit - o.totalBenifit;
    }

    @Override
    public void write(DataOutput out) throws IOException {
        out.writeInt(this.totalCost);
        out.writeInt(this.totalIncome);
        out.writeInt(this.totalBenifit);
        out.writeUTF(this.key);
    }

    @Override
    public void readFields(DataInput in) throws IOException {
        this.totalCost = in.readInt();
        this.totalIncome = in.readInt();
        this.totalBenifit = in.readInt();
        this.key = in.readUTF();
    }

    public int getTotalBenifit() {
        return totalBenifit;
    }

    public void setTotalBenifit(int totalBenifit) {
        this.totalBenifit = totalBenifit;
    }

    public void setTotalIncome(int totalIncome) {
        this.totalIncome = totalIncome;
    }

    public void setTotalCost(int totalCost) {
        this.totalCost = totalCost;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    @Override
    public String toString() {
        return
                key +
                        "\t" + totalIncome +
                        "\t" + totalCost +
                        "\t" + totalBenifit;
    }
}
