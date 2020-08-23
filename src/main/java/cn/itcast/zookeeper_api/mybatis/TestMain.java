package cn.itcast.zookeeper_api.mybatis;



public class TestMain {

      /**
       * 泛型方法
       * */
     public  static <T extends  Person> void testGerernic(T t){
            System.out.println(t);
     }

    public static void main(String[] args) {
        MyStudent myStudent=new MyStudent("zhangsan",20,30.0);
        System.out.println(myStudent.getName()+"-----"+myStudent.getAge()+"-----"+myStudent.getSalary());
        testGerernic(new MyStudent<>("zhangsan",20,30));
     }
}
