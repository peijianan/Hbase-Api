package com.tensor.api.org.enpity.mq;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author liaochuntao
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Message<T> {

    private long sequence;
    private String topic;
    private T data;
    private int retryCnt;
    private long publishTime;

    public static void adaper(long sequence, Message var1, Message var2) {
        var1.sequence = sequence;
        var1.topic = var2.topic;
        var1.data = var2.data;
        var1.publishTime = var2.publishTime;
        var1.retryCnt = var2.retryCnt;
    }

    public static <T> Message<T> buildMessage(String topic, T data) {
        return (Message<T>) Message.builder()
                .topic(topic)
                .data(data)
                .publishTime(System.currentTimeMillis())
                .retryCnt(0)
                .build();
    }

}
