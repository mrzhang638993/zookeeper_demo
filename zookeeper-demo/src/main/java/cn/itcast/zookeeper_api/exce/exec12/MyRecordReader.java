package cn.itcast.zookeeper_api.exce.exec12;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FSDataInputStream;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.mapreduce.InputSplit;
import org.apache.hadoop.mapreduce.RecordReader;
import org.apache.hadoop.mapreduce.TaskAttemptContext;
import org.apache.hadoop.mapreduce.lib.input.FileSplit;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class MyRecordReader extends RecordReader<ScoreWritable, NullWritable> {

    private Configuration conf = null;
    private FileSplit fileSplit = null;
    private FileSystem fs = null;
    private FSDataInputStream open = null;
    private BufferedReader br = null;
    private ScoreWritable scoreWritable = null;

    @Override
    public void initialize(InputSplit inputSplit, TaskAttemptContext taskAttemptContext) throws IOException, InterruptedException {
        conf = taskAttemptContext.getConfiguration();
        fileSplit = (FileSplit) inputSplit;
        fs = FileSystem.newInstance(conf);
        Path path = fileSplit.getPath();
        //  获取问价输入流
        open = fs.open(path);
        br = new BufferedReader(new InputStreamReader(open));
    }

    //  完成每一行的key以及value的封装操作实现
    @Override
    public boolean nextKeyValue() throws IOException, InterruptedException {
        String content = br.readLine();
        if (content != null) {
            String[] split = content.split(" ");
            scoreWritable = new ScoreWritable();
            scoreWritable.setId(split[0]);
            scoreWritable.setName(split[1]);
            scoreWritable.setYuwenScore(Integer.valueOf(split[2]));
            scoreWritable.setShuxueScore(Integer.parseInt(split[3]));
            scoreWritable.setEnglishScore(Integer.parseInt(split[4]));
            scoreWritable.setWuliScore(Integer.parseInt(split[5]));
            scoreWritable.setHuaxueScore(Integer.parseInt(split[6]));
            int totalScore = Integer.valueOf(split[2])
                    + Integer.parseInt(split[3])
                    + Integer.parseInt(split[4])
                    + Integer.parseInt(split[5])
                    + Integer.parseInt(split[6]);
            double avgScore = totalScore / 5;
            scoreWritable.setTotalScore(totalScore);
            scoreWritable.setAvgScore(avgScore);
            return true;
        } else {
            return false;
        }
    }

    @Override
    public ScoreWritable getCurrentKey() throws IOException, InterruptedException {
        return scoreWritable;
    }

    @Override
    public NullWritable getCurrentValue() throws IOException, InterruptedException {
        return NullWritable.get();
    }

    @Override
    public float getProgress() throws IOException, InterruptedException {
        return 0;
    }

    @Override
    public void close() throws IOException {
        if (br != null) {
            br.close();
        }
        if (open != null) {
            open.close();
        }
        if (fs != null) {
            fs.close();
        }
    }
}
