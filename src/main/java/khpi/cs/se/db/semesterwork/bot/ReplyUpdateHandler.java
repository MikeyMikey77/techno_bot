package khpi.cs.se.db.semesterwork.bot;

import com.google.maps.GeocodingApi;
import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.model.Message;
import com.pengrad.telegrambot.model.request.InlineKeyboardButton;
import com.pengrad.telegrambot.model.request.InlineKeyboardMarkup;
import com.pengrad.telegrambot.model.request.LabeledPrice;
import com.pengrad.telegrambot.model.request.ReplyKeyboardMarkup;
import com.pengrad.telegrambot.request.SendInvoice;
import com.pengrad.telegrambot.request.SendMessage;
import com.pengrad.telegrambot.request.SendPhoto;
import com.pengrad.telegrambot.response.SendResponse;
import khpi.cs.se.db.semesterwork.model.*;
import khpi.cs.se.db.semesterwork.service.*;
import khpi.cs.se.db.semesterwork.utils.GoogleApiUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.text.SimpleDateFormat;
import java.util.*;

@Component
public class ReplyUpdateHandler {

    private final BotKeyboards keyboards;
    private final TelegramBot bot;

    private final UserService userService;
    private final ProductTypeService productTypeService;
    private final OrderStateService orderStateService;
    private final OrderService orderService;
    private ProductInOrderService productInOrderService;
    private BasketService basketService;

    @Autowired
    public ReplyUpdateHandler(BotKeyboards botKeyboards, UserService userService,
                              TelegramBot bot, ProductTypeService productTypeService,
                              OrderStateService orderStateService, OrderService orderService,
                              ProductInOrderService productInOrderService, BasketService basketService) {
        this.keyboards = botKeyboards;
        this.userService = userService;
        this.bot = bot;
        this.productTypeService = productTypeService;
        this.orderStateService = orderStateService;
        this.orderService = orderService;
        this.productInOrderService = productInOrderService;
        this.basketService = basketService;
    }

    public void handleUpdate(User u, Message m) {
        switch (u.getBotState()) {
            case MAIN_MENU:
                mainMenu(m, u);
                return;
            case CATEGORY:
                category(m, u);
                return;
            case BASKET:
                confirmOrder(u);
                return;
            case USER_INFO:
                userInfo(m, u);
                return;
            case USERNAME:
                u.setUsername(m.text());
                break;
            case PASSWORD:
                u.setPassword(userService.encryptPassword(m.text()));
                break;
            case NAME:
                u.setfName(m.text());
                break;
            case SURNAME:
                u.setlName(m.text());
                break;
            case COUNTRY:
                u.setCountry(m.text());
                break;
            case CITY:
                u.setCity(m.text());
                break;
            case STREET:
                u.setStreet(m.text());
                break;
            case GENDER:
                try {
                    u.setGender(Gender.valueOf(m.text()));
                } catch (Exception ex) {
                    bot.execute(new SendMessage(u.getChatId(), "Wrong value"));
                }
                break;
            case HOUSE:
                try {
                    u.setHousenumber(Integer.parseInt(m.text()));
                } catch (Exception e) {
                    bot.execute(new SendMessage(u.getChatId(),
                            "Wrong value. Please try again"));
                }
                break;
            case DATE_OF_BIRTH:
                SimpleDateFormat format1 = new SimpleDateFormat("dd,MM,yyyy");
                SimpleDateFormat format2 = new SimpleDateFormat("dd-MM-yyyy");
                SimpleDateFormat format3 = new SimpleDateFormat("dd/MM/yyyy");
                Date date = null;
                try {
                    date = format1.parse(m.text());
                } catch (Exception e1) {
                    try {
                        date = format2.parse(m.text());
                    } catch (Exception e2) {
                        try {
                            date = format3.parse(m.text());
                        } catch (Exception e3) {
                            bot.execute(new SendMessage(u.getChatId(),
                                    "Wrong value. Please try again"));
                        }
                    }
                }
                if (date != null) {
                    u.setDateOfBirth(date);
                }
                break;
            default:

        }
        u.setBotState(BotState.USER_INFO);
        userService.save(u);
        bot.execute(new SendMessage(u.getChatId(),
                "Please, choose which of this you want to change")
                .replyMarkup(keyboards.infoKeyboard(u)));
    }


