package com.deepayan.model;


import jakarta.json.bind.annotation.JsonbTransient;
import jakarta.xml.bind.annotation.XmlRootElement;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@XmlRootElement
public class Message {
    private Long id;
    private String message;
    private String author;
    private Date created;
    private List<Link> links = new ArrayList<>();
    @JsonbTransient
    private List<Comment> commentList = new ArrayList<>();


    public Message() {
    }

    public Message(Long id, String message, String author) {
        this.id = id;
        this.message = message;
        this.author = author;
        this.created = new Date();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
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

    public List<Comment> getCommentList() {
        return commentList;
    }

    public void setCommentList(List<Comment> commentList) {
        this.commentList = commentList;
    }

    public List<Link> getLinks() {
        return links;
    }

    public void setLinks(List<Link> links) {
        this.links = links;
    }

    public void addLink(String rel, String href) {
        Link link = new Link();
        link.setRel(rel);
        link.setHref(href);
        this.links.add(link);
    }

    @Override
    public String toString() {
        return "Message {" +
                "id=" + id +
                ", message='" + message + '\'' +
                ", author='" + author + '\'' +
                ", created=" + created +
                '}';
    }
}
