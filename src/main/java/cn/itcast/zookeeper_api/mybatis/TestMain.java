package cn.itcast.zookeeper_api.mybatis;

public class TestMain {

    /**
     * 泛型方法.上界
     */
    public static <T extends Person> void testGerernic(T t) {
        System.out.println(t);
    }

    /**
     * 下界操作.只能是Person以及Person的父类的。
     */
    /*public static <S super Person>  void  testUpper(S s ){
        System.out.println(s);
    }*/
    public static void main(String[] args) {
        MyStudent myStudent = new MyStudent("zhangsan", 20, 30.0);
        System.out.println(myStudent.getName() + "-----" + myStudent.getAge() + "-----" + myStudent.getSalary());
        testGerernic(new MyStudent<>("zhangsan", 20, 30));
    }
}
