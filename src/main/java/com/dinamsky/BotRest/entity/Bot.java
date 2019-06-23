package com.dinamsky.BotRest.entity;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.AnswerCallbackQuery;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;

public class Bot extends TelegramLongPollingBot {

    private static final Logger logger = LoggerFactory.getLogger(Bot.class);
//    private String URL_REST = "http://localhost:8080/rest/";
//
//    @Autowired
//    RestTemplate restTemplate;
//    @Bean
//    public RestTemplate restTemplate(RestTemplateBuilder builder) {
//        return builder.build();
//    }
    private String id;
    private String token;
    private String username;

    public Bot(String id,String token, String username) {
        this.id = id;
        this.token = token;
        this.username = username;
    }

    @Override
    public String getBotToken() {
        return token;
    }

    @Override
    public String getBotUsername() {
        return username;
    }

    @Override
    public void onUpdateReceived(Update update) {

        Message msg = update.getMessage();
        String txt = msg.getText();
        if (txt.equals("/start")) {
            sendMsg(msg, "Hello, world! This is "+username+" bot!");
        }
        if (txt.equals("/info")) {
            this.sendMsg(msg, "Soon here'll be some interesting service! for more info you can go direct on http://fakenewsbot.online");
        }

        if (txt.equals("/help")) {
            sendMsg(msg, "I dont know what i can.");
        }}





    @PostConstruct
    public void start() {
        logger.info("username: {}, token: {}", username, token);
    }

    private void sendMsg(Message msg, String text) {
        SendMessage s = new SendMessage();
        s.enableMarkdown(true);
        s.setChatId(msg.getChatId());
        s.setText(text);
        try {
            execute(s);
            logger.info("Sent message \"{}\" to {}", text);
        } catch (TelegramApiException e){
            logger.error("Failed to send message \"{}\" to {} due to error: {}", text,  e.getMessage());
        }
    }






}

