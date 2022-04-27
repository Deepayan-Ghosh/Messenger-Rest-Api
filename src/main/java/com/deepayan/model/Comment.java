package com.deepayan.model;

import java.util.Date;

public class Comment {
    private Long id;
    private String comment;
    private String author;
    private Date created;

    public Comment() {
    }

    public Comment(Long id, String comment, String author) {
        this.id = id;
        this.comment = comment;
        this.author = author;
        this.created = new Date();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public Date getCreated() {
        return created;
    }

    public void setCreated(Date created) {
        this.created = created;
    }
}
