package com.itheima;

import org.apache.flink.api.common.state.MapState;
import org.apache.flink.streaming.api.datastream.ConnectedStreams;
import org.apache.flink.streaming.api.datastream.DataStreamSource;
import org.apache.flink.streaming.api.environment.LocalStreamEnvironment;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.functions.co.CoMapFunction;

public class TestConnect {
    public static void main(String[] args) {
        LocalStreamEnvironment localEnvironment = StreamExecutionEnvironment.createLocalEnvironment();
        DataStreamSource<Integer> integerDataStreamSource = localEnvironment.fromElements(1, 0, 9, 2, 3, 6);
        DataStreamSource<String> stringDataStreamSource = localEnvironment.fromElements("LOW", "HIGH", "LOW", "LOW");
        ConnectedStreams<Integer, String> connectStream = integerDataStreamSource.connect(stringDataStreamSource);
        //双stream处理操作的时候对应的是需要共享状态进行操作实现的。这个状态的考虑需要进行评估的
        //处理的时候不保证接口的调用的顺序的，对应的是根据数据的顺序来实现相关的操作的。
        //对应的将stream1的数据和stream2的数据定时的存储到state中来保存的。
        //对应的是单个的数据流来执行操作的。
        connectStream.map(new CoMapFunction<Integer, String, Object>() {
            //在对应的计算中共享相关的state的数据信息的。
            MapState mapState=null;
            //处理第一个stream的数据
            @Override
            public Object map1(Integer value) throws Exception {
                return null;
            }

            //处理第二个stream的数据,这个数据可以作为共享的mapState的数据执行操作。
            @Override
            public Object map2(String value) throws Exception {
                return null;
            }
        });
    }
}
