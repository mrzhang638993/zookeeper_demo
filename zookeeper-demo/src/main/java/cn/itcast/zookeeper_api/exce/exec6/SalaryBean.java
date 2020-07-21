package cn.itcast.zookeeper_api.exce.exec6;

import org.apache.hadoop.io.WritableComparable;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

/**
 * 封装业务相关的bean
 */
public class SalaryBean implements WritableComparable<SalaryBean> {

    private String name;
    private int age;
    private int salary;

    @Override
    public int compareTo(SalaryBean o) {
        if (this.salary == o.salary) {
            return this.age - o.age;
        } else {
            return o.salary - this.salary;
        }
    }

    @Override
    public void write(DataOutput out) throws IOException {
        out.writeUTF(this.name);
        out.writeInt(this.age);
        out.writeInt(this.salary);
    }

    @Override
    public void readFields(DataInput in) throws IOException {
        this.name = in.readUTF();
        this.age = in.readInt();
        this.salary = in.readInt();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public int getSalary() {
        return salary;
    }

    public void setSalary(int salary) {
        this.salary = salary;
    }

    @Override
    public String toString() {
        return
                name +
                        "\t" + age +
                        "\t" + salary;
    }
}
