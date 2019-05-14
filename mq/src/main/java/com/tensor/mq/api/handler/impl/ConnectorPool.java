package com.tensor.mq.api.handler.impl;

import com.tensor.mq.api.handler.ConsumerService;
import com.tensor.mq.api.store.MQStore;
import com.tensor.mq.api.util.IDUtils;
import io.netty.channel.Channel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.function.Supplier;

/**
 * @author liaochuntao
 */
@Scope(value = ConfigurableBeanFactory.SCOPE_SINGLETON)
@Component
public class ConnectorPool {

    @Autowired
    private MQStore mqStore;

    private ConcurrentHashMap<RoleType, ConcurrentHashMap<String, Client>> connectorPool = new ConcurrentHashMap<>();
    private ConcurrentHashMap<String, Set<String>> index = new ConcurrentHashMap<>();
    private final ReadWriteLock lock = new ReentrantReadWriteLock();
    private final Lock readLock = lock.readLock();
    private final Lock writeLock = lock.writeLock();

    private final Supplier<ConsumerService> supplier = () -> new BatchInnerConsumerServiceImpl(new InnerConsumerServiceImpl());

    public ConnectorPool() {
        connectorPool.put(RoleType.CONSUMER, new ConcurrentHashMap<>());
        connectorPool.put(RoleType.PROVIDER, new ConcurrentHashMap<>());
    }

    public String addConnector(RoleType type, String topic, Channel channel) {
        String id = channel.id().asLongText();
        Client client = Client.builder()
                .id(id)
                .topic(topic)
                .lastReqTime(System.currentTimeMillis())
                .session(channel)
                .type(type)
                .build();
        connectorPool.get(type).put(id, client);
        final Lock lock = writeLock;
        try {
            lock.lock();
            Set<String> members = index.computeIfAbsent(topic, (subscribeTopic) -> new HashSet<>());
            members.add(id);
            mqStore.register(topic, supplier);
        } finally {
            lock.unlock();
        }
        return id;
    }

    public Client getClient(RoleType type, String id) {
        return connectorPool.get(type).get(id);
    }

    public void removeConnector(RoleType type, String topic, String id) {
        connectorPool.get(type).remove(id);
        final Lock lock = writeLock;
        try {
            lock.lock();
            Set<String> members = index.getOrDefault(topic, new HashSet<>());
            if (!members.isEmpty()) {
                members.remove(id);
            }
            if (members.isEmpty()) {
                index.remove(topic);
                mqStore.dregister(topic);
            }
        } finally {
            lock.unlock();
        }
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Client {
        private String id;
        private RoleType type;
        private String topic;
        private long lastReqTime;
        private Channel session;
    }
}
