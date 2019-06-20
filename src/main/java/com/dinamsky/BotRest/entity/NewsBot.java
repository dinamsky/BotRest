package com.dinamsky.BotRest.entity;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.springframework.web.client.RestTemplate;

import javax.annotation.PostConstruct;

@Component
public class NewsBot extends TelegramLongPollingBot {

    private static final Logger logger = LoggerFactory.getLogger(NewsBot.class);
    static final String URL_ALL_NEWS = "http://localhost:8080/rest_allNews";
    static final String URL_FIND_NEWS = "http://localhost:8080/rest_findNews/";
    @Autowired
    RestTemplate restTemplate;
    @Bean
    public RestTemplate restTemplate(RestTemplateBuilder builder) {
        return builder.build();
    }


    @Value("${bot.token}")
    private String token;

    @Value("${bot.username}")
    private String username;

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
            sendMsg(msg, "Hello, world! This is Fake News bot!");
        }
        if (txt.equals("/info")) {
            this.sendMsg(msg, "Soon here'll be some interesting service! for more info you can go direct on http://fakenewsbot.online");
        }
        if (txt.equals("/news")) {

            RssBean[] list = restTemplate.getForObject(URL_ALL_NEWS, RssBean[].class);
            RssBean textSent= null;;

            for (RssBean text : list) {

                    this.sendMsg(msg, text.toString());
                   };





            }

        if (txt.equals("/help")) {
            sendMsg(msg, "Send news to read all news");
        }
        RssBean[] list = restTemplate.getForObject(URL_FIND_NEWS+txt, RssBean[].class);
        RssBean textSent;;
        for (RssBean text : list) {

                this.sendMsg(msg, text.toString());
                };


           }


//    if(update.hasMessage()){
//        Message message = update.getMessage();
//
//        //check if the message contains a text
//        if(message.hasText()){
//            String input = message.getText();
//
//            if(input.equals("/start")){
//                SendMessage sendMessagerequest = new SendMessage();
//                sendMessagerequest.setChatId(message.getChatId().toString());
//                /*
//                 * we just add the first link from our array
//                 *
//                 * We use markdown to embedd the image
//                 */
//                sendMessagerequest.setText("[​](" + this.urls.get(0)[1] + ")");
//                sendMessagerequest.enableMarkdown(true);
//
//                sendMessagerequest.setReplyMarkup(this.getGalleryView(0, -1));
//
//
//                try {
//                    sendMessage(sendMessagerequest);
//                } catch (TelegramApiException e) {
//                    e.printStackTrace();
//                }
////            }
//        }
//    }
//		else if(update.hasCallbackQuery()){
//        CallbackQuery callbackquery = update.getCallbackQuery();
//        String[] data = callbackquery.getData().split(":");
//        int index = Integer.parseInt(data[2]);
//
//        if(data[0].equals("gallery")){
//
//            InlineKeyboardMarkup markup = null;
//
//            if(data[1].equals("back")){
//                markup = this.getGalleryView(Integer.parseInt(data[2]), 1);
//                if(index > 0){
//                    index--;
//                }
//            }else if(data[1].equals("next")){
//                markup = this.getGalleryView(Integer.parseInt(data[2]), 2);
//                if(index < this.urls.size()-1){
//                    index++;
//                }
//            }else if(data[1].equals("text")){
//                try {
//                    this.sendAnswerCallbackQuery("Please use one of the given actions below, instead.", false, callbackquery);
//                } catch (TelegramApiException e) {
//                    e.printStackTrace();
//                }
//            }
//
//            if(markup == null){
//                try {
//                    this.sendAnswerCallbackQuery(INDEX_OUT_OF_RANGE, false, callbackquery);
//                } catch (TelegramApiException e) {
//                    e.printStackTrace();
//                }
//            }else{
//
//                EditMessageText editMarkup = new EditMessageText();
//                editMarkup.setChatId(callbackquery.getMessage().getChatId().toString());
//                editMarkup.setInlineMessageId(callbackquery.getInlineMessageId());
//                editMarkup.setText("[​](" + this.urls.get(index)[1] + ")");
//                editMarkup.enableMarkdown(true);
//                editMarkup.setMessageId(callbackquery.getMessage().getMessageId());
//                editMarkup.setReplyMarkup(markup);
//                try {
//                    editMessageText(editMarkup);
//                } catch (TelegramApiException e) {
//                    e.printStackTrace();
//                }
//
//            }
//
//
//
//        }
//    }


    @PostConstruct
    public void start() {
        logger.info("username: {}, token: {}", username, token);
    }

    private void sendMsg(Message msg, String text) {
        SendMessage s = new SendMessage();
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

