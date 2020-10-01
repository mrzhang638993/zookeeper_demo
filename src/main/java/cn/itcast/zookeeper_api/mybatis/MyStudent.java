package cn.itcast.zookeeper_api.mybatis;

public class MyStudent<T,S,W> extends Person {
    private  T  name;
    private  S age;
    private  W salary;

    public MyStudent(T name, S age,W salary) {
        this.name = name;
        this.age = age;
        this.salary=salary;
    }


    public T getName() {
        return name;
    }

    public void setName(T name) {
        this.name = name;
    }

    public S getAge() {
        return age;
    }

    public void setAge(S age) {
        this.age = age;
    }

    public W getSalary() {
        return salary;
    }

    public void setSalary(W salary) {
        this.salary = salary;
    }

    @Override
    public String toString() {
        return "MyStudent{" +
                "name=" + name +
                ", age=" + age +
                ", salary=" + salary +
                '}';
    }
}
