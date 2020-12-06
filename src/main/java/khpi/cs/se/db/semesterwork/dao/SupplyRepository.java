package khpi.cs.se.db.semesterwork.dao;

import khpi.cs.se.db.semesterwork.model.Supply;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SupplyRepository extends CrudRepository<Supply, Integer> {
    List<Supply> findBySupplier_IdSupplier(Integer id);
}
