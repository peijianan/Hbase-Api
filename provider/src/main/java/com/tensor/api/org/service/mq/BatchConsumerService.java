package com.tensor.api.org.service.mq;

import java.util.List;

/**
 * @author liaochuntao
 */
public interface BatchConsumerService<T> {

    /**
     *
     * @param data
     */
    void onEvent(List<T> data);

}
