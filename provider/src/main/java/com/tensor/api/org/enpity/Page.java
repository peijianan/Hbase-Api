package com.tensor.api.org.enpity;

import lombok.Data;

import java.util.ArrayList;

/**
 * @author liaochuntao
 */
@Data
public class Page<T> {

    private T data = (T) new ArrayList(0);
    private int total;

}
