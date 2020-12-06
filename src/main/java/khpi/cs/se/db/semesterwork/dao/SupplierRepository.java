package khpi.cs.se.db.semesterwork.dao;

import khpi.cs.se.db.semesterwork.model.Supplier;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SupplierRepository extends CrudRepository<Supplier, Integer> {
    Iterable<Supplier> findAllByOrderByIdSupplier();
}
