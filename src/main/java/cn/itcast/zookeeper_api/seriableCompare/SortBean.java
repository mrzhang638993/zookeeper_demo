package cn.itcast.zookeeper_api.seriableCompare;

import org.apache.hadoop.io.WritableComparable;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

/**
 * mapreduce实现排序序列化接口
 */
public class SortBean implements WritableComparable<SortBean> {

    private String word;
    private int num;

    /**
     * 实现比较器，定义排序规则
     */
    @Override
    public int compareTo(SortBean sortBean) {
        /**
         *  规则：首先排序word，升序排列，然后执行排序num升序排列
         *  字符串默认的是按照字典序排列的
         * */
        int result = this.getWord().compareTo(sortBean.getWord());
        if (result == 0) {
            return this.getNum() - sortBean.getNum();//执行升序排列
        } else {
            return result;
        }
    }

    /**
     * 实现序列化的接口
     */
    @Override
    public void write(DataOutput out) throws IOException {
        out.writeUTF(word);
        out.writeInt(num);
    }

    /**
     * 实现反序列化的接口
     */
    @Override
    public void readFields(DataInput in) throws IOException {
        this.word = in.readUTF();
        this.num = in.readInt();
    }

    public String getWord() {
        return word;
    }

    public void setWord(String word) {
        this.word = word;
    }

    public int getNum() {
        return num;
    }

    public void setNum(int num) {
        this.num = num;
    }

    @Override
    public String toString() {
        return word + '\t' +
                num;
    }
}
