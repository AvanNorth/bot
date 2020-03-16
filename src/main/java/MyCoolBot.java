import org.telegram.telegrambots.bots.DefaultBotOptions;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.extensions.bots.commandbot.TelegramLongPollingCommandBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.User;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.*;

public class MyCoolBot extends TelegramLongPollingCommandBot {

    InlineKeyboardMarkup inlineKeyboardMarkup =new InlineKeyboardMarkup();
    List<InlineKeyboardButton> keyboardButtonsRow1 = new ArrayList<>();
    List<List<InlineKeyboardButton>> rowList= new ArrayList<>();
    ArrayList<String> regInfo = new ArrayList<>();
    private static final String BOT_NAME = "AlbertLOH";

    public MyCoolBot(){

        HelpCommand helpCommand = new HelpCommand(this);
        register(helpCommand);
        RegCommand regCommand = new RegCommand(this);
        register(regCommand);
        BuyCommand buyCommand = new BuyCommand(this);
        register(buyCommand);
    }


    public String getBotUsername() {
            return "AlbertLOH";
        }

    @Override
    public void processNonCommandUpdate(Update update) {

        Message msg = update.getMessage();
        User user = msg.getFrom();
        SendMessage message = new SendMessage();
       sendMessageToUser(msg.getChatId(),"Такой команды нет, попробуй /help");

    }

    private void sendMessageToUser(long chatId,String text) {
        SendMessage message = new SendMessage();
        message.setText(text);
        message.setChatId(chatId);
        try {
            execute(message);
        } catch (TelegramApiException e) {
        }
    }

    @Override
    public String getBotToken() {
            return "1109980768:AAGW5Xe-_sE5NYqHsH388JnlJBWIifScMXA";
        }

    public static SendMessage sendInlineKeyBoardMessage(long chatId,Message message) {
        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();
        List<InlineKeyboardButton> keyboardButtonsRow1 = new ArrayList<>();
        //List<InlineKeyboardButton> keyboardButtonsRow2 = new ArrayList<>();
        keyboardButtonsRow1.add(new InlineKeyboardButton().setText("Зарегаться").setCallbackData("Создатель этого бота"));
        keyboardButtonsRow1.add(new InlineKeyboardButton().setText("God").setCallbackData("This bot creator"));
        keyboardButtonsRow1.add(new InlineKeyboardButton().setText("UserInfo").setCallbackData("Я знаю многое о тебе, "+ getUserInfo(message,"userName")+" aka " + getUserInfo(message,"firstName")+" "+getUserInfo(message,"lastName")));
       // keyboardButtonsRow2.add(inlineKeyboardButton2);
        List<List<InlineKeyboardButton>> rowList = new ArrayList<>();
        rowList.add(keyboardButtonsRow1);
        //rowList.add(keyboardButtonsRow2);
        inlineKeyboardMarkup.setKeyboard(rowList);
        return new SendMessage().setChatId(chatId).setText("Ну чтож").setReplyMarkup(inlineKeyboardMarkup);
    }

    private static String getUserInfo(Message message, String type){
            switch (type){
                case "firstName": return message.getFrom().getFirstName();
                case "lastName": return message.getFrom().getLastName();
                case "id": return message.getFrom().getId().toString();
                case "userName": return message.getFrom().getUserName();
            }
            return null;
    }



    }

