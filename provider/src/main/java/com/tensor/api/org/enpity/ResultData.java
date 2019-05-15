package com.tensor.api.org.enpity;

import com.tensor.api.org.util.ResultCode;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import org.springframework.http.HttpStatus;

import java.util.HashMap;

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

    public boolean isSuccess() {
        return this.code == 200;
    }

    public void buildFromResultCode(ResultCode code) {
        this.code = code.getCode();
        this.msg = code.getDesc();
    }

    public static <T> ResultData<T> buildSuccessFromData(T data) {
        return (ResultData<T>) ResultData
                .builder()
                .code(HttpStatus.OK.value())
                .data(data)
                .msg(HttpStatus.OK.getReasonPhrase())
                .build();
    }

    public static <T> ResultData<T> buildErrorFromData(Exception data) {
        return (ResultData<T>) ResultData
                .builder()
                .code(HttpStatus.INTERNAL_SERVER_ERROR.value())
                .data(false)
                .msg(data.getLocalizedMessage())
                .build();
    }
}
