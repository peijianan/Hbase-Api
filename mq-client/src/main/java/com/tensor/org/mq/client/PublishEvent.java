package com.tensor.org.mq.client;

import com.tensor.org.mq.common.MQResponse;

/**
 * @author <a href="mailto:liaochunyhm@live.com">liaochuntao</a>
 * @since
 */
public class PublishEvent {

    private MQResponse response;

    public PublishEvent(MQResponse response) {
        this.response = response;
    }

    public MQResponse response() {
        return response;
    }
}
