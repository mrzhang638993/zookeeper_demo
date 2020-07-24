package cn.itcast.zookeeper_api.exce.exec13;


import org.apache.hadoop.io.WritableComparable;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

public class ScoreBean implements WritableComparable<ScoreBean> {
    private String stuClass;
    private String lec;
    private Double avgScore;

    @Override
    public int compareTo(ScoreBean o) {
        // 首先根据班级，然后根据课程进行排序
        int result = this.stuClass.compareTo(o.stuClass);
        if (result == 0) {
            return o.lec.compareTo(this.lec);
        } else {
            return result;
        }
    }

    @Override
    public void write(DataOutput out) throws IOException {
        out.writeUTF(this.stuClass);
        out.writeUTF(this.lec);
        out.writeDouble(this.avgScore);
    }

    @Override
    public void readFields(DataInput in) throws IOException {
        this.stuClass = in.readUTF();
        this.lec = in.readUTF();
        this.avgScore = in.readDouble();
    }

    public String getStuClass() {
        return stuClass;
    }

    public void setStuClass(String stuClass) {
        this.stuClass = stuClass;
    }

    public String getLec() {
        return lec;
    }

    public void setLec(String lec) {
        this.lec = lec;
    }

    public Double getAvgScore() {
        return avgScore;
    }

    public void setAvgScore(Double avgScore) {
        this.avgScore = avgScore;
    }

    @Override
    public String toString() {
        return
                "stuClass='" + stuClass + '\'' +
                        ", lec='" + lec + '\'' +
                        ", avgScore=" + avgScore
                ;
    }
}
