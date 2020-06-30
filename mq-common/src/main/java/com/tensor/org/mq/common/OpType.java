package com.tensor.org.mq.common;

/**
 * @author <a href="mailto:liaochunyhm@live.com">liaochuntao</a>
 * @since
 */
public enum OpType {

    /**
     *
     */
    SUBSCRIBE_CREATE(0),

    /**
     *
     */
    SUBSCRIBE_PUBLISH(1),

    /**
     *
     */
    SUBSCRIBE_DELETE(2),

    /**
     *
     */
    CONSUMER_SUBSCRIBE(0),

    /**
     *
     */
    CONSUMER_REQUEST(1),

    /**
     *
     */
    CONSUMER_UN_SCBSCRIBE(2),

    /**
     *
     */
    CONSUMER_ACK(3),
    ;

    int type;

    OpType(int type) {
        this.type = type;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }
}
