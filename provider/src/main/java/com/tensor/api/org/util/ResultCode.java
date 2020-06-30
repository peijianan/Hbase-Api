package com.tensor.api.org.util;

/**
 * @author liaochuntao
 */

public enum ResultCode {
    /**
     *
     */
    STORAGE_FAILURE(1000, "HBase存储失败")
    ;

    private int code;
    private String desc;

    ResultCode(int code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public int getCode() {
        return code;
    }

    public String getDesc() {
        return desc;
    }

    @Override
    public String toString() {
        return "ResultCode{" +
                "code=" + code +
                ", desc='" + desc + '\'' +
                '}';
    }
}
