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

import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public final class BuyCommand extends BotsCommand {

    private final ICommandRegistry mCommandRegistry;
    private AbsSender absSender;
    private User user;
    private String comId;
    private int sum;


    public BuyCommand(ICommandRegistry commandRegistry) {
        super("buy", " покупка");
        mCommandRegistry = commandRegistry;
    }

    @Override
    public void execute(AbsSender absSender, User user, Chat chat, String[] strings) {
        StringBuilder sb = new StringBuilder();
        boolean b=false;


        SendMessage message = new SendMessage();
        message.setChatId(chat.getId().toString());
        String comId = getAlphaNumericString(15);
        int sum = 1; //TODO сюда крч цену кидать надо будет с кнопки, если через команду все делаем, то тогда юзер будет писать



        message.setText("Проведите платеж на номер +79370073938 (qiwi), в комментарии укажите то, что я пришлю ниже. "+"\n **Внимание!** Без этого комментария платеж засчитан не будет!");
        execute(absSender,message,user);
        message.setText(comId);
        execute(absSender,message,user);

        long t= System.currentTimeMillis();
        long end = t+300000;
        while(System.currentTimeMillis() < end) {
            b=request(comId,sum,absSender,user,chat,message);
            if(b){
                break;
            }
            try {
                Thread.sleep(3000); //todo поставить 900000 (15 минут)
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        if (!b) {
            message.setText("Прошло 15 мин, а платежа все нет.\n Если вы оплатили, а это все баг, то пишите @SofHacker");
            execute(absSender, message, user);
        }

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


    private boolean request(String id, int sum, AbsSender absSender, User user, Chat chat, SendMessage message) {
        ResponseHandler<JSONObject> rh = httpRequest.target("https://edge.qiwi.com/payment-history/v2/persons/79370073938/payments?rows=11")
                .addHeader(authHeader)
                .addHeader(TypeHeader)
                .request(HttpMethod.GET, JSONObject.class);

        JSONObject someType = rh.get();
        ArrayList arrayList;
        arrayList = (ArrayList) someType.get("data");

        String regex1 = "(?<name>comment=";
        regex1=regex1+id+")";
        String regex2 = "(?<name>total=\\{amount=";
        regex2=regex2+sum+")";

        boolean comB=false,sumB=false,suc = false;

            for (int i=0;i<10;i++){

                final Pattern pattern1 = Pattern.compile(regex1, Pattern.MULTILINE);
                final Matcher matcher = pattern1.matcher(arrayList.get(i).toString());

                final Pattern pattern2 = Pattern.compile(regex2, Pattern.MULTILINE);
                final Matcher matcher2 = pattern2.matcher(arrayList.get(i).toString());

            if (matcher2.find()) {
                sumB=true;
                comB = matcher.find();
            }else {
                sumB=false;
            }

                if(sumB&&comB){
                    message.setText("Платеж найден");
                    execute(absSender,message,user);
                    suc = true;
                    //TODO отправить из бд ключ
                    break;
                }
        }
        return suc;
    }


    public static String getAlphaNumericString(int n)

    {

        // длина ограничена 256 символами
        byte[] array = new byte[256];
        new Random().nextBytes(array);


        String randomString
                = new String(array, Charset.forName("UTF-8"));


        // Создать StringBuffer для сохранения результата
        StringBuffer r = new StringBuffer();


        // Добавляем первые 20 буквенно-цифровых символов
        // из сгенерированной случайной строки в результат
        for (int k = 0; k < randomString.length(); k++) {


            char ch = randomString.charAt(k);


            if (((ch >= 'a' && ch <= 'z')
                    || (ch >= 'A' && ch <= 'Z')
                    || (ch >= '0' && ch <= '9'))
                    && (n > 0)) {


                r.append(ch);
                n--;
            }
        }


        // возвращаем результирующую строку
        return r.toString();
    }

}
