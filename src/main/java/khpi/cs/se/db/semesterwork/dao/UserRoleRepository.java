package khpi.cs.se.db.semesterwork.dao;

import khpi.cs.se.db.semesterwork.model.UserRole;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRoleRepository extends CrudRepository<UserRole, Integer> {
    Optional<UserRole> findByTitle(String title);

    Iterable<UserRole> findAllByOrderByIdRole();
}
