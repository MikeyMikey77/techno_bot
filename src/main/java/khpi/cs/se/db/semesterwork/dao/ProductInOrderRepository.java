package khpi.cs.se.db.semesterwork.dao;

import khpi.cs.se.db.semesterwork.model.Product;
import khpi.cs.se.db.semesterwork.model.ProductInOrder;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProductInOrderRepository extends CrudRepository<ProductInOrder, Integer> {
    Optional<ProductInOrder> findByProduct_IdProductAndOrder_IdOrder(Integer idProduct, Integer idOrder);

    List<ProductInOrder> findAllByProduct(Product prod);

    List<ProductInOrder> findByOrder_IdOrder(Integer idOrder);
}
