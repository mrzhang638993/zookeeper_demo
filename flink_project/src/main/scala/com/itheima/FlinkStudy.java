package com.itheima;

import org.apache.flink.api.common.functions.FilterFunction;
import org.apache.flink.api.java.tuple.Tuple2;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;

import java.util.ArrayList;
import java.util.List;

/**
 * flink 学习初步探索操作
 * */
public class FlinkStudy {
    public static void main(String[] args) throws Exception {
        /*Tuple2<String,Integer> person=Tuple2.of("Fred",35);
        System.out.println(person.f0);
        System.out.println(person.f1);*/
        //flink执行POJO类型的序列化操作
        //Person person = new Person("Fred Flintstone", 35);
        final StreamExecutionEnvironment env =
                StreamExecutionEnvironment.getExecutionEnvironment();
        List<Person> people = new ArrayList<Person>();
        people.add(new Person("Fred", 35));
        people.add(new Person("Wilma", 35));
        people.add(new Person("Pebbles", 2));
        //使用fromCollection完成对应的DataStream的构造的。
        DataStream<Person> flintstones = env.fromCollection(people);
        //使用fromElements对应的完成元素的构造操作实现的。
        /*DataStream<Person> flintstones = env.fromElements(
                new Person("Fred", 35),
                new Person("Wilma", 35),
                new Person("Pebbles", 2));*/
        //对应的还可以使用socket来创建对应的DataStream的相关的知识的。
        //DataStream<String> lines = env.socketTextStream("localhost", 9999);
        //或者是使用textFile的方式实现创建DataStream的。
        //DataStream<String> lines = env.readTextFile("file:///path");
        DataStream<Person> adults = flintstones.filter(new FilterFunction<Person>() {
            @Override
            public boolean filter(Person person) throws Exception {
                return person.age >= 18;
            }
        });
        adults.print();
        //调用execute对应的会将DataStream转换成为一个job graph相关的内容。
        env.execute();
    }
}
class  Person{
    public String name;
    public Integer age;
    public Person() {}
    public Person(String name,Integer age){
        this.name=name;
        this.age=age;
    }

    @Override
    public String toString() {
        return "Person{" +
                "name='" + name + '\'' +
                ", age=" + age +
                '}';
    }
}