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
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboard;
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
    final private String HELP = "Помощь";
    final private String FIND = "Искать новости";
    final private String BACK = "⬅️ ";
    final private String NEXT = " ➡️";
    final private String INDEX_OUT_OF_RANGE ="ВСЁ";
    final private String ALL = "Последние новости";

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

    private ArrayList<RssBean> rssBeans;

    private RssBean[] rssBean;


    @Override
    public void onUpdateReceived(Update update) {
        if (update.hasMessage()) {
            Message msg = update.getMessage();
            String txt = msg.getText();
            if(txt.equals("/start")){
                SendMessage sendMessagerequest = new SendMessage();
                sendMessagerequest.setChatId(msg.getChatId().toString());
                /*
                 * we just add the first link from our array
                 *
                 * We use markdown to embedd the image
                 */
                sendMessagerequest.setText("Привет, Я новостной бот!");
                sendMessagerequest.enableMarkdown(true);

                sendMessagerequest.setReplyMarkup(this.getButtonsView(-1,-1));


                try {
                    execute(sendMessagerequest);
                } catch (TelegramApiException e) {
                    e.printStackTrace();
                }
            }
            rssBean = newsService.findNew(txt);
            SendMessage sendMessagerequest = new SendMessage();
            sendMessagerequest.setChatId(msg.getChatId().toString());
            /*
             * we just add the first link from our array
             *
             * We use markdown to embedd the image
             */
            sendMessagerequest.setText(rssBeans.get(0).toString());
            sendMessagerequest.enableMarkdown(true);

            sendMessagerequest.setReplyMarkup(this.getButtonsView(-1,-1));


            try {
                execute(sendMessagerequest);
            } catch (TelegramApiException e) {
                e.printStackTrace();
            }
        }
        else if(update.hasCallbackQuery()){
            CallbackQuery callbackquery = update.getCallbackQuery();
            String[] data = callbackquery.getData().split(":");
            int index = Integer.parseInt(data[2]);

            if(data[0].equals("news")){

                InlineKeyboardMarkup markup = null;

                if(data[1].equals("back")){
                    markup = this.getButtonsView(Integer.parseInt(data[2]), 1);
                    if(index > 0){
                        index--;
                    }
                }else if(data[1].equals("next")){
                    markup = this.getButtonsView(Integer.parseInt(data[2]), 2);
                    if(index < rssBean.length-1){
                        index++;
                    }
                }else if(data[1].equals("find")){

                    try {
                        this.sendAnswerCallbackQuery("Введите текст", false, callbackquery);
                    } catch (TelegramApiException e) {
                        e.printStackTrace();
                    }
                }
                else if(data[1].equals("all")){

                    rssBean = newsService.find();
                    try {
                        this.sendAnswerCallbackQuery(rssBean[0].toString(), false, callbackquery);
                    } catch (TelegramApiException e) {
                        e.printStackTrace();
                    }
                }

                if(markup == null){
                    try {
                        this.sendAnswerCallbackQuery(INDEX_OUT_OF_RANGE, false, callbackquery);
                    } catch (TelegramApiException e) {
                        e.printStackTrace();
                    }
                }else{

                    EditMessageText editMarkup = new EditMessageText();
                    editMarkup.setChatId(callbackquery.getMessage().getChatId().toString());
                    editMarkup.setInlineMessageId(callbackquery.getInlineMessageId());
                    editMarkup.setText("[​](" + rssBean[index].toString() + ")");
                    editMarkup.enableMarkdown(true);
                    editMarkup.setMessageId(callbackquery.getMessage().getMessageId());
                    editMarkup.setReplyMarkup(markup);
                    try {
                        execute(editMarkup);
                    } catch (TelegramApiException e) {
                        e.printStackTrace();
                    }

                }



            }
        }



    }

    private InlineKeyboardMarkup getButtonsView(int index,int action) {
        InlineKeyboardMarkup markupInline = new InlineKeyboardMarkup();

        if(action == 1 && index > 0){
            index--;
        }
        else if((action == 1 && index == 0)){
            return null;
        }
        else if(action == 2 && index >= rssBeans.size()-1){
            return null;
        }
        else if(action == 2){
            index++;
        }
        else if(action == -1){
            List<List<InlineKeyboardButton>> rowsInline = new ArrayList<>();
            List<InlineKeyboardButton> rowInline = new ArrayList<>();
            List<InlineKeyboardButton> rowInline2 = new ArrayList<>();
            rowInline.add(new InlineKeyboardButton().setText(ALL).setCallbackData("news:all:" +index));
            rowInline2.add(new InlineKeyboardButton().setText(FIND).setCallbackData("news:find"+index));
            rowsInline.add(rowInline);
            rowsInline.add(rowInline2);
            markupInline.setKeyboard(rowsInline);

            return markupInline;
        }




        List<List<InlineKeyboardButton>> rowsInline = new ArrayList<>();

        List<InlineKeyboardButton> rowInline = new ArrayList<>();

        List<InlineKeyboardButton> rowInline2 = new ArrayList<>();
        rowInline2.add(new InlineKeyboardButton().setText(BACK).setCallbackData("news:back"+index));
        rowInline2.add(new InlineKeyboardButton().setText(NEXT).setCallbackData("news:next"+index));





        rowsInline.add(rowInline);

        rowsInline.add(rowInline2);

        markupInline.setKeyboard(rowsInline);

        return markupInline;

    }

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
    private void sendAnswerCallbackQuery(String text, boolean alert, CallbackQuery callbackquery) throws TelegramApiException{
        AnswerCallbackQuery answerCallbackQuery = new AnswerCallbackQuery();
        answerCallbackQuery.setCallbackQueryId(callbackquery.getId());
        answerCallbackQuery.setShowAlert(alert);
        answerCallbackQuery.setText(text);
        execute(answerCallbackQuery);
    }


}

