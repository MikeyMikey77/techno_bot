package khpi.cs.se.db.semesterwork.service;

import khpi.cs.se.db.semesterwork.dao.UserRepository;
import khpi.cs.se.db.semesterwork.dao.UserRoleRepository;
import khpi.cs.se.db.semesterwork.model.UserRole;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.Optional;

@Service
public class UserRoleService {

    private UserRoleRepository repository;
    private UserRepository userRepository;

    @Autowired
    public UserRoleService(UserRoleRepository repository, UserRepository userRepository){
        this.repository = repository;
        this.userRepository = userRepository;
    }

//    @PostConstruct
//    public void init(){
//        if(repository.count() == 0) {
//            UserRole role1 = new UserRole();
//            role1.setTitle("ROLE_ADMIN");
//            role1.setDiscount(30);
//            repository.save(role1);
//            UserRole role2 = new UserRole();
//            role2.setTitle("ROLE_CUSTOMER10");
//            role2.setDiscount(5);
//            repository.save(role2);
//            UserRole role3 = new UserRole();
//            role3.setTitle("ROLE_CUSTOMER50");
//            role3.setDiscount(10);
//            repository.save(role3);
//            UserRole role4 = new UserRole();
//            role4.setTitle("ROLE_CUSTOMER100");
//            role4.setDiscount(15);
//            repository.save(role4);
//            UserRole role5 = new UserRole();
//            role5.setTitle("ROLE_CUSTOMER500");
//            role5.setDiscount(20);
//            repository.save(role5);
//            UserRole role6 = new UserRole();
//            role6.setTitle("ROLE_CUSTOMER-VIP");
//            role6.setDiscount(25);
//            repository.save(role6);
//        }
//    }

    public boolean isEmpty(){
        if(repository.count() == 0){
            return true;
        }else{
            return false;
        }
    }

    public void save(UserRole role){
        repository.save(role);
    }

    public void safeDelete(UserRole role){
        role.getUsers().forEach(x -> userRepository.delete(x));
        repository.delete(role);
    }

    public Optional<UserRole> findByTitle(String title) {
        return repository.findByTitle(title);
    }

    public Optional<UserRole> findById(Integer id){
        return repository.findById(id);
    }

    public Iterable<UserRole> findAll() {
        return repository.findAllByOrderByIdRole();
    }


    public void delete(UserRole userRole) {
        repository.delete(userRole);
    }
}
