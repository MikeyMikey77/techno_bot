package khpi.cs.se.db.semesterwork.service;

import khpi.cs.se.db.semesterwork.dao.OrderStateRepository;
import khpi.cs.se.db.semesterwork.model.OrderState;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.List;
import java.util.Optional;

@Service
public class OrderStateService {

    private OrderStateRepository repository;
    private OrderService orderService;


    @Autowired
    OrderStateService(OrderStateRepository repository, OrderService orderService){
        this.repository = repository;
        this.orderService = orderService;
    }

    @PostConstruct
    private void init(){
        if(repository.findAll() == null ||  ((List<OrderState>)repository.findAll()).size()  == 0) {
            OrderState notPaid = new OrderState("NOT_PAID", "Someone make order, but don`t paid for it");
            OrderState paid = new OrderState("PAID", "Someone make order and paid for it");
            OrderState completed = new OrderState("COMPLETED", "Products sent to customer");
            repository.save(notPaid);
            repository.save(paid);
            repository.save(completed);
        }
    }

    public void save(OrderState e){
        repository.save(e);
    }

    public Iterable<OrderState> findAll(){
        return repository.findAll();
    }

    public void safeDelete(OrderState obj){
        obj.getOrders().forEach(x -> orderService.safeDelete(x));
        repository.delete(obj);
    }

    public Optional<OrderState> findById(Integer id) {
        return repository.findById(id);
    }

    public Iterable<OrderState> findAllByOrderById() {
        return repository.findAllByOrderByIdOrderState();
    }

    public Optional<OrderState> findByTitle(String title){
        return repository.findByTitle(title);
    }
}
