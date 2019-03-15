package com.tensor.api.org.enpity;

import lombok.Builder;
import lombok.Data;

/**
 * @author liaochuntao
 */
@Data
@Builder
public class ResultData<T> {

    private int code;
    private T data;
    private String msg;

}
