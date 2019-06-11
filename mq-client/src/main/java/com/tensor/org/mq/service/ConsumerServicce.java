package com.tensor.org.mq.service;

import java.util.List;

/**
 * @author <a href="mailto:liaochunyhm@live.com">liaochuntao</a>
 * @since
 */
public interface ConsumerServicce {

    int onNext();

    void onEvent(List<String> list);

    String topic();

}
