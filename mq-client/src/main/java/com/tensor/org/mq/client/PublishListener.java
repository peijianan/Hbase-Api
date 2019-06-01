package com.tensor.org.mq.client;

/**
 * @author <a href="mailto:liaochunyhm@live.com">liaochuntao</a>
 * @since
 */
public interface PublishListener {

    void onEvent(PublishEvent publishEvent);

    String topic();

}
