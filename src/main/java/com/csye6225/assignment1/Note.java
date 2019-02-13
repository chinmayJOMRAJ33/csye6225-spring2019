package com.csye6225.assignment1;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;

@Entity
public class Note {

    @Id
//    @GeneratedValue(generator = "UUID")
//    @GenericGenerator(
//            name="UUID",
//            strategy = "org.hibernate.id.UUIDGenerator"
//    )
    @Column(name="Uid",updatable = false,nullable = false)
    private String id;
    private String content;
    private String title;
    @JsonIgnore
    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    @ManyToOne
    @JoinColumn(name="id",nullable =false)
    private User user;

    public String getId() {
        return id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getCreated_on() {
        return created_on;
    }

    public void setCreated_on(String created_on) {
        this.created_on = created_on;
    }

    public String getUpdated_on() {
        return updated_on;
    }

    public void setUpdated_on(String updated_on) {
        this.updated_on = updated_on;
    }

    public void setId(String id) {
        this.id = id;
    }

    private String created_on;
    private String updated_on;
    //private UUID uuid;



}