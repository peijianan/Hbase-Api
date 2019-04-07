package com.tensor.api.org.config.schedule;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

@Slf4j
public final class Schedule {

    private static final int CORE_POOL_SIZE = 10;
    private static final int MAX_POOL_SIZE = 100;
    private static final long KEEP_ALIVE_TIME = 10;
    private static final TimeUnit UNIT = TimeUnit.SECONDS;

    private static ThreadFactory dbThreadFactory = new ThreadFactory() {
        private final String namePrefix = "HBASE_DB_THREAD-";
        private final AtomicInteger nextId = new AtomicInteger(1);

        @Override
        public Thread newThread(Runnable r) {
            String name = namePrefix + nextId.getAndDecrement();
            return new Thread(r, name);
        }
    };

    private static RejectedExecutionHandler rejectedExecutionHandler =
            (r, executor) -> log.error("{} Hbase任务被拒绝。 {}", r.toString(), executor.toString());

    public static final ThreadPoolExecutor DB = new ThreadPoolExecutor(CORE_POOL_SIZE, MAX_POOL_SIZE, KEEP_ALIVE_TIME, UNIT,
            new ArrayBlockingQueue<>(1000), dbThreadFactory, rejectedExecutionHandler);

}