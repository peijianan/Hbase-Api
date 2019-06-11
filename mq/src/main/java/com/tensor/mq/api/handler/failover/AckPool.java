package com.tensor.mq.api.handler.failover;

import com.tensor.mq.api.pojo.Message;
import com.tensor.mq.api.store.MQStore;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;

/**
 * @author <a href="mailto:liaochunyhm@live.com">liaochuntao</a>
 * @since
 */
@Slf4j
@Scope(value = ConfigurableBeanFactory.SCOPE_SINGLETON)
@Component
public class AckPool {

    private static final int MAX_RETRY_CNT = 3;

    private ScheduledExecutorService executorService = Executors.newSingleThreadScheduledExecutor(new ThreadFactory() {
        @Override
        public Thread newThread(Runnable r) {
            Thread thread = new Thread(r);
            thread.setDaemon(true);
            thread.setName("[MQ-ACK-Thread]");
            return thread;
        }
    });

    @Autowired
    private MQStore mqStore;

    private final ConcurrentHashMap<String, List<Message>> WAIT_ACK_POOL = new ConcurrentHashMap<>();

    /**
     * 开启定时任务处理没有被ack的数据，重新压回消息队列中
     */
    @PostConstruct
    public void init() {
        executorService.scheduleWithFixedDelay(new WaitCleanWork(mqStore), 30, 60, TimeUnit.SECONDS);
    }

    public void clientAck(String key) {
        WAIT_ACK_POOL.remove(key);
    }

    public void waitAck(String ackN, List<Message> messages) {
        WAIT_ACK_POOL.put(ackN, messages);
    }

    private class WaitCleanWork implements Runnable {

        final MQStore mqStore;

        public WaitCleanWork(MQStore mqStore) {
            this.mqStore = mqStore;
        }

        @Override
        public void run() {
            Iterator<Map.Entry<String, List<Message>>> iterator = WAIT_ACK_POOL.entrySet().iterator();
            while (iterator.hasNext()) {
                Map.Entry<String, List<Message>> entry = iterator.next();
                iterator.remove();
                for (Message message : entry.getValue()) {
                    message.setRetryCnt(message.getRetryCnt() + 1);
                    if (message.getRetryCnt() > MAX_RETRY_CNT) {
                        continue;
                    }
                    message.setPublishTime(System.currentTimeMillis());
                    mqStore.publish(message);
                }
            }
        }
    }

}
