package khpi.cs.se.db.semesterwork.bot;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.model.request.InlineKeyboardButton;
import com.pengrad.telegrambot.model.request.InlineKeyboardMarkup;
import com.pengrad.telegrambot.request.DeleteWebhook;
import com.pengrad.telegrambot.request.SendMessage;
import com.pengrad.telegrambot.request.SetWebhook;
import khpi.cs.se.db.semesterwork.model.BotState;
import khpi.cs.se.db.semesterwork.model.User;
import khpi.cs.se.db.semesterwork.service.UserRoleService;
import khpi.cs.se.db.semesterwork.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.List;


@Service
@PropertySource("classpath:bot.properties")
public class BotMarket {

    private final TelegramBot bot;
    private final UserRoleService userRoleService;
    private final UserService userService;

    private final BotKeyboards keyboards;
    private final InlineUpdateHandler inlineUpdateHandler;
    private final ReplyUpdateHandler replyUpdateHandler;
    private final PaymentHandler paymentHandler;
    private Environment env;

    @Value("${bot.webhook.url.dev}")
    private String webhookUrlDev;

    @Value("${bot.webhook.url.prod}")
    private String webhookUrlProd;

    static String currentAppUrl;

    @PostConstruct
    private void setWebhook() {
        if(env.getActiveProfiles()[0].equals("dev")){
            currentAppUrl = webhookUrlDev;
        } else {
            currentAppUrl = webhookUrlProd;
        }
        System.out.println("\n\n\n"+currentAppUrl+"\n\n\n");
        DeleteWebhook dw = new DeleteWebhook();
        bot.execute(dw);
        //building request for set webhook
        SetWebhook request = new SetWebhook()
                .url(currentAppUrl + "webhook/bot");
        // executing previous webhook request
        bot.execute(request);
    }

    @Autowired
    public BotMarket(UserRoleService userRoleService, UserService userService,
                     BotKeyboards keyboards, InlineUpdateHandler inlineUpdateHandler,
                     ReplyUpdateHandler replyUpdateHandler, TelegramBot bot,
                     Environment env, PaymentHandler paymentHandler) {
        this.userRoleService = userRoleService;
        this.userService = userService;
        this.keyboards = keyboards;
        this.inlineUpdateHandler =  inlineUpdateHandler;
        this.replyUpdateHandler = replyUpdateHandler;
        this.bot = bot;
        this.env = env;
        this.paymentHandler = paymentHandler;
    }

    public void handleUpdate(Update update) {
        User u = detectUser(update);
        if(update.preCheckoutQuery() != null
                || update.shippingQuery() != null
                || (update .message() != null && update.message().successfulPayment() != null)){
            paymentHandler.handleUpdate(update, u);
        } else if (update.callbackQuery() != null) {
            inlineUpdateHandler.handleUpdate(update, u);
        } else if (update.message().text().equals("/admin")) {
            approveAdmin(u);
        } else if (update.message().text().equals("Back to main menu...")) {
            u.setBotState(BotState.MAIN_MENU);
            userService.save(u);
            bot.execute(new SendMessage(u.getChatId(), "Please, choose any action")
                    .replyMarkup(keyboards.mainKeyboard()));
        } else {
            replyUpdateHandler.handleUpdate(u, update.message());
        }
    }

    private User detectUser(Update u) {
        User user;
        com.pengrad.telegrambot.model.User telegramUser = null;
        if(u.message() != null) {
            telegramUser = u.message().from();
        }else if(u.callbackQuery() != null){
            telegramUser = u.callbackQuery().from();
        } else if(u.shippingQuery() != null){
            telegramUser = u.shippingQuery().from();
        } else if(u.preCheckoutQuery() != null){
            telegramUser = u.preCheckoutQuery().from();
        }
        if (userService.findById(telegramUser.id()).isPresent()) {
            user = userService.findById(telegramUser.id()).get();
        } else {
            user = new User();
            if(userRoleService.findByTitle("ROLE_CUSTOMER10").isPresent())
                user.setUserRole(userRoleService.findByTitle("ROLE_CUSTOMER10").get());
            user.setUsername(telegramUser.username() != null
                    ? telegramUser.username()
                    : "username");
            user.setIdUser(telegramUser.id());
            user.setChatId(u.message().chat().id());
            user.setlName(telegramUser.lastName());
            user.setfName(telegramUser.firstName());
            user.setActive(false);
            userService.save(user);
        }
        return user;
    }

    private void approveAdmin(User u) {
        List<User> admins = userService.findByUserRole("ROLE_ADMIN");
        SendMessage sm = new SendMessage(admins.get(0).getChatId(),
                "Do you approve user: " + u.getfName() + " " + u.getlName() + " as admin?");
        sm.replyMarkup(new InlineKeyboardMarkup(new InlineKeyboardButton[]
                {new InlineKeyboardButton("Approve user")
                        .callbackData(u.getIdUser().toString()+"|approve_user")}));
        bot.execute(sm);
    }

}
