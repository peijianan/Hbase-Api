package com.tensor.api.org.service.mq.impl;

import com.tensor.api.org.enpity.mq.Message;
import com.tensor.api.org.service.mq.ConsumerService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class ConsumerServiceImpl implements ConsumerService {

    @Override
    public void onEvent(Message event) {
        log.info("Event {}", event);
    }
}
