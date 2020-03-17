import com.jsunsoft.http.*;
import org.apache.http.Header;
import org.apache.http.HeaderElement;
import org.apache.http.ParseException;
import org.apache.http.entity.ContentType;
import org.json.simple.JSONObject;
import org.telegram.telegrambots.extensions.bots.commandbot.commands.ICommandRegistry;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Chat;
import org.telegram.telegrambots.meta.api.objects.User;
import org.telegram.telegrambots.meta.bots.AbsSender;
import orm.DataUser;
import orm.UserService;

import java.util.ArrayList;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public final class BuyCommand extends BotsCommand {

    private final ICommandRegistry mCommandRegistry;

    public BuyCommand(ICommandRegistry commandRegistry) {
        super("buy", " покупка");
        mCommandRegistry = commandRegistry;
    }

    @Override
    public void execute(AbsSender absSender, User user, Chat chat, String[] strings) {
        StringBuilder sb = new StringBuilder();

        SendMessage message = new SendMessage();
        message.setChatId(chat.getId().toString());
        Random random = new Random();
        int comId = 123; //random.nextInt();
        int sum = 1;
        request(comId,sum,absSender,user,chat,message);



    }


    private static final Header authHeader = new Header() {
        @Override
        public HeaderElement[] getElements() throws ParseException {
            return new HeaderElement[0];
        }

        @Override
        public String getName() {
            return "Authorization";
        }

        @Override
        public String getValue() {
            return "Bearer 27c1b65e32f2a1e32349cb8f7623359a";
        }

    };
    private static final Header TypeHeader = new Header() {
        @Override
        public HeaderElement[] getElements() throws ParseException {
            return new HeaderElement[0];
        }

        @Override
        public String getName() {
            return "Content-Type";
        }

        @Override
        public String getValue() {
            return "application/json";
        }
    };
    private static final HttpRequest httpRequest =
            HttpRequestBuilder.create(ClientBuilder.create().build())
                    .addContentType(ContentType.APPLICATION_JSON)
                    .build();


    void request(int id,int sum,AbsSender absSender, User user, Chat chat,SendMessage message) {
        ResponseHandler<JSONObject> rh = httpRequest.target("https://edge.qiwi.com/payment-history/v2/persons/79370073938/payments?rows=11").addHeader(authHeader).addHeader(TypeHeader).request(HttpMethod.GET, JSONObject.class);
        JSONObject someType = rh.get();
        ArrayList arrayList = new ArrayList();
        arrayList = (ArrayList) someType.get("data");

        String regex1 = "(?<name>comment=";
        regex1=regex1+id+")";
        String regex2 = "(?<name>sum=";
        regex2=regex2+sum+")";

    for (int i=0;i<10;i++){
        final Pattern pattern1 = Pattern.compile(regex1, Pattern.MULTILINE);
        final Matcher matcher = pattern1.matcher(arrayList.get(i).toString());

     if (matcher.find()) {
         final Pattern pattern2 = Pattern.compile(regex2, Pattern.MULTILINE);
         final Matcher matcher2 = pattern2.matcher(arrayList.get(i).toString());
         if (matcher2.find()) {
             execute(absSender, message, user);
         }
         else{
             message.setText("Платеж не проведен");
             execute(absSender,message,user);
         }
        }else {
        message.setText("Платеж не проведен");
        execute(absSender,message,user);
        }
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