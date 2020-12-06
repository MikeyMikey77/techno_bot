package khpi.cs.se.db.semesterwork.service;

import khpi.cs.se.db.semesterwork.dao.ProductInOrderRepository;
import khpi.cs.se.db.semesterwork.model.Product;
import khpi.cs.se.db.semesterwork.model.ProductInOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProductInOrderService {

    private ProductInOrderRepository repository;

    @Autowired
    ProductInOrderService(ProductInOrderRepository repository){
        this.repository = repository;
    }

    public void save(ProductInOrder obj){
        repository.save(obj);
    }

    public void safeDelete(ProductInOrder obj){
        repository.delete(obj);
    }

    public Optional<ProductInOrder> findByProduct_IdProductAndOrder_IdOrder(Integer idProduct, Integer idOrder) {
        return repository.findByProduct_IdProductAndOrder_IdOrder(idProduct, idOrder);
    }

    public List<ProductInOrder> findAllByProduct(Product prod) {
        return repository.findAllByProduct(prod);
    }

    public List<ProductInOrder> findByOrder_IdOrder(Integer idOrder) {
        return repository.findByOrder_IdOrder(idOrder);
    }
}
