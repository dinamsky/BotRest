package com.dinamsky.BotRest.entity;

import org.springframework.format.annotation.DateTimeFormat;


import java.net.URL;
import java.time.LocalDateTime;



public class MessageBean {




    private int id;


    private String user_id;

    private String username;

    private String chat_id;

    private String text;

//    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm")
//    private LocalDateTime newsDate;

    public String getUser_id() {
        return user_id;
    }
    public String getUsername() {
        return username;
    }
    public String getText() {
        return text;
    }
    public void setUsername(String username) {
        this.username = username;
    }
    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }
    public void setText(String text) {
        this.text = text;
    }
    public String getChat_id() {
        return chat_id;
    }
    public void setChat_id(String description) {
        this.chat_id = chat_id;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

//    public LocalDateTime getNewsDate() {
//        return newsDate;
//    }

//    public void setNewsDate(LocalDateTime newsDate) {
//        this.newsDate = newsDate;
//    }

//    public URL getUrl() {
//        return url;
//    }

//    public void setUrl(URL url) {
//        this.url = url;
//    }

//    public int getRating() {
//        return rating;
//    }

//    public void setRating(int rating) {
//        this.rating = rating;
//    }

//    public String getSource() {
//        return source;
//    }
//
//    public void setSource(String source) {
//        this.source = source;
//    }

    @Override
    public String toString() {
        return username +
                "\n"+text ;
    }
}
