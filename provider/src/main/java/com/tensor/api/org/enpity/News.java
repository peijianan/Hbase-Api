package com.tensor.api.org.enpity;

import com.tensor.api.org.annotation.Field;
import com.tensor.api.org.annotation.Table;
import com.tensor.api.org.util.DateUtils;
import com.tensor.api.org.util.IDUtils;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;


/**
 * @author liaochuntao
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "news")
public class News {

    @Id
    private Long id;
    @Field(name = "new_title", cluster = "cf1")
    private String newTitle;
    @Field(name = "new_type", cluster = "cf1")
    private String newType;
    @Field(name = "author", cluster = "cf1")
    private String author;
    @Field(name = "text", cluster = "cf1")
    private String text;
    @Field(name = "publish_date", cluster = "cf1")
    private String publishDate = DateUtils.now();
    @Field(name = "download_date", cluster = "cf1")
    private String downloadDate = DateUtils.now();
    @Field(name = "url", cluster = "cf1")
    private String url;
    @Field(name = "source", cluster = "cf1")
    private String source;
    @Field(name = "actual_url", cluster = "cf1")
    private String actualUrl;
    @Field(name = "hash_code", cluster = "cf2")
    private String hashCode;

}
