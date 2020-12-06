package khpi.cs.se.db.semesterwork.controller;

import com.pengrad.telegrambot.BotUtils;
import khpi.cs.se.db.semesterwork.bot.BotMarket;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;


@RestController
public class BotController {

    private BotMarket bot;

    @Autowired
    BotController(BotMarket bot){
        this.bot = bot;
    }

    @PostMapping(value = "/webhook/bot", consumes = MediaType.APPLICATION_JSON_VALUE)
    public void webhook(@RequestBody String json) {
        bot.handleUpdate(BotUtils.parseUpdate(json));
    }
}
