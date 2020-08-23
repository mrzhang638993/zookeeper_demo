package cn.itcast.zookeeper_api.mybatis;

public class TestMain {

    public static void main(String[] args) {
        MyStudent myStudent=new MyStudent("zhangsan",20,30.0);
        System.out.println(myStudent.getName()+"-----"+myStudent.getAge()+"-----"+myStudent.getSalary());
    }
}
