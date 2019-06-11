package com.tensor.org.mq;

import com.tensor.org.mq.client.MQClient;
import com.tensor.org.mq.client.MQClientPublisherHandler;
import com.tensor.org.mq.client.MqClientConsumerHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author <a href="mailto:liaochunyhm@live.com">liaochuntao</a>
 * @since
 */
@Configuration
@ConditionalOnClass
@EnableConfigurationProperties(TensorMQProperties.class)
public class TensorMQAutoConfigure {

    @Autowired
    private TensorMQProperties tensorMQProperties;

    @Autowired
    private MqClientConsumerHandler consumerrHandler;

    @Autowired
    private MQClientPublisherHandler publisherHandler;

    @Bean
    public MQClient mqClient() {
        return new MQClient(tensorMQProperties, consumerrHandler, publisherHandler);
    }

}
