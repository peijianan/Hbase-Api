package com.tensor.org.mq.common;

import java.util.List;

/**
 * @author <a href="mailto:liaochunyhm@live.com">liaochuntao</a>
 * @since
 */
public class MQConsumer {

    private int opType;
    private String topic;
    private String id;
    private int reqN;
    private String ackN;
    private List<String> contexts;

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

    public int getReqN() {
        return reqN;
    }

    public void setReqN(int reqN) {
        this.reqN = reqN;
    }

    public String getAckN() {
        return ackN;
    }

    public void setAckN(String ackN) {
        this.ackN = ackN;
    }

    public List<String> getContexts() {
        return contexts;
    }

    public void setContexts(List<String> contexts) {
        this.contexts = contexts;
    }

    @Override
    public String toString() {
        return "com.tensor.org.mq.common.MQConsumer{" +
                "opType=" + opType +
                ", topic='" + topic + '\'' +
                ", id='" + id + '\'' +
                ", contexts=" + contexts +
                '}';
    }
}
