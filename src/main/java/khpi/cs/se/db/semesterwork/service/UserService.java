package khpi.cs.se.db.semesterwork.service;

import khpi.cs.se.db.semesterwork.dao.UserRepository;
import khpi.cs.se.db.semesterwork.model.Basket;
import khpi.cs.se.db.semesterwork.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    private UserRepository repository;
    private UserRoleService roleService;


    @Autowired
    UserService(UserRepository repository, UserRoleService roleService){
        this.roleService = roleService;
        this.repository = repository;
    }

    public void save(User obj) {
        if(repository.count() == 0){
            if(roleService.findByTitle("ROLE_ADMIN").isPresent()) {
                obj.setUserRole(roleService.findByTitle("ROLE_ADMIN").get());
                obj.setPassword(encryptPassword("admin"));
                obj.setUsername("admin");
                obj.setActive(true);
            }
        }
        repository.save(obj);
    }

    public Optional<User> findById(Integer id) {
        return repository.findById(id);
    }

    public Iterable<User> findAll() {
        return repository.findAll();
    }

    public Iterable<User> findAllByIdUserRole(Integer id) {
        return repository.findAllByUserRole_IdRoleOrderByIdUser(id);
    }

    public void deleteByIdProduct(Integer idCustomer) {
        repository.deleteById(idCustomer);
    }

    public void safeDelete(User obj){
        repository.delete(obj);
    }

    public List<User> findByUserRole(String role) {
        return repository.findByUserRole_Title(role);
    }

    public String encryptPassword(String rowPassword) {
        return new BCryptPasswordEncoder().encode(rowPassword);
    }


}
