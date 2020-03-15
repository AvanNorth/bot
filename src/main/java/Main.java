import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.telegram.telegrambots.ApiContextInitializer;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.HashMap;
import java.util.Map;

public class Main {
    public static void main(String[] args) {
        Map<String, String> jdbcUrlSettings = new HashMap<>();
        String jdbcDbUrl = System.getenv("JDBC_DATABASE_URL");
        if (null != jdbcDbUrl) {
            jdbcUrlSettings.put("hibernate.connection.url", System.getenv("JDBC_DATABASE_URL"));
        }

        new StandardServiceRegistryBuilder().
                configure("hibernate.cfg.xml").
                applySettings(jdbcUrlSettings).
                build();

        ApiContextInitializer.init();
        TelegramBotsApi botsApi = new TelegramBotsApi();
        try {
            botsApi.registerBot(new MyCoolBot());
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }
}
