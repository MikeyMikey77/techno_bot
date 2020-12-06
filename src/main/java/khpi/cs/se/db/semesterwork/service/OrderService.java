package khpi.cs.se.db.semesterwork.service;

import khpi.cs.se.db.semesterwork.dao.OrderRepository;
import khpi.cs.se.db.semesterwork.model.Order;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class OrderService {

    private OrderRepository repository;
    private ProductInOrderService productInOrderService;

    @Autowired
    OrderService(OrderRepository repository, ProductInOrderService productInOrderService){
        this.productInOrderService = productInOrderService;
        this.repository = repository;
    }

    public void save(Order e){
        repository.save(e);
    }

    public Iterable<Order> findAll(){
        return repository.findAll();
    }

    public Iterable<Order> findByIdOrderState(Integer id){
        return repository.findAllByOrderState_IdOrderStateOrderByIdOrder(id);
    }

    public void safeDelete(Order obj){
        if(obj.getProductInOrders() != null) {
            obj.getProductInOrders().forEach(x -> productInOrderService.safeDelete(x));
        }
        repository.delete(obj);
    }

    public ArrayList<Order> findByUser(Integer idUser) {
        return repository.findByUser_IdUserOrderByDateOfCreation(idUser);
    }

    public Optional<Order> findByOrderState_TitleAndUser_IdUser(String title, Integer idUser){
        return repository.findByOrderState_TitleAndUser_IdUser(title, idUser);
    }

    public Order findById(int idOrder) {
        return repository.findById(idOrder).get();
    }

    public Iterable<Order> findAllByOrderByDate() {
        return repository.findAllByOrderByDateOfCreation();
    }

    public List<Order> findByOrderState_Title(String title) {
        return repository.findByOrderState_Title(title);
    }
}
