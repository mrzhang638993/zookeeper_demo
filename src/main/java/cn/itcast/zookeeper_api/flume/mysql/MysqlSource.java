package cn.itcast.zookeeper_api.flume.mysql;

import org.apache.flume.Context;
import org.apache.flume.Event;
import org.apache.flume.EventDeliveryException;
import org.apache.flume.PollableSource;
import org.apache.flume.conf.Configurable;
import org.apache.flume.event.SimpleEvent;
import org.apache.flume.source.AbstractSource;
import org.slf4j.Logger;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static org.slf4j.LoggerFactory.*;

/**
 *  主题的source的业务逻辑
 * */
public class MysqlSource  extends AbstractSource implements Configurable, PollableSource {

    private  QueryMysql queryMysql;
    //打印日志
    private static final Logger LOG = getLogger(MysqlSource.class);
    /**
     *  主要需要重写的业务逻辑
     *  查询数据库记录写入到channel中
     * */
    @Override
    public Status process() throws EventDeliveryException {
        try {
            //查询数据表
            List<List<Object>> result = queryMysql.executeQuery();
            //存放event的集合
            List<Event> events = new ArrayList<>();
            //存放event头集合
            HashMap<String, String> header = new HashMap<>();
            //如果有返回数据，则将数据封装为event
            if (!result.isEmpty()) {
                List<String> allRows = queryMysql.getAllRows(result);
                Event event = null;
                for (String row : allRows) {
                    event = new SimpleEvent();
                    event.setBody(row.getBytes());
                    event.setHeaders(header);
                    events.add(event);
                }
                //将event写入channel
                this.getChannelProcessor().processEventBatch(events);
                //更新数据表中的offset信息
                queryMysql.updateOffset2DB(result.size());
            }
            //等待时长
            Thread.sleep(queryMysql.getRunQueryDelay());
            return Status.READY;
        } catch (InterruptedException e) {
            LOG.error("Error procesing row", e);
            return Status.BACKOFF;
        }
    }

    @Override
    public long getBackOffSleepIncrement() {
        return 0;
    }

    @Override
    public long getMaxBackOffSleepInterval() {
        return 0;
    }

    /**
     *  主要的业务逻辑，对应的是初始化的方法
     * */
    @Override
    public void configure(Context context) {
       queryMysql=new QueryMysql(context);
    }
}
