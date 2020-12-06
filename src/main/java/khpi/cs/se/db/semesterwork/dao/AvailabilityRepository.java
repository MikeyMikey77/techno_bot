package khpi.cs.se.db.semesterwork.dao;

import khpi.cs.se.db.semesterwork.model.Availability;
import khpi.cs.se.db.semesterwork.model.Store;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AvailabilityRepository extends CrudRepository<Availability, Integer> {

    Iterable<Availability> findAllByStore(Store store);

    Optional<Availability> findByProduct_IdProductAndStore_IdStore(Integer idProduct, Integer idStore);
}
