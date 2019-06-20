package com.dinamsky.BotRest.entity;

import java.net.URL;
import java.time.LocalDateTime;

public class RssBean {
    private int id;

    private String title;
    private String description;
    private URL url;

    private LocalDateTime newsDate;

    public String getTitle() {
        return title;
    }
    public void setTitle(String title) {
        this.title = title;
    }
    public String getDescription() {
        return description;
    }
    public void setDescription(String description) {
        this.description = description;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public LocalDateTime getNewsDate() {
        return newsDate;
    }

    public void setNewsDate(LocalDateTime newsDate) {
        this.newsDate = newsDate;
    }

    public URL getUrl() {
        return url;
    }

    public void setUrl(URL url) {
        this.url = url;
    }

    @Override
    public String toString() {
        return title + '\''  + description + '\'' + newsDate ;
    }
}
