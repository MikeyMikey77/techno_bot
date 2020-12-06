package khpi.cs.se.db.semesterwork.bot;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.model.PreCheckoutQuery;
import com.pengrad.telegrambot.model.ShippingQuery;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.AnswerPreCheckoutQuery;
import com.pengrad.telegrambot.request.AnswerShippingQuery;
import khpi.cs.se.db.semesterwork.model.Order;
import khpi.cs.se.db.semesterwork.model.OrderState;
import khpi.cs.se.db.semesterwork.model.User;
import khpi.cs.se.db.semesterwork.service.OrderService;
import khpi.cs.se.db.semesterwork.service.OrderStateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class PaymentHandler {

    private final TelegramBot bot;
    private final OrderService orderService;
    private final OrderStateService orderStateService;

    @Autowired
    public PaymentHandler(TelegramBot bot, OrderService orderService, OrderStateService orderStateService){
        this.bot = bot;
        this.orderService = orderService;
        this.orderStateService = orderStateService;
    }

    public void handleUpdate(Update up, User us){
        if(up.shippingQuery() != null){
            handleShipping(up.shippingQuery(), us);
        } else if( up.preCheckoutQuery() != null){
            handlePreCheckout(up.preCheckoutQuery(), us);
        }
    }

    private void handlePreCheckout(PreCheckoutQuery preCheckoutQuery, User us) {
        AnswerPreCheckoutQuery answerPreCheckoutQuery;
        if(!preCheckoutQuery.invoicePayload().equals("BestBotMarketPayload")){
            answerPreCheckoutQuery = new AnswerPreCheckoutQuery(preCheckoutQuery.id(),
                    "Wrong payload");
        } else{
            answerPreCheckoutQuery = new AnswerPreCheckoutQuery(preCheckoutQuery.id());
        }
        Optional<Order> order = orderService.findByOrderState_TitleAndUser_IdUser("NOT_PAID", us.getIdUser());
        if(order.isPresent() && order.get().getCost().equals(preCheckoutQuery.totalAmount())){
            order.get().setOrderState(orderStateService.findByTitle("PAID").get());
            orderService.save(order.get());
        }
        bot.execute(answerPreCheckoutQuery);
    }

    private void handleShipping(ShippingQuery shippingQuery, User us) {
        AnswerShippingQuery answerShippingQuery;
        if(!shippingQuery.invoicePayload().equals("BestBotMarketPayload")){
            answerShippingQuery = new AnswerShippingQuery(shippingQuery.id(),
                    "Wrong Payload");
        } else {
            answerShippingQuery = new AnswerShippingQuery(shippingQuery.id());
        }
        bot.execute(answerShippingQuery);
    }
}
