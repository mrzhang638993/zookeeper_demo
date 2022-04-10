package com.itheima;

import org.apache.flink.streaming.api.functions.source.RichSourceFunction;

//对应的必须是要相关的定时任务的实现操作的。
public class MySource extends RichSourceFunction<String> {

    @Override
    public void run(SourceContext<String> ctx) throws Exception {
        //定时器是在处理元素的时候得到的相关的time的上下文的。source中是不可能存在timer上下文的东西的
        ctx.collect();
    }

    @Override
    public void cancel() {

    }
}
