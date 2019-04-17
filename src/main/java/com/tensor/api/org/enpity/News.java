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


    public long getId() {
        return id;
    }

    public String getAuthor() {
        return author;
    }

    public String getNewTitle() {
        return newTitle;
    }

    public String getNewType() {
        return newType;
    }

    public String getText() {
        return text;
    }

    public void setId(long id) {
        this.id = id;
    }
}
