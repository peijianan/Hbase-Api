package com.tensor.api.org.enpity;

import com.tensor.api.org.util.ResultCode;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

/**
 * @author liaochuntao
 */
@Data
@Builder
@AllArgsConstructor
public class ResultData<T> {

    private int code;
    private T data;
    private String msg;

    public ResultData() {
    }

    public void setData(T data) {
        this.data = data;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public void buildFromResultCode(ResultCode code) {
        this.code = code.getCode();
        this.msg = code.getDesc();
    }
}
