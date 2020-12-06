package khpi.cs.se.db.semesterwork.dao;

import khpi.cs.se.db.semesterwork.model.Basket;
import khpi.cs.se.db.semesterwork.model.Product;
import khpi.cs.se.db.semesterwork.model.ProductInBasket;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ProductInBasketRepository extends CrudRepository<ProductInBasket, Integer> {


    Optional<ProductInBasket> findByBasket_IdBasketAndProduct_IdProduct(Integer idBasket, Integer idProduct);
}
