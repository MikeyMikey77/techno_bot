package khpi.cs.se.db.semesterwork.dao;

import khpi.cs.se.db.semesterwork.model.Order;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Repository
public interface OrderRepository extends CrudRepository<Order, Integer> {

    Iterable<Order> findAllByOrderState_IdOrderStateOrderByIdOrder(Integer id);

    ArrayList<Order> findByUser_IdUserOrderByDateOfCreation(Integer idUser);

    Optional<Order> findByOrderState_TitleAndUser_IdUser(String title, Integer idUser);

    Iterable<Order> findAllByOrderByDateOfCreation();

    List<Order> findByOrderState_Title(String title);
}
