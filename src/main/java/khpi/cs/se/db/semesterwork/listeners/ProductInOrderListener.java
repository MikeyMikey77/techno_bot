package khpi.cs.se.db.semesterwork.listeners;

import com.google.maps.DirectionsApi;
import com.google.maps.GeoApiContext;
import com.google.maps.model.DirectionsLeg;
import com.google.maps.model.DirectionsRoute;
import khpi.cs.se.db.semesterwork.model.Order;
import khpi.cs.se.db.semesterwork.model.ProductInOrder;
import khpi.cs.se.db.semesterwork.model.Store;
import khpi.cs.se.db.semesterwork.model.User;
import khpi.cs.se.db.semesterwork.service.*;
import khpi.cs.se.db.semesterwork.utils.GoogleApiUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.support.TransactionSynchronizationAdapter;
import org.springframework.transaction.support.TransactionSynchronizationManager;

import javax.persistence.PostPersist;
import javax.persistence.PrePersist;
import javax.persistence.PreRemove;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Component
public class ProductInOrderListener {

    private static StoreService storeService;
    private static ProductInOrderService productInOrderService;
    private static AvailabilityService availabilityService;
    private static OrderService orderService;
    private boolean isExist;
    private static UserRoleService userRoleService;
    private static UserService userService;

    @Autowired
    public void init(StoreService service, ProductInOrderService productInOrder,
                     AvailabilityService avService, OrderService ordService,
                     UserRoleService usrRoleService, UserService usrService) {
        storeService = service;
        productInOrderService = productInOrder;
        availabilityService = avService;
        orderService = ordService;
        userRoleService = usrRoleService;
        userService = usrService;
    }

    @PrePersist
    public void prePersist(ProductInOrder productInOrder) {
        User u = productInOrder.getOrder().getUser();
        ArrayList<Order> userOrders = orderService.findByUser(productInOrder.getOrder().getUser().getIdUser());
        Integer totalCost = 0;
        if (userOrders != null && !userOrders.isEmpty()) {
            for (Order order : userOrders) {
                if (!order.equals(productInOrder.getOrder()))
                    totalCost += order.getCost();
            }
        }
        changeRole(
                productInOrder.getOrder().getUser(),
                totalCost,
                (productInOrder.getProduct().getPrice()*productInOrder.getCount())-(productInOrder.getProduct().getPrice()*productInOrder.getCount()*productInOrder.getOrder().getUser().getUserRole().getDiscount()/100)
        );

        isExist = false;
        if (storeService.getAll() != null && !storeService.getAll().isEmpty()) {
            List<Store> stores = GoogleApiUtils.sortedStores(productInOrder.getOrder().getUser());
            Store store = stores.get(0);
            store.getAvailabilities().forEach(availability -> {
                if (availability.getProduct().equals(productInOrder.getProduct())) {
                    if(availability.getCount() - productInOrder.getCount() > 0) {
                        availability.setCount(availability.getCount() - productInOrder.getCount());
                        productInOrder.getOrder().setStore(store);
                        isExist = true;
                    }
                }
            });
        }
        TransactionSynchronizationManager.registerSynchronization(new TransactionSynchronizationAdapter() {
            @Override
            public void beforeCommit(boolean readOnly) {
                if (!isExist) {
                    throw new RuntimeException("Rollback the inserting order...");
                }
            }
        });
    }

    @PreRemove
    public void preRemove(ProductInOrder productInOrder) {
        Store store = productInOrder.getOrder().getStore();
        store.getAvailabilities().forEach(av -> {
            if (av.getProduct().equals(productInOrder.getProduct())) {
                av.setCount(av.getCount() + productInOrder.getCount());
                availabilityService.save(av);
            }
        });
        ArrayList<Order> userOrders = orderService.findByUser(productInOrder.getOrder().getUser().getIdUser());
        if (userOrders != null && !userOrders.isEmpty()) {
            Integer totalCost = 0;
            for (Order order : userOrders) {
                totalCost += order.getCost();
            }
            changeRole(
                    productInOrder.getOrder().getUser(),
                    totalCost,
                    -(productInOrder.getProduct().getPrice() * productInOrder.getCount()) - (productInOrder.getProduct().getPrice() * productInOrder.getCount() * productInOrder.getOrder().getUser().getUserRole().getDiscount() / 100));
        }

    }



    private void changeRole(User user, Integer totalCost, Integer currentCost) {
        if (user.getUserRole().getTitle().equals("ROLE_ADMIN")) {
            return;
        }
        if (totalCost + currentCost / 100 > 10) {
            if (totalCost + currentCost / 100 > 50) {
                if (totalCost + currentCost / 100 > 100) {
                    if (totalCost + currentCost / 100 > 500) {
                        //do nothing. It is highest role in the system
                    } else {
                        user.setUserRole(userRoleService.findByTitle("ROLE_CUSTOMER500").get());
                    }
                } else {
                    user.setUserRole(userRoleService.findByTitle("ROLE_CUSTOMER100").get());
                }
            } else {
                user.setUserRole(userRoleService.findByTitle("ROLE_CUSTOMER50").get());
            }
        } else {
            user.setUserRole(userRoleService.findByTitle("ROLE_CUSTOMER10").get());
        }
        userService.save(user);
    }


}
