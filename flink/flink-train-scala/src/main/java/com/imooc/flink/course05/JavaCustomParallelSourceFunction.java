package com.imooc.flink.course05;

import org.apache.flink.streaming.api.functions.source.ParallelSourceFunction;

public class JavaCustomParallelSourceFunction implements ParallelSourceFunction<Long> {
    boolean isRunning = true;
    long count = 1;
    @Override
    public void run(SourceContext<Long> ctx) throws Exception {
        while (isRunning) {
            ctx.collect(count);
            count ++;
            Thread.sleep(1000);
        }
    }

    @Override
    public void cancel() {
        isRunning = false;
    }
}
