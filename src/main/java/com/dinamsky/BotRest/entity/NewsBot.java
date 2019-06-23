package com.dinamsky.BotRest.entity;

import com.dinamsky.BotRest.service.NewsService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RestController;
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
import org.springframework.web.client.RestTemplate;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;

@RestController
@Component
public class NewsBot extends TelegramLongPollingBot {
    private static final Logger logger = LoggerFactory.getLogger(NewsBot.class);

@Autowired
    NewsService newsService;


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
        if (update.hasMessage()) {
            Message msg = update.getMessage();
            String txt = msg.getText();
            if (txt.equals("/start")) {
                sendMsg(msg, "Hello, world! This is Fake News bot!");
            }
            if (txt.equals("/info")) {
                this.sendMsg(msg, "Soon here'll be some interesting service! for more info you can go direct on http://fakenewsbot.online");
            }
            if (txt.equals("/news")) {
                 for (RssBean text : newsService.findAll()) {

                    this.sendMsg(msg, text.toString());
                }
                ;


            }

            if (txt.equals("/help")) {
                sendMsg(msg, "Send news to read all news");
            }
            for (RssBean text : newsService.findNews(txt)) {

                this.sendMsg(msg, text.toString());
            }
            ;


        } else if (update.hasCallbackQuery()) {
            CallbackQuery callbackQuery = update.getCallbackQuery();
            try {
                this.sendAnswerCallbackQuery("Works callback", false, callbackQuery);
            } catch (TelegramApiException e) {
                e.printStackTrace();
            }
        }


    }

    @PostConstruct
    public void start() {
        logger.info("username: {}, token: {}", username, token);
    }

    private void sendMsg(Message msg, String text) {

        SendMessage s = new SendMessage();
        s.enableMarkdown(true);
        this.setInline(s);
        s.setChatId(msg.getChatId());
        s.setText(text);
        try {
            execute(s);
            logger.info("Sent message \"{}\" to {}", text);
        } catch (TelegramApiException e){
            logger.error("Failed to send message \"{}\" to {} due to error: {}", text,  e.getMessage());
        }


    }
    private void sendAnswerCallbackQuery(String text, boolean alert, CallbackQuery callbackquery) throws TelegramApiException{
        AnswerCallbackQuery answerCallbackQuery = new AnswerCallbackQuery();
        answerCallbackQuery.setCallbackQueryId(callbackquery.getId());
        answerCallbackQuery.setShowAlert(alert);
        answerCallbackQuery.setText(text);
        execute(answerCallbackQuery);
    }
    public synchronized void setButtons(SendMessage sendMessage) {
        // Создаем клавиуатуру
        ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup();
        sendMessage.setReplyMarkup(replyKeyboardMarkup);
        replyKeyboardMarkup.setSelective(true);
        replyKeyboardMarkup.setResizeKeyboard(true);
        replyKeyboardMarkup.setOneTimeKeyboard(false);

        // Создаем список строк клавиатуры
        List<KeyboardRow> keyboard = new ArrayList<>();

        // Первая строчка клавиатуры
        KeyboardRow keyboardFirstRow = new KeyboardRow();
        // Добавляем кнопки в первую строчку клавиатуры
        keyboardFirstRow.add(new KeyboardButton("Поиск"));

        // Вторая строчка клавиатуры
        KeyboardRow keyboardSecondRow = new KeyboardRow();
        // Добавляем кнопки во вторую строчку клавиатуры
        keyboardSecondRow.add(new KeyboardButton("Помощь"));

        // Добавляем все строчки клавиатуры в список
        keyboard.add(keyboardFirstRow);
        keyboard.add(keyboardSecondRow);
        // и устанваливаем этот список нашей клавиатуре
        replyKeyboardMarkup.setKeyboard(keyboard);
    }
    private void setInline(SendMessage sendMessage) {
        List<List<InlineKeyboardButton>> buttons = new ArrayList<>();
        List<InlineKeyboardButton> buttons1 = new ArrayList<>();
        buttons1.add(new InlineKeyboardButton().setText("Кнопка").setCallbackData(String.valueOf(17)));
        buttons.add(buttons1);

        InlineKeyboardMarkup markupKeyboard = new InlineKeyboardMarkup();
        markupKeyboard.setKeyboard(buttons);
    }

}

