package com.tensor.api.org.enpity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author liaochuntao
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class News {

    private Long id;
    private String newTitle;
    private String newType;
    private String author;
    private String text;
    private String publishDate;
    private String url;
    private String source;
    private String hashCode;

}
