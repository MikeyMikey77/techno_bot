package khpi.cs.se.db.semesterwork.configuration;

import com.pengrad.telegrambot.TelegramBot;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.context.annotation.PropertySource;

@Configuration
@PropertySource("classpath:bot.properties")
public class BeansConfig {

    @Value("${bot.token}")
    private String botToken;

    @Value("${bot.name}")
    private String botName;

    @Bean
    public TelegramBot bot(){
        return new TelegramBot(botToken);
    }

}
