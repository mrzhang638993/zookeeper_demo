package cn.itcast.zookeeper_api.exce.exec14;

import org.apache.hadoop.io.WritableComparable;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

public class SortBean implements WritableComparable<SortBean> {

    private String sort;
    private int sortValue;

    @Override
    public int compareTo(SortBean o) {
        int result = this.sort.compareTo(o.sort);
        if (result == 0) {
            return this.sortValue - o.sortValue;
        } else {
            return result;
        }
    }

    @Override
    public void write(DataOutput out) throws IOException {
        out.writeUTF(this.sort);
        out.writeInt(this.sortValue);
    }

    @Override
    public void readFields(DataInput in) throws IOException {
        this.sort = in.readUTF();
        this.sortValue = in.readInt();
    }

    public String getSort() {
        return sort;
    }

    public void setSort(String sort) {
        this.sort = sort;
    }

    public int getSortValue() {
        return sortValue;
    }

    public void setSortValue(int sortValue) {
        this.sortValue = sortValue;
    }

    @Override
    public String toString() {
        return
                "sort='" + sort + '\'' +
                        ", sortValue=" + sortValue;
    }
}
