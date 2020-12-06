package khpi.cs.se.db.semesterwork.dao;

import khpi.cs.se.db.semesterwork.model.Basket;
import khpi.cs.se.db.semesterwork.model.User;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface BasketRepository extends CrudRepository<Basket, Integer> {

    Optional<Basket> findByUser_IdUser(Integer id);
}
