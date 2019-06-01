package com.tensor.org.mq.common;

/**
 * @author <a href="mailto:liaochunyhm@live.com">liaochuntao</a>
 * @since
 */
public class MQPublisher {

    private int opType;
    private String topic;
    private String id;
    private String context;

    public int getOpType() {
        return opType;
    }

    public void setOpType(int opType) {
        this.opType = opType;
    }

    public String getTopic() {
        return topic;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getContext() {
        return context;
    }

    public void setContext(String context) {
        this.context = context;
    }

    @Override
    public String toString() {
        return "com.tensor.org.mq.common.MQPublisher{" +
                "opType=" + opType +
                ", topic='" + topic + '\'' +
                ", id='" + id + '\'' +
                ", context='" + context + '\'' +
                '}';
    }
}
