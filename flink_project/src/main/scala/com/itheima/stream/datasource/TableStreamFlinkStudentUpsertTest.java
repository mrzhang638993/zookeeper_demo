package com.itheima.stream.datasource;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.datastream.DataStreamSource;
import org.apache.flink.streaming.api.datastream.SingleOutputStreamOperator;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.table.api.Table;
import org.apache.flink.table.api.java.StreamTableEnvironment;
import org.apache.flink.types.Row;


import java.util.HashMap;
import java.util.Map;

public class TableStreamFlinkStudentUpsertTest {
    public static void main(String[] args) throws Exception {
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
        StreamTableEnvironment tableEnvironment = StreamTableEnvironment.getTableEnvironment(env);
        /*Test test=new Test(1,"hello");
        Test test1=new Test(2,"world");
        Test test2=new Test(3,"many");
        DataStreamSource dataStreamSource = env.fromElements(test,test1,test2);
        Table id = tableEnvironment.fromDataStream(dataStreamSource);
        DataStream<Test> stringDataStream = tableEnvironment.toAppendStream(id, Test.class);
        stringDataStream.print();*/
      /*  DataStreamSource<String> source = env.fromElements("1\001hello\001test", "2\001hello\001test1", "3\001hello\001test2");
        DataStream<Test1> returns = source.map(it -> {
            String[] split = it.split("\001");
            Map<String, String> map = new HashMap<>();
            map.put("id", split[0]);
            map.put("value", split[1]);
            map.put("desc", split[2]);
            Test1 test1 = new Test1();
            BeanUtils.populate(test1, map);
            return test1;
           *//* Row  row=new Row(3);
            row.setField(0,split[0]);
            row.setField(1,split[1]);
            row.setField(2,split[2]);*//*
            //return row;
        }).returns(Test1.class);
        returns.print();
*/
        env.execute("test");
    }
}
