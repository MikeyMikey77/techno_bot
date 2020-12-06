package khpi.cs.se.db.semesterwork.service;

import khpi.cs.se.db.semesterwork.dao.ProductTypeRepository;
import khpi.cs.se.db.semesterwork.model.Product;
import khpi.cs.se.db.semesterwork.model.ProductType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.List;
import java.util.Optional;

@Service
public class ProductTypeService {

    private ProductTypeRepository repository;
    private ProductService productService;

    @Autowired
    ProductTypeService(ProductTypeRepository repository, ProductService productService){
        this.repository = repository;
        this.productService = productService;
    }

//    @PostConstruct
//    void init(){
//        ProductType type = new ProductType();
//        type.setTitle("test category #1");
//        repository.save(type);
//        Product prod = new Product();
//        prod.setTitle("test product #1");
//        prod.setPrice(12000);
//        prod.setProductType(type);
//        productService.save(prod);
//    }

    public void save(ProductType e){
        repository.save(e);
    }

    public Iterable<ProductType> findAll(){
        return repository.findAll();
    }

    public Iterable<ProductType> getByIdProductType() {
        return repository.findByOrderByIdProductType();
    }

    public Optional<ProductType> findByIdProductType(int id){
        return repository.findById(id);
    }

    public void delete(ProductType type) {
        repository.delete(type);
    }

    public void safeDelete(ProductType obj){
        obj.getProducts().forEach(x -> productService.safeDelete(x));
        repository.delete(obj);
    }

    public ProductType findByTitle(String title) {
        return repository.findByTitle(title);
    }
}