    void confirmOrder(User u) {
        if (u.getCountry() == null || u.getCity() == null || u.getStreet() == null || u.getHousenumber() == null) {
            bot.execute(new SendMessage(u.getChatId(), "Firstly you must add a shipping data to your profile.\n You can do this at \"My Info\""));
            return;
        }
        if (orderService.findByOrderState_TitleAndUser_IdUser("NOT_PAID", u.getIdUser()).isPresent()) {
            bot.execute(new SendMessage(u.getChatId(), "Firstly you must pay for your last order"));
            Order order = orderService.findByOrderState_TitleAndUser_IdUser("NOT_PAID", u.getIdUser()).get();
            sendInvoice(order, u);
            return;
        }
        if (u.getBasket() == null || u.getBasket().getProductsInBasket().isEmpty()) {
            bot.execute(new SendMessage(u.getChatId(), "You don`t have any products in your basket\nFirstly add some products to basket"));
            return;
        }
        //transfer from basket to order table
        Order order = new Order();
        Integer cost = 0;
        if (orderStateService.findByTitle("NOT_PAID").isPresent()) {
            order.setOrderState(orderStateService.findByTitle("NOT_PAID").get());
            order.setUser(u);
            orderService.save(order);
            List<ProductInOrder> prodInOrder = new ArrayList<>();

            for (ProductInBasket item : u.getBasket().getProductsInBasket()) {
                ProductInOrder productInOrder = new ProductInOrder(item.getProduct(), order, item.getCount());
                prodInOrder.add(productInOrder);
                try {
                    productInOrderService.save(productInOrder);
                    cost += productInOrder.getProduct().getPrice() * productInOrder.getCount();
                } catch (Exception e) {
                    e.printStackTrace();
                    orderService.safeDelete(order);
                    return;
                }
            }
            order.setProductInOrders(prodInOrder);
            order.setCost(cost - (cost * u.getUserRole().getDiscount() / 100));
            orderService.save(order);
        }
        //deleting basket, becouse products in order table now
        basketService.delete(u.getBasket());
        sendInvoice(order, u);
    }

    private void sendInvoice(Order order, User u) {
        SendInvoice sendInvoice = new SendInvoice(
                u.getIdUser(),
                "Order # " + order.getIdOrder(),
                "List of the product:\n" + orderInfoToString(order) + "\nYour discount: " + u.getUserRole().getDiscount() + "%",
                "BestBotMarketPayload",
                "284685063:TEST:MDJlMjI1Yjc0YjRj",
                "my_start_param",
                "USD",
                new LabeledPrice(
                        "Order # " + order.getIdOrder(),
                        order.getCost()))
                .replyMarkup(new InlineKeyboardMarkup(new InlineKeyboardButton[]{
                        new InlineKeyboardButton("Test pay").pay()
                }));
        SendResponse response = bot.execute(sendInvoice);
    }

    private void userInfo(Message m, User u) {
        String text;
        switch (m.text()) {
            case "Name":
                text = "Please, Enter your name\n[Current name is\"" + u.getfName() + "\"]:";
                u.setBotState(BotState.NAME);
                break;
            case "Surname":
                text = "Please, Enter your surname\n[Current surname is\"" + u.getlName() + "\"]:";
                u.setBotState(BotState.SURNAME);
                break;
            case "Gender":
                text = "Please, Enter your gender\n" +
                        "use only MALE, FEMALE, TRANSGENDER or INDEFINITE\n" +
                        "(be careful, it case sensitive)\n" +
                        "[Current gender is \"" + u.getGender() + "\"]:";
                u.setBotState(BotState.GENDER);
                break;
            case "Date of birth":
                text = "Please, Enter your date of birth\n" +
                        "use only one of next formats\n" +
                        "(dd-MM-yyyy, dd,MM,yyyy, dd/MM/yyyy)\n" +
                        "[Current date is \"" + u.getDateOfBirth() + "\"]:";
                u.setBotState(BotState.DATE_OF_BIRTH);
                break;
            case "Country":
                text = "Please, Enter title of your country\n" +
                        "[Current country is \"" + u.getCountry() + "\"]:";
                u.setBotState(BotState.COUNTRY);
                break;
            case "City":
                text = "Please, Enter title of your city\n" +
                        "[Current city is \"" + u.getCity() + "\"]:";
                u.setBotState(BotState.CITY);
                break;
            case "Street":
                text = "Please, Enter title of your street\n" +
                        "[Current street is \"" + u.getStreet() + "\"]:";
                u.setBotState(BotState.STREET);
                break;
            case "House":
                text = "Please, Enter number of your house\n" +
                        "[Current number is \"" + u.getHousenumber() + "\"]:";
                u.setBotState(BotState.HOUSE);
                break;
            case "Username":
                text = "Please, Enter your username\n" +
                        "[Current username is \"" + u.getUsername() + "\"]:";
                u.setBotState(BotState.USERNAME);
                break;
            case "Password":
                text = "Please, Enter your password:";
                u.setBotState(BotState.PASSWORD);
                break;
            default:
                text = "Please, choose which of this you want to change";
                u.setBotState(BotState.USER_INFO);
                userService.save(u);
                bot.execute(new SendMessage(u.getChatId(), text).replyMarkup(keyboards.infoKeyboard(u)));
                return;
        }
        userService.save(u);
        bot.execute(new SendMessage(u.getChatId(), text).replyMarkup(keyboards.backToMainMenuKeyboard()));
    }

