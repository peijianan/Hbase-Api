package com.tensor.api.org.enpity.mq;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

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

}
