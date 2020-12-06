package khpi.cs.se.db.semesterwork.dao;

import khpi.cs.se.db.semesterwork.model.ProductType;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductTypeRepository extends CrudRepository<ProductType, Integer> {
    Iterable<ProductType> findByOrderByIdProductType();

    ProductType findByTitle(String title);
}
