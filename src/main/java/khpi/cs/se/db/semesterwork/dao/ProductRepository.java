package khpi.cs.se.db.semesterwork.dao;

import khpi.cs.se.db.semesterwork.model.Product;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;

@Repository
public interface ProductRepository extends CrudRepository<Product,Integer> {
    @Transactional
    Long deleteByIdProduct(Integer idProduct);

    Iterable<Product> findAllByProductType_IdProductTypeOrderByIdProduct(Integer id);

    Product findByIdProduct(Integer idProduct);
}
