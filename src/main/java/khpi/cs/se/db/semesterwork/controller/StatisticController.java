package khpi.cs.se.db.semesterwork.controller;

import khpi.cs.se.db.semesterwork.model.Order;
import khpi.cs.se.db.semesterwork.model.Product;
import khpi.cs.se.db.semesterwork.model.ProductInOrder;
import khpi.cs.se.db.semesterwork.model.User;
import khpi.cs.se.db.semesterwork.service.OrderService;
import khpi.cs.se.db.semesterwork.service.ProductInOrderService;
import khpi.cs.se.db.semesterwork.service.ProductService;
import khpi.cs.se.db.semesterwork.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.*;

@Controller
@RequestMapping("/statistic")
public class StatisticController {

    @Autowired
    private OrderService orderService;
    @Autowired
    private ProductService productService;
    @Autowired
    private ProductInOrderService productInOrderService;
    @Autowired
    private UserService userService;


    @GetMapping
    public String statistic(Model model){
        Calendar c = new GregorianCalendar();
        c.setTime(new Date());
        Integer year = c.get(Calendar.YEAR);
        Iterable<Order> orders = orderService.findAllByOrderByDate();
        int[] counts = calculateCount(orders, year);
        model.addAttribute("ordersDataset1", Month.values());
        model.addAttribute("ordersDataset2", counts);


        Map<String, Integer> prodStatistics = createProdMap();
        model.addAttribute("productsDataset1", prodStatistics.keySet());
        model.addAttribute("productsDataset2", prodStatistics.values());


        Map<String, Integer> customerStatictic = createCustomersMap();
        model.addAttribute("customersDataset1", customerStatictic.keySet());
        model.addAttribute("customersDataset2", customerStatictic.values());
        return "chart";
    }

    private Map<String, Integer> createCustomersMap() {
        Iterable<User> users = userService.findAll();
        Map<String, Integer> map = new HashMap<>();
        for(User user: users){
            Collection<Order> orders = user.getOrders();
            int totalCostOfUserOrders = 0;
            for(Order order:orders){
                totalCostOfUserOrders += order.getCost();
            }
            map.put("User id: "+user.getIdUser().toString(), totalCostOfUserOrders);
        }
        return map;
    }

    private Map<String, Integer> createProdMap() {
        Iterable<Product> prods = productService.findAll();
        Map<String, Integer> map = new HashMap<>();
        for(Product prod: prods){
            List<ProductInOrder> pio = productInOrderService.findAllByProduct(prod);
            int count = 0;
            for(ProductInOrder item: pio){
                count += item.getCount();
            }
            map.put(prod.getTitle(), count);
        }
        return map;
    }

    private int[] calculateCount(Iterable<Order> orders, Integer year){
        Calendar calendar = new GregorianCalendar();
        int[] counts = new int[12];
        for (Order ord : orders) {
            calendar.setTime(ord.getDateOfCreation());
            if (year.equals(calendar.get(Calendar.YEAR))){
                    counts[calendar.get(Calendar.MONTH)] += 1;
            }
        }
        return counts;
    }
}
