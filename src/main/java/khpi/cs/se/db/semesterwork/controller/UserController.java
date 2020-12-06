package khpi.cs.se.db.semesterwork.controller;

import khpi.cs.se.db.semesterwork.model.ProductInOrder;
import khpi.cs.se.db.semesterwork.model.User;
import khpi.cs.se.db.semesterwork.model.UserRole;
import khpi.cs.se.db.semesterwork.service.OrderService;
import khpi.cs.se.db.semesterwork.service.ProductInOrderService;
import khpi.cs.se.db.semesterwork.service.UserRoleService;
import khpi.cs.se.db.semesterwork.service.UserService;
import khpi.cs.se.db.semesterwork.utils.CollectionUtils4Controllers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/users")
public class UserController {

    private UserService userService;
    private UserRoleService userRoleService;
    private OrderService orderService;
    private ProductInOrderService productInOrderService;

    @Autowired
    UserController(UserService userService, UserRoleService userRoleService,
                   OrderService orderService, ProductInOrderService productInOrderService){
        this.userService = userService;
        this.userRoleService = userRoleService;
        this.orderService = orderService;
        this.productInOrderService = productInOrderService;
    }

    @GetMapping
    public String userRolePage(Model model) {
        Iterable<UserRole> list = userRoleService.findAll();
        model.addAttribute("customerRoles", CollectionUtils4Controllers.groupObjects(list));
        return "userRole";
    }


    @PostMapping(params = {"update"})
    public String userRoleUpdate(Model model, @ModelAttribute UserRole userRole){
        userRoleService.save(userRole);
        Iterable<UserRole> list = userRoleService.findAll();
        model.addAttribute("customerRoles", CollectionUtils4Controllers.groupObjects(list));
        return "userRole";
    }

    @PostMapping(params = {"add"})
    public String userRoleAdd(Model model, @ModelAttribute UserRole userRole){
        userRoleService.save(userRole);
        Iterable<UserRole> list = userRoleService.findAll();
        model.addAttribute("customerRoles", CollectionUtils4Controllers.groupObjects(list));
        return "userRole";
    }

    @PostMapping(params = "delete")
    public String userRoleDelete(Model model, @ModelAttribute UserRole userRole){
        userRoleService.delete(userRole);
        Iterable<UserRole> list = userRoleService.findAll();
        model.addAttribute("customerRoles", CollectionUtils4Controllers.groupObjects(list));
        return "userRole";
    }


    @GetMapping(value = "/{idRole}")
    public String backToUsersPage(@PathVariable("idRole") Integer id, Model model) {
        Iterable<User> users = userRoleService.findById(id).get().getUsers();
        model.addAttribute("users", CollectionUtils4Controllers.groupObjects(users));
        return "user";
    }

    @GetMapping(value = "/{idRole}/{idUser}")
    public String backToOrders(@PathVariable Integer idRole, @PathVariable Integer idUser, Model model){
        model.addAttribute("orders", CollectionUtils4Controllers.groupObjects(userService.findById(idUser).get().getOrders()));
        return "order";
    }

    @PostMapping(value = "/{idRole}/{idUser}", params = "products")
    public String productList(@PathVariable Integer idRole,
                              @PathVariable Integer idUser,
                              @RequestParam Integer idOrder,
                              Model model){
        List<ProductInOrder> list = productInOrderService.findByOrder_IdOrder(idOrder);
        model.addAttribute("prodInOrder", CollectionUtils4Controllers.groupObjects(list));
        return "prodInOrder";
    }

}
