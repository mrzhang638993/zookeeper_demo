package cn.itcast.zookeeper_api.functioninterface;


import com.alibaba.fastjson.JSON;

import java.util.*;

public class Test {

    public static void main(String[] args) {
        //  使用函数式编程的思想和函数式编程的逻辑执行代码的编写和操作实现
        MyFunction myFunction=()->{System.out.println(123);};
        //  构造函数式对象，从而创建和使用函数式的接口代码操作
        myFunction.test();

        String[] att=new String[]{"test","good","baby"};
        att[0]="good";
        System.out.println(JSON.toJSONString(att));
        List list=new ArrayList<>();
    }
}
