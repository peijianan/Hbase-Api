package com.tensor.mq.api.handler;

import com.lmax.disruptor.WorkHandler;
import com.tensor.mq.api.pojo.Message;


/**
 * @author liaochuntao
 */
public interface ConsumerService<T> extends WorkHandler<Message> {

    /**
     * 请求消息
     *
     * @param reqNum
     * @return
     */
    T onNext(int reqNum);

}
