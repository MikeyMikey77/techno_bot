package khpi.cs.se.db.semesterwork.dao;

import khpi.cs.se.db.semesterwork.model.Store;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.Map;

@Repository
public interface StoreRepository extends CrudRepository<Store, Integer>{
    ArrayList<Store> findAllByOrderByIdStore();
}
