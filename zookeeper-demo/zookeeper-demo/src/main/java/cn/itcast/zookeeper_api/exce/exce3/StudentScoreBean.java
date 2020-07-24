package cn.itcast.zookeeper_api.exce.exce3;

import org.apache.hadoop.io.WritableComparable;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;

/**
 * 实现学生成绩的封装操作实现
 */
public class StudentScoreBean implements WritableComparable<StudentScoreBean> {

    private List<Integer> scoreList;

    private double totalScore;

    private double avgScore;

    private String stuKey;

    @Override
    public int compareTo(StudentScoreBean st) {
        // 对应的执行比较操作,double类型的数据无法比较大小的.降序排列
        BigDecimal bigDecimal = new BigDecimal(st.totalScore);
        BigDecimal decimal = new BigDecimal(this.totalScore);
        return bigDecimal.compareTo(decimal);
    }

    @Override
    public void write(DataOutput out) throws IOException {
        out.writeUTF(this.stuKey);
        out.writeDouble(this.avgScore);
        out.writeDouble(this.totalScore);
    }

    @Override
    public void readFields(DataInput in) throws IOException {
        this.stuKey = in.readUTF();
        this.avgScore = in.readDouble();
        this.totalScore = in.readDouble();
    }

    @Override
    public String toString() {
        return String.format("%s\t%.1f\t%.1f", stuKey, totalScore, avgScore);
    }

    public List<Integer> getScoreList() {
        return scoreList;
    }

    public void setScoreList(List<Integer> scoreList) {
        this.scoreList = scoreList;
        setAvgScore();
        setTotalScore();
    }

    private void setAvgScore() {
        int size = scoreList.size();
        this.avgScore = getTotalScore() / size;
    }

    private void setTotalScore() {
        this.totalScore = getTotalScore();
    }

    private double getTotalScore() {
        int size = scoreList.size();
        if (size == 0) {
            return 0;
        } else {
            double total = 0;
            for (int i = 0; i < size; i++) {
                total += scoreList.get(i);
            }
            return total;
        }
    }

    public String getStuKey() {
        return stuKey;
    }

    public void setStuKey(String stuKey) {
        this.stuKey = stuKey;
    }
}
