package com.dinamsky.BotRest.rest;

import com.dinamsky.BotRest.config.Config;
import com.dinamsky.BotRest.entity.Bot;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

public class BotRest {
    @Autowired
    RestTemplate restTemplate;
//    это с моей стороны
    @PostMapping("/newbot")
    public ResponseEntity<Config> createBot(@RequestBody Config config){
    Bot bot = new Bot(config.getId(),config.getToken(),config.getUsername());
    return new ResponseEntity<Config>(config, HttpStatus.OK); }


//    //это с твоей стороны
//    public void createBot(String token,String name){
//    RestTemplate restTemplate = new RestTemplate();
//    HttpEntity<Config> request = new HttpEntity<>(new Config(token,name));
//    ResponseEntity<Config> response = restTemplate
//            .exchange(createBotUrl, HttpMethod.POST, request, Config.class);
//
//    //здесь можно проверить что все ок
//    (response.getStatusCode(), is(HttpStatus.CREATED));
//   //здесь показать что бот с таким то конфигом создан
//    Config config= response.getBody();}
//
//

//    @GetMapping("/bot/{id}")




}
