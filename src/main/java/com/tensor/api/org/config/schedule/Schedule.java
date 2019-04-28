package com.tensor.api.org.config.schedule;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author liaochuntao
 */
@Slf4j
public final class Schedule {

    private static final int MIN_POO_SIE = 2;
    private static final int CORE_POOL_SIZE = 5;
    private static final int MAX_POOL_SIZE = 20;
    private static final long KEEP_ALIVE_TIME = 10;
    private static final TimeUnit UNIT = TimeUnit.SECONDS;

    private static ThreadFactory DB_Thread_Factory = new ThreadFactory() {
        private final String namePrefix = "HBASE_DB_THREAD-";
        private final AtomicInteger nextId = new AtomicInteger(1);

        @Override
        public Thread newThread(Runnable r) {
            String name = namePrefix + nextId.getAndDecrement();
            return new Thread(r, name);
        }
    };

    private static ThreadFactory MQ_Thread_Factory = new ThreadFactory() {
        private final String namePrefix = "HBASE_MQ_THREAD-";
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
            new ArrayBlockingQueue<>(1000), DB_Thread_Factory, rejectedExecutionHandler);

    public static final ThreadPoolExecutor MQ = new ThreadPoolExecutor(MIN_POO_SIE, MIN_POO_SIE, KEEP_ALIVE_TIME, UNIT,
            new ArrayBlockingQueue<>(1000), MQ_Thread_Factory, rejectedExecutionHandler);
}