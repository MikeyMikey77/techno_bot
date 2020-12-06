package khpi.cs.se.db.semesterwork.dao;

import khpi.cs.se.db.semesterwork.model.User;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRepository extends CrudRepository<User, Integer> {
    Iterable<User> findAllByUserRole_IdRoleOrderByIdUser(Integer id);

    List<User> findByUserRole_Title(String role);
}
