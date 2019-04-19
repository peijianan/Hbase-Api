package com.tensor.api.org.enpity;

import lombok.Builder;
import lombok.Data;

/**
 * @author liaochuntao
 */
@Data
@Builder
public class News {

    private long id;
    private String newTitle;
    private String newType;
    private String author;
    private String text;
    private String hashCode;
        

}
