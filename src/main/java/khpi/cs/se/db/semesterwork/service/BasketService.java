package khpi.cs.se.db.semesterwork.service;

import khpi.cs.se.db.semesterwork.dao.BasketRepository;
import khpi.cs.se.db.semesterwork.model.Basket;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class BasketService {

    private final BasketRepository repository;
    private final UserService userService;

    @Autowired
    public BasketService(BasketRepository repository, UserService userService) {
        this.repository = repository;
        this.userService = userService;
    }

//    public Basket findByUser(User user) {
//        Basket basket;
//        if (repository.findByUser_IdUser(user.getIdUser()).isPresent()) {
//            return repository.findByUser_IdUser(user.getIdUser()).get();
//        } else {
//            basket = new Basket();
//            basket.setUser(userService.findById(user.getIdUser()).get());
//            repository.save(basket);
//        }
//        return basket;
//    }
//
//    public List<Product> listProductByUser(User user){
//        Basket b = findByUser(user);
//        List<Product> list = new ArrayList<>();
//        if(b.getProductsInBasket() != null) {
//            b.getProductsInBasket().forEach(x -> list.add(x.getProduct()));
//        }
//        return list;
//    }

    public void delete(Basket basket) {
        basket.getUser().setBasket(null);
        userService.save(basket.getUser());
        repository.delete(basket);
    }

    public void save(Basket b) {
        repository.save(b);
    }
}