    private void category(Message m, User u) {
        if (u.getCountry() == null || u.getCity() == null || u.getStreet() == null || u.getHousenumber() == null) {
            bot.execute(new SendMessage(u.getChatId(), "Firstly you must add a shipping data to your profile.\n You can do this at \"My Info\""));
            return;
        }
        if(!GoogleApiUtils.checkAddress(u)){
            bot.execute(new SendMessage(u.getChatId(), "Sorry, but you enter wrong address, maybe you just make typo.\n You can change this at \"My Info\""));
            return;
        }
        bot.execute(new SendMessage(u.getChatId(), "What do you want to buy?").replyMarkup(keyboards.backToMainMenuKeyboard()));
        sendProductList(u, m);
        u.setBotState(BotState.PRODUCTS);
        userService.save(u);
    }

    private void mainMenu(Message m, User u) {
        ReplyKeyboardMarkup keyboard;
        String text;
        switch (m.text()) {
            case "Categories":
                text = "Please, choose the category...";
                u.setBotState(BotState.CATEGORY);
                keyboard = keyboards.categoryKeyboard();
                break;
            case "About market":
                text = "You can see detail information via link below\n\nLink: " + BotMarket.currentAppUrl + "about";
                u.setBotState(BotState.ABOUT_MARKET);
                keyboard = keyboards.backToMainMenuKeyboard();
                break;
            case "Basket":
                text = "You can confirm your order or delete some products from order and confirm";
                sendBasketList(u);
                u.setBotState(BotState.BASKET);
                keyboard = keyboards.basketKeyboard(u);
                break;
            case "Orders":
                text = "Your orders situated above";
                sendOrdersList(u);
                u.setBotState(BotState.ORDERS);
                keyboard = keyboards.backToMainMenuKeyboard();
                break;
            case "Help":
                text = "You can see how to use bot via link below\n\nLink: " + BotMarket.currentAppUrl + "help";
                u.setBotState(BotState.HELP);
                keyboard = keyboards.backToMainMenuKeyboard();
                break;
            case "My info":
                text = "Please, choose which of this you want to change";
                u.setBotState(BotState.USER_INFO);
                keyboard = keyboards.infoKeyboard(u);
                break;
            default:
                text = "Please, choose any action";
                u.setBotState(BotState.MAIN_MENU);
                keyboard = keyboards.mainKeyboard();
        }
        userService.save(u);
        bot.execute(new SendMessage(u.getChatId(), text).replyMarkup(keyboard));
    }

    private void sendOrdersList(User u) {
        if (orderService.findByUser(u.getIdUser()) != null) {
            ArrayList<Order> list = orderService.findByUser(u.getIdUser());
            for (Order order : list) {
                StringBuilder text = new StringBuilder("Order #" + order.getIdOrder() + "\n");
                text.append(orderInfoToString(order));
                text.append("State: ");
                text.append(order.getOrderState().getTitle());
                text.append("\n\n\nTotalCost: ");
                text.append(((float) order.getCost()) / 100);
                SendMessage sm = new SendMessage(u.getChatId(), text.toString());
                if (order.getOrderState().getTitle().equals("NOT_PAID")) {
                    sm.replyMarkup(keyboards.forOrderList(order));
                }
                bot.execute(sm);
            }
        }
    }

    private void sendProductList(User u, Message m) {
        List<Product> list = (List<Product>) productTypeService.findByTitle(m.text()).getProducts();
        Set<Product> set = new HashSet<>();
        List<Store> stores = GoogleApiUtils.sortedStores(u);
        stores.get(0).getAvailabilities().forEach(av -> {
            if (av.getProduct().getProductType().getTitle().equals(m.text()))
                set.add(av.getProduct());
        });
        for (Product prod : set) {
            String text = prod.getTitle() + "\n" + prod.getDescription() + "\nPrice:" + prod.getPrice().floatValue() / 100;
            SendPhoto sp = new SendPhoto(u.getChatId().toString(), prod.getUrlPhoto());
            sp.caption(text);
            sp.replyMarkup(keyboards.forCategoryProductList(prod, "add"));
            bot.execute(sp);
        }
    }

    private void sendBasketList(User u) {
        if (u.getBasket() != null) {
            u.getBasket().getProductsInBasket().forEach(pib -> {
                SendPhoto sp = new SendPhoto(u.getChatId().toString(), pib.getProduct().getUrlPhoto());
                sp.caption(pib.getProduct().getTitle() + "\n" + pib.getProduct().getDescription() + "\nPrice:" + pib.getProduct().getPrice().floatValue() / 100);
                sp.replyMarkup(keyboards.forBasketProductList(pib.getProduct(), pib.getCount()));
                bot.execute(sp);
            });
        } else {
            bot.execute(new SendMessage(u.getChatId(), "Basket is empty!"));
        }
    }

    private String orderInfoToString(Order order) {
        StringBuilder text = new StringBuilder();
        order.getProductInOrders().forEach(item -> {
            text.append("Product: ");
            text.append(item.getProduct().getTitle());
            text.append("\n");
            text.append("Count: ");
            text.append(item.getCount());
            text.append("\n");
            text.append("Cost: ");
            text.append(((float) item.getCount() * item.getProduct().getPrice()) / 100);
            text.append("\n***\n");
        });
        return text.toString();
    }
}
