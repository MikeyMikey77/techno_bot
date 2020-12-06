package khpi.cs.se.db.semesterwork.service;

import khpi.cs.se.db.semesterwork.dao.StoreRepository;
import khpi.cs.se.db.semesterwork.model.Store;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Optional;

@Service
public class StoreService {

    private StoreRepository repository;
    private SupplyService supplyService;
    private AvailabilityService availabilityService;

    public StoreService(){}

    @Autowired
    public void init(StoreRepository repository, SupplyService supplyService,
                 AvailabilityService availabilityService){
        this.repository = repository;
        this.supplyService = supplyService;
        this.availabilityService = availabilityService;
    }

    public void save(Store e){
        repository.save(e);
    }

    public ArrayList<Store> getAll(){
        if(repository.findAllByOrderByIdStore() != null)
            return repository.findAllByOrderByIdStore();
        else
            return new ArrayList<Store>();
    }

    public void delete(Store s){
        repository.delete(s);
    }

    public Optional<Store> findById(Integer id){
            return repository.findById(id);
    }

    public void deleteAll(Iterable<Store> stores){
        repository.deleteAll(stores);
    }

    public void safeDelete(Store obj){
        obj.getAvailabilities().forEach(x -> availabilityService.safeDelete(x));
        obj.getSupplies().forEach(x -> supplyService.safeDelete(x));
        repository.delete(obj);
    }
}
