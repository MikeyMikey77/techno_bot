package khpi.cs.se.db.semesterwork.controller;


import khpi.cs.se.db.semesterwork.model.Order;
import khpi.cs.se.db.semesterwork.model.OrderState;
import khpi.cs.se.db.semesterwork.service.OrderService;
import khpi.cs.se.db.semesterwork.service.OrderStateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/orders")
public class OrderController {

    private OrderService orderService;
    private OrderStateService orderStateService;

    @Autowired
    public OrderController(OrderService orderService,
                           OrderStateService orderStateService){
        this.orderService = orderService;
        this.orderStateService = orderStateService;
    }

    @GetMapping
    public String get(Model model){
        model.addAttribute("orders",orderService.findByOrderState_Title("PAID"));
        return "orders";
    }

    @PostMapping
    public String post(Model model,
                       @RequestParam Integer idOrder){
        Order order = orderService.findById(idOrder);
        OrderState state = orderStateService.findByTitle("COMPLETED").get();
        order.setOrderState(state);
        orderService.save(order);
        return get(model);
    }
}
