package com.tensor.api.org.enpity;

import com.tensor.api.org.annotation.hbase.Field;
import com.tensor.api.org.annotation.hbase.Table;
import com.tensor.api.org.util.DateUtils;
import com.tensor.api.org.util.IDUtils;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;


/**
 * @author liaochuntao
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "news")
public class News {

    @Id
    private Long id = IDUtils.buildId();
    @Field(name = "new_title", cluster = "cf1")
    private String newTitle = "DEFAULT_TITLE";
    @Field(name = "new_type", cluster = "cf1")
    private String newType = "DEFAULT_TYPE";
    @Field(name = "author", cluster = "cf1")
    private String author = "DEFAULT_AUTHOR";
    @Field(name = "text", cluster = "cf1")
    private String text = "";
    @Field(name = "publish_date", cluster = "cf1")
    private String publishDate = DateUtils.now();
    @Field(name = "download_date", cluster = "cf1")
    private String downloadDate = DateUtils.now();
    @Field(name = "url", cluster = "cf1")
    private String url = "http://127.0.0.1:8080";
    @Field(name = "source", cluster = "cf1")
    private String source = "hdu";
    @Field(name = "actual_url", cluster = "cf1")
    private String actualUrl = "http://127.0.0.1:8080";
    @Field(name = "hash_code", cluster = "cf2")
    private String hashCode = "1234567890";

    public static Builder builder() {
        return new Builder();
    }

    public static final class Builder {
        private Long id = IDUtils.buildId();
        private String newTitle = "DEFAULT_TITLE";
        private String newType = "DEFAULT_TYPE";
        private String author = "DEFAULT_AUTHOR";
        private String text = "";
        private String publishDate = DateUtils.now();
        private String downloadDate = DateUtils.now();
        private String url = "http://127.0.0.1:8080";
        private String source = "hdu";
        private String actualUrl = "http://127.0.0.1:8080";
        private String hashCode = "1234567890";

        private Builder() {
        }

        public Builder id(Long id) {
            this.id = id;
            return this;
        }

        public Builder newTitle(String newTitle) {
            this.newTitle = newTitle;
            return this;
        }

        public Builder newType(String newType) {
            this.newType = newType;
            return this;
        }

        public Builder author(String author) {
            this.author = author;
            return this;
        }

        public Builder text(String text) {
            this.text = text;
            return this;
        }

        public Builder publishDate(String publishDate) {
            this.publishDate = publishDate;
            return this;
        }

        public Builder downloadDate(String downloadDate) {
            this.downloadDate = downloadDate;
            return this;
        }

        public Builder url(String url) {
            this.url = url;
            return this;
        }

        public Builder source(String source) {
            this.source = source;
            return this;
        }

        public Builder actualUrl(String actualUrl) {
            this.actualUrl = actualUrl;
            return this;
        }

        public Builder hashCode(String hashCode) {
            this.hashCode = hashCode;
            return this;
        }

        public News build() {
            News news = new News();
            news.setId(id);
            news.setNewTitle(newTitle);
            news.setNewType(newType);
            news.setAuthor(author);
            news.setText(text);
            news.setPublishDate(publishDate);
            news.setDownloadDate(downloadDate);
            news.setUrl(url);
            news.setSource(source);
            news.setActualUrl(actualUrl);
            news.setHashCode(hashCode);
            return news;
        }
    }
}
