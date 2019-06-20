package com.dinamsky.BotRest.config;

import org.springframework.boot.context.properties.ConfigurationProperties;


public class Config {
    private  String id;
    private String token;
    private String username;

    public String getToken() {
        return token;
    }

    public String getUsername() {
        return username;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}

