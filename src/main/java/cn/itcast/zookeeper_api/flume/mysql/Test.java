package cn.itcast.zookeeper_api.flume.mysql;

import org.apache.flume.Context;
import org.apache.flume.EventDeliveryException;

public class Test {

    public static void main(String[] args) throws EventDeliveryException {
        MysqlSource mysqlSource = new MysqlSource();
        mysqlSource.process();
    }
}
