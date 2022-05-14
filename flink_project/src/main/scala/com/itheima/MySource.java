package com.itheima;

import org.apache.flink.streaming.api.functions.source.RichSourceFunction;

//对应的必须是要相关的定时任务的实现操作的。
public class MySource extends RichSourceFunction<String> {

    @Override
    public void run(SourceContext<String> ctx) throws Exception {
    }

    @Override
    public void cancel() {

    }
}
