package khpi.cs.se.db.semesterwork.service;

import khpi.cs.se.db.semesterwork.dao.ProductInBasketRepository;
import khpi.cs.se.db.semesterwork.model.Basket;
import khpi.cs.se.db.semesterwork.model.Product;
import khpi.cs.se.db.semesterwork.model.ProductInBasket;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class ProductInBasketService {

    private final ProductInBasketRepository repository;

    @Autowired
    public ProductInBasketService(ProductInBasketRepository repository){
        this.repository = repository;
    }

    public void save(ProductInBasket pib){
        repository.save(pib);
    }

    public void delete(ProductInBasket pib){
        repository.delete(pib);
    }

    public void deleteById(Integer id){
        repository.deleteById(id);
    }

    public ProductInBasket findByBasketAndProduct(Basket b, Product p){
        return repository.findByBasket_IdBasketAndProduct_IdProduct(b.getIdBasket(), p.getIdProduct()).get();
    }

    public Optional<ProductInBasket> findByBasket_IdBasketAndByProduct_IdProduct(Integer idBasket, Integer idProduct) {
        return repository.findByBasket_IdBasketAndProduct_IdProduct(idBasket, idProduct);
    }
}
