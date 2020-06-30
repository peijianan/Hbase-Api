package com.tensor.api.org.service.mq.impl;

import com.tensor.api.org.config.schedule.Schedule;
import com.tensor.api.org.enpity.mq.Message;
import com.tensor.api.org.service.mq.BatchConsumerService;
import com.tensor.api.org.service.mq.ConsumerService;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentLinkedDeque;
import java.util.function.Supplier;

/**
 * @author liaochuntao
 */
@Slf4j
public class BatchConsumerProcessor implements ConsumerService {

    private final Supplier<Integer> request;
    private BatchConsumerService<Message> processor;
    private ConcurrentLinkedDeque<Message> cache = new ConcurrentLinkedDeque<>();
    private volatile boolean isStart = false;

    public BatchConsumerProcessor(int reqNum, BatchConsumerService processor) {
        this(processor, () -> reqNum);
    }

    /**
     *
     * @param processor {@link BatchConsumerService}
     * @param request {@link Supplier}
     */
    public BatchConsumerProcessor(BatchConsumerService processor, Supplier<Integer> request) {
        this.processor = processor;
        this.request = request;
        start();
    }

    @Override
    public void onEvent(Message event) throws Exception {
        if (!isStart) {
            synchronized (this) {
                isStart = true;
            }
        }
        cache.add(event);
    }

    private void start() {
        Schedule.Publisher.submit(new Publisher());
    }

    private class Publisher implements Runnable {

        @Override
        public void run() {
            try {
                while (!isStart) {
                    Thread.sleep(1000);
                }
                while (isStart) {
                    int n = request.get();
                    if (cache.isEmpty()) {
                        Thread.sleep(1000);
                    }
                    if (cache.size() < n) {
                        Thread.sleep(1000);
                    }
                    List<Message> messages = new ArrayList<>();
                    while (n >= 0 && cache.size() > 0) {
                        messages.add(cache.pollFirst());
                        n--;
                    }
                    if (!messages.isEmpty()) {
                        processor.onEvent(messages);
                    }
                }
            } catch (InterruptedException e) {
                log.error("[BatchConsumerProcessor err] : {}", e.getMessage());
            }
        }
    }
}
