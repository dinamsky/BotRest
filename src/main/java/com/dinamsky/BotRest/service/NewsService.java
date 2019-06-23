package com.dinamsky.BotRest.service;

import com.dinamsky.BotRest.entity.NewsBot;
import com.dinamsky.BotRest.entity.RssBean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service
public class NewsService {

    @Autowired
    RestTemplate restTemplate;
    @Bean
    public RestTemplate restTemplate(RestTemplateBuilder builder) {
        return builder.build();
    }

    private static final Logger logger = LoggerFactory.getLogger(NewsService.class);
    static final String BASE_URL = "http://localhost:8080";
    static final String URL_ALL_NEWS = "/rest_allNews";
    static final String URL_FIND_NEWS = "/rest_findNews/";
    static final String URL_RATING ="/rest_newsByRating/";
    static final String URL_BY_SOURCE = "/rest_newsBySource/";


    public ArrayList<RssBean> findNews(String txt){
      return new ArrayList<RssBean>(Arrays.asList(restTemplate.getForObject(BASE_URL +URL_FIND_NEWS + txt, RssBean[].class)));
    }
    public ArrayList<RssBean> findAll(){
        return new ArrayList<RssBean>(Arrays.asList(restTemplate.getForObject(BASE_URL+URL_ALL_NEWS, RssBean[].class)));
    }
    public RssBean[] findByRating(String rating){
        return restTemplate.getForObject(BASE_URL+URL_RATING+rating, RssBean[].class);

    }
    public RssBean[] findBySource(String source) {
        return restTemplate.getForObject(BASE_URL+URL_BY_SOURCE + source, RssBean[].class);
    }
    public RssBean[] findBySourceAndRating (String source,String rating) {
        return restTemplate.getForObject(BASE_URL+URL_BY_SOURCE + source, RssBean[].class);
    }
    public RssBean[] find(){
        return restTemplate.getForObject(BASE_URL+URL_ALL_NEWS,RssBean[].class);
    }
    public RssBean[] findNew(String txt){
        return restTemplate.getForObject(BASE_URL +URL_FIND_NEWS + txt, RssBean[].class);
    }

}
