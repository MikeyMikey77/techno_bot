package khpi.cs.se.db.semesterwork.dao;

import khpi.cs.se.db.semesterwork.model.OrderState;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface OrderStateRepository extends CrudRepository<OrderState, Integer> {
    Iterable<OrderState> findAllByOrderByIdOrderState();

    Optional<OrderState> findByTitle(String title);
}
