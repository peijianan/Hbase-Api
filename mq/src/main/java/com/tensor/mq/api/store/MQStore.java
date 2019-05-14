package com.tensor.mq.api.store;

import com.lmax.disruptor.EventFactory;
import com.lmax.disruptor.YieldingWaitStrategy;
import com.lmax.disruptor.dsl.Disruptor;
import com.lmax.disruptor.dsl.ProducerType;
import com.tensor.mq.api.handler.ConsumerService;
import com.tensor.mq.api.pojo.Message;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Supplier;

/**
 * @author liaochuntao
 */
@Slf4j
@Scope(value = ConfigurableBeanFactory.SCOPE_SINGLETON)
@Component
public class MQStore {

    private static final int MIN_POO_SIE = 2;
    private static final long KEEP_ALIVE_TIME = 10;
    private static final TimeUnit UNIT = TimeUnit.SECONDS;
    private ConcurrentHashMap<String, ConsumerService> services = new ConcurrentHashMap<>();

    private static ThreadFactory MQ_Thread_Factory = new ThreadFactory() {
        private final String namePrefix = "HBase_MQ_THREAD-";
        private final AtomicInteger nextId = new AtomicInteger(1);

        @Override
        public Thread newThread(Runnable r) {
            String name = namePrefix + nextId.getAndDecrement();
            return new Thread(r, name);
        }
    };

    private RejectedExecutionHandler rejectedExecutionHandler =
            (r, executor) -> log.error("{} Hbase任务被拒绝。 {}", r.toString(), executor.toString());

    private final ThreadPoolExecutor MQ = new ThreadPoolExecutor(MIN_POO_SIE, MIN_POO_SIE, KEEP_ALIVE_TIME, UNIT,
            new ArrayBlockingQueue<>(1000), MQ_Thread_Factory, rejectedExecutionHandler);

    private final ConcurrentHashMap<String, Disruptor<Message>> tensorMQ = new ConcurrentHashMap<>(16);

    private Disruptor<Message> disruptor(int ringBufferSize) {
        EventFactory<Message> factory = Message::new;
        return new Disruptor<>(factory, ringBufferSize, MQ, ProducerType.SINGLE, new YieldingWaitStrategy());
    }

    public boolean isExist(String topic) {
        return services.containsKey(topic);
    }

    public void register(String topic, Supplier<ConsumerService> supplier) {
        if (tensorMQ.containsKey(topic)) {
            throw new IllegalArgumentException(String.format("Topic-[%s] 已存在，不可重复注册[Topic]", topic));
        }
        int ringBufferSize = 1024 * 1024;
        ConsumerService service = services.computeIfAbsent(topic, s -> supplier.get());
        Disruptor<Message> disruptor = disruptor(ringBufferSize);
        disruptor.handleEventsWithWorkerPool(service);
        disruptor.start();
        tensorMQ.put(topic, disruptor);
    }

    public void dregister(String topic) {
        services.remove(topic);
        tensorMQ.remove(topic);
    }

    public void publish(Message message) {
        tensorMQ.get(message.getTopic()).publishEvent(((event, sequence) -> Message.adaper(sequence, event, message)));
    }

    public ConsumerService getService(String topic) {
        return services.get(topic);
    }

}
