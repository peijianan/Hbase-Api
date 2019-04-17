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
	public void setId(long id) {
		this.id = id;
	}
	public String getNewTitle() {
		return newTitle;
	}
	public void setNewTitle(String newTitle) {
		this.newTitle = newTitle;
	}
	public String getNewType() {
		return newType;
	}
	public void setNewType(String newType) {
		this.newType = newType;
	}
	public String getAuthor() {
		return author;
	}
	public void setAuthor(String author) {
		this.author = author;
	}
	public String getText() {
		return text;
	}
	public void setText(String text) {
		this.text = text;
	}
	public String getHashCode() {
		return hashCode;
	}
	public void setHashCode(String hashCode) {
		this.hashCode = hashCode;
	}

    

}
