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
    private T data;
    private long publishTime;

    public static void adaper(long sequence, Message var1, Message var2) {
        var1.data = var2.data;
        var1.sequence = sequence;
        var1.publishTime = var2.publishTime;
    }

}
