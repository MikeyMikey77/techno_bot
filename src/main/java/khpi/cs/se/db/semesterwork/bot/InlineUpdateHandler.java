package khpi.cs.se.db.semesterwork.bot;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.model.CallbackQuery;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.AnswerCallbackQuery;
import com.pengrad.telegrambot.request.DeleteMessage;
import com.pengrad.telegrambot.request.EditMessageReplyMarkup;
import com.pengrad.telegrambot.request.SendMessage;
import khpi.cs.se.db.semesterwork.model.Basket;
import khpi.cs.se.db.semesterwork.model.Product;
import khpi.cs.se.db.semesterwork.model.ProductInBasket;
import khpi.cs.se.db.semesterwork.model.User;
import khpi.cs.se.db.semesterwork.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class InlineUpdateHandler {

    private final UserService userService;
    private final UserRoleService userRoleService;
    private final ProductInBasketService productInBasketService;
    private final BasketService basketService;
    private final ProductService productService;
    private final BotKeyboards keyboards;

    private final TelegramBot bot;
    private OrderService orderService;

    @Autowired
    public InlineUpdateHandler(UserService userService, UserRoleService userRoleService,
                               BasketService basketService, ProductService productService,
                               ProductInBasketService productInBasketService, TelegramBot bot,
                               BotKeyboards botKeyboards, OrderService orderService) {
        this.userRoleService = userRoleService;
        this.userService = userService;
        this.basketService = basketService;
        this.productService = productService;
        this.productInBasketService = productInBasketService;
        this.bot = bot;
        this.keyboards = botKeyboards;
        this.orderService = orderService;
    }

    public void handleUpdate(Update update, User user) {
        switch (getArgument(update.callbackQuery().data(), 1)) {
            case "approve_user":
                try {
                    addAdmin(getArgument(
                            update.callbackQuery().data(),
                            0)
                    );
                } catch (Exception e) {
                    bot.execute(new SendMessage(user.getChatId(),
                            "Can`t add this user as admin")
                    );
                }
                break;
            case "add_to_basket":
                System.out.println(update.callbackQuery().message());
                addToBasket(update, user);
                break;
            case "delete_from_basket":
                deleteProdFromBasket(update, user);
                break;
            case "->":
                changeCount("more", update, user);
                break;
            case "<-":
                changeCount("less", update, user);
                break;
            case "delete_order":
                deleteOrder(update);
                break;
        }
        answerCallbackQuery(update.callbackQuery());
    }

    private void changeCount(String moreOrLess, Update update, User user) {
        String callbackData = update.callbackQuery().data();
        ProductInBasket productInBasket = null;
        for (ProductInBasket item : user.getBasket().getProductsInBasket()) {
            if (item.getProduct().getIdProduct().equals(Integer.parseInt(getArgument(callbackData, 2)))) {
                if (moreOrLess.equals("more")) {
                    item.setCount(item.getCount() + 1);
                } else {
                    if (!item.getCount().equals(1))
                        item.setCount(item.getCount() - 1);
                }
                productInBasketService.save(item);
                productInBasket = item;
            }
        }
        EditMessageReplyMarkup editMessageReplyMarkup = new EditMessageReplyMarkup(
                update.callbackQuery().message().chat().id(),
                update.callbackQuery().message().messageId()
        );
        editMessageReplyMarkup
                .replyMarkup(
                        keyboards.forBasketProductList(productInBasket.getProduct(),productInBasket.getCount() )
                );
        bot.execute(editMessageReplyMarkup);
    }

    private void deleteProdFromBasket(Update update, User user) {
        String data = update.callbackQuery().data();
        Product prod = productService.findByIdProduct(
                Integer.parseInt(getArgument(data, 0)));
        if (productInBasketService.findByBasketAndProduct(user.getBasket(), prod) != null) {
            productInBasketService.delete(
                    productInBasketService.findByBasketAndProduct(user.getBasket(), prod)
            );
        }
        if (user.getBasket().getProductsInBasket().size() == 0) {
            basketService.delete(user.getBasket());
        }
        DeleteMessage dm = new DeleteMessage(update.callbackQuery().message().chat().id(), update.callbackQuery().message().messageId());
        bot.execute(dm);
    }

    private void addToBasket(Update update, User user) {
        String idProd = getArgument(update.callbackQuery().data(), 0);
        if (user.getBasket() == null) {
            Basket b = new Basket();
            basketService.save(b);
            user.setBasket(b);
            userService.save(user);
        }

        ProductInBasket pib = new ProductInBasket()
                .setCount(1)
                .setBasket(user.getBasket())
                .setProduct(productService.findByIdProduct(Integer.parseInt(idProd)));
        productInBasketService.save(pib);
        user
                .getBasket()
                .getProductsInBasket()
                .add(pib);
        userService.save(user);
        EditMessageReplyMarkup editMessageReplyMarkup = new EditMessageReplyMarkup(
                update.callbackQuery().message().chat().id(),
                update.callbackQuery().message().messageId()
        );
        editMessageReplyMarkup.replyMarkup(keyboards.forCategoryProductList(pib.getProduct(), "added"));
        bot.execute(editMessageReplyMarkup);
    }

    private void deleteOrder(Update update){
        String callbackData = update.callbackQuery().data();
        orderService.safeDelete(orderService.findById(Integer.parseInt(getArgument(callbackData, 0))));
        DeleteMessage dm =  new DeleteMessage(
                update.callbackQuery().message().chat().id(),
                update.callbackQuery().message().messageId()
        );
        bot.execute(dm);
    }

    private void addAdmin(String idUser) throws Exception {
        User newAdmin = userService.findById(Integer.parseInt(idUser)).get();
        newAdmin.setUserRole(userRoleService.findByTitle("ROLE_ADMIN").get());
        newAdmin.setActive(true);
        userService.save(newAdmin);
    }

    private String getArgument(String str, Integer argIndex) {
        return str.split("\\|")[argIndex];
    }

    private void answerCallbackQuery(CallbackQuery callbackQuery) {
        AnswerCallbackQuery answerCallbackQuery =
                new AnswerCallbackQuery(callbackQuery.id());
        bot.execute(answerCallbackQuery);
    }
}
