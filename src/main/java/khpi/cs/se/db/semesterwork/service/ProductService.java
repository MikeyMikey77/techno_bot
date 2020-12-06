package khpi.cs.se.db.semesterwork.service;

import khpi.cs.se.db.semesterwork.dao.ProductRepository;
import khpi.cs.se.db.semesterwork.model.Product;
import khpi.cs.se.db.semesterwork.model.ProductType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.Optional;

@Service
public class ProductService {


    private ProductRepository repository;
    private AvailabilityService availabilityService;
    private ProductInOrderService productInOrderService;
    private SupplyService supplyService;

    @Autowired
    ProductService(ProductRepository repository, AvailabilityService availabilityService,
                   ProductInOrderService productInOrderService, SupplyService supplyService){
        this.repository = repository;
        this.availabilityService = availabilityService;
        this.supplyService = supplyService;
        this.productInOrderService = productInOrderService;
    }

    public void save(Product e){
        repository.save(e);
    }

    public Iterable<Product> findAll(){
        return repository.findAll();
    }

    public void delete(Product p){
        repository.delete(p);
    }

    public void deleteByIdProduct(Integer idProduct){
        repository.deleteByIdProduct(idProduct);
    }

    public Product findByIdProduct(Integer id){
        return repository.findByIdProduct(id);
    }

    public Iterable<Product> findAllByIdProductType(Integer id) {
        return repository.findAllByProductType_IdProductTypeOrderByIdProduct(id);
    }

    public void deleteAll(Iterable<Product> prod){
        repository.deleteAll(prod);
    }

    public void safeDelete(Product obj){
        obj.getProductInOrders().forEach(x -> productInOrderService.safeDelete(x));
        obj.getSupplies().forEach(x -> supplyService.safeDelete(x));
        obj.getAvailabilities().forEach(x -> availabilityService.safeDelete(x));
        repository.delete(obj);
    }

    public Optional<Product> findById(int id) {
        return repository.findById(id);
    }
}
