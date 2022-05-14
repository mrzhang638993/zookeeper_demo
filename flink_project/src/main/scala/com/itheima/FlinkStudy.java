package com.itheima;

import org.apache.flink.api.java.tuple.Tuple2;

/**
 * flink 学习初步探索操作
 * */
public class FlinkStudy {
    public static void main(String[] args) {
        Tuple2<String,Integer> person=Tuple2.of("Fred",35);
        System.out.println(person.f0);
        System.out.println(person.f1);
    }
}
