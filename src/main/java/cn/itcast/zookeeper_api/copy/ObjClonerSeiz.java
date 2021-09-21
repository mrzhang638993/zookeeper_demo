package cn.itcast.zookeeper_api.copy;

import java.io.*;

public class ObjClonerSeiz {

    private static <T> T CloneObj(T obj) {
        T retobj = null;
        try {
            //写入流中
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ObjectOutputStream oos = new ObjectOutputStream(baos);
            oos.writeObject(obj);
            //从流中读取
            ObjectInputStream ios = new ObjectInputStream(new ByteArrayInputStream(baos.toByteArray()));
            retobj = (T) ios.readObject();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return retobj;
    }


    /**
     * 对象的属性值全部拷贝成功，并且开辟的都是不同的内存空间的。对应的是深拷贝的机制的。
     */
    public static void main(String[] args) {
        Body body = new Body("张三", new Fonter(), new Head());
        Body body2 = CloneObj(body);
        System.out.println(body);
        System.out.println(body2);
        System.out.println("body==body2  ====>" + (body == body2));
        System.out.println(body.getFonter().toString());
        System.out.println(body2.getFonter().toString());
        System.out.println("body.font==body2.font  ====>" + (body.getFonter() == body2.getFonter()));
        System.out.println(body.getHead().toString());
        System.out.println(body2.getHead().toString());
        System.out.println("body.head==body2.head  ====>" + (body.getHead() == body2.getHead()));
    }
}

/**
 * @author DGW-PC
 * @date 2018年6月7日
 * @since 串行化 实现 深拷贝
 */

class Body implements Serializable {
    /**
     *
     */
    private static final long serialVersionUID = 1L;
    private String name;
    private Fonter fonter;
    private Head head;

    public Body(String name, Fonter fonter, Head head) {
        super();
        this.name = name;
        this.fonter = fonter;
        this.head = head;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Fonter getFonter() {
        return fonter;
    }

    public void setFonter(Fonter fonter) {
        this.fonter = fonter;
    }

    public Head getHead() {
        return head;
    }

    public void setHead(Head head) {
        this.head = head;
    }

    @Override
    public String toString() {
        return "Body [name=" + name + ", fonter=" + fonter + ", head=" + head + "]";
    }


}

class Head implements Serializable {
    /**
     *
     */
    private static final long serialVersionUID = 1L;
    private Integer size = 4;

    @Override
    public String toString() {
        return "Head{" +
                "size=" + size +
                '}';
    }
}

class Fonter implements Serializable {
    /**
     *
     */
    private static final long serialVersionUID = 1L;
    private Integer size = 2;

    @Override
    public String toString() {
        return "Fonter{" +
                "size=" + size +
                '}';
    }
}

class Face implements Serializable {
    /**
     *
     */
    private static final long serialVersionUID = 1L;
    private Integer size = 3;

    @Override
    public String toString() {
        return "Face{" +
                "size=" + size +
                '}';
    }
}
