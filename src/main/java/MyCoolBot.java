import org.telegram.telegrambots.bots.TelegramLongPollingBot;
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

public class MyCoolBot extends TelegramLongPollingBot {

    InlineKeyboardMarkup inlineKeyboardMarkup =new InlineKeyboardMarkup();
    List<InlineKeyboardButton> keyboardButtonsRow1 = new ArrayList<>();
    List<List<InlineKeyboardButton>> rowList= new ArrayList<>();



        public void onUpdateReceived(Update update) {
            if (update.hasMessage() && update.getMessage().hasText()) {
                if(update.getMessage().getText().equals("Hello")){
                    try {
                        execute(sendInlineKeyBoardMessage(update.getMessage().getChatId(),update.getMessage()));
                    } catch (TelegramApiException e) {
                        e.printStackTrace();
                    }
                }
            }else if(update.hasCallbackQuery()){
                try {
                    execute(new SendMessage().setText(
                            update.getCallbackQuery().getData())
                            .setChatId(update.getCallbackQuery().getMessage().getChatId()));
                } catch (TelegramApiException e) {
                    e.printStackTrace();
                }
            }
        }


        public String getBotUsername() {
            return "AlbertLOH";
        }

        @Override
        public String getBotToken() {
            return "1109980768:AAGW5Xe-_sE5NYqHsH388JnlJBWIifScMXA";
        }

        public static SendMessage sendInlineKeyBoardMessage(long chatId,Message message) {
        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();
        List<InlineKeyboardButton> keyboardButtonsRow1 = new ArrayList<>();
        //List<InlineKeyboardButton> keyboardButtonsRow2 = new ArrayList<>();
        keyboardButtonsRow1.add(new InlineKeyboardButton().setText("Бог").setCallbackData("Создатель этого бота"));
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

