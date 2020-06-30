package com.tensor.org.mq.common;

/**
 * @author <a href="mailto:liaochunyhm@live.com">liaochuntao</a>
 * @since
 */
public class MQResponse<T> {

    private int code;
    private int opType;
    private String topic;
    private T data;
    private String errMsg;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

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

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public String getErrMsg() {
        return errMsg;
    }

    public void setErrMsg(String errMsg) {
        this.errMsg = errMsg;
    }

    public static <T> MQResponse<T> successResponse(int code, int opType, T msg, String topic) {
        MQResponse mqResponse = new MQResponse();
        mqResponse.code = code;
        mqResponse.opType = opType;
        mqResponse.data = msg;
        mqResponse.topic = topic;
        mqResponse.errMsg = "";
        return mqResponse;
    }

    public static MQResponse errResponse(int code, int opType, String errMsg, String topic) {
        MQResponse mqResponse = new MQResponse();
        mqResponse.code = code;
        mqResponse.opType = opType;
        mqResponse.topic = topic;
        mqResponse.errMsg = errMsg;
        mqResponse.data = "";
        return mqResponse;
    }
}
