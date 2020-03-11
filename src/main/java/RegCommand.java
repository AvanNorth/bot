import org.telegram.telegrambots.extensions.bots.commandbot.commands.ICommandRegistry;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Chat;
import org.telegram.telegrambots.meta.api.objects.User;
import org.telegram.telegrambots.meta.bots.AbsSender;

import java.util.regex.Pattern;

public final class RegCommand extends BotsCommand {

    private final ICommandRegistry mCommandRegistry;
    private final String defText = " , чтобы зарегистрироваться , нужно ввести корректный емэйл! Попробуй /reg <email@example.com>";

    public RegCommand(ICommandRegistry commandRegistry) {
        super("reg", " регистрация");
        mCommandRegistry = commandRegistry;
    }

    @Override
    public void execute(AbsSender absSender, User user, Chat chat, String[] strings) {


        StringBuilder sb = new StringBuilder();

        SendMessage message = new SendMessage();
        message.setChatId(chat.getId().toString());

        String email = getEmail(strings);

        if (email == null) {
            sb.append(user.getUserName());
            sb.append(defText);

            message.setText(sb.toString());
            execute(absSender, message, user);
        }else{
            message.setText(email);
            execute(absSender, message, user);
        }

    }

    private String getEmail(String[] strings) {

        if (strings == null || strings.length == 0) {
            return null;
        }

        String name = String.join(" ", strings);
        String email = name.replaceAll(" ", "").isEmpty() ? null : name;


        String emailRegex = "^[a-zA-Z0-9_+&*-]+(?:\\."+
                "[a-zA-Z0-9_+&*-]+)*@" +
                "(?:[a-zA-Z0-9-]+\\.)+[a-z" +
                "A-Z]{2,7}$";

        Pattern pat = Pattern.compile(emailRegex);

        if (pat.matcher(email).matches()){
            return email;
        }else {
            return null;
        }
    }
}