package khpi.cs.se.db.semesterwork.service;

import khpi.cs.se.db.semesterwork.dao.AvailabilityRepository;
import khpi.cs.se.db.semesterwork.model.Availability;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AvailabilityService {

    private AvailabilityRepository repository;

    @Autowired
    AvailabilityService(AvailabilityRepository repository){
        this.repository = repository;
    }

    public Iterable<Availability> findAll(){
        return repository.findAll();
    }


    public void save(Availability av) {
        repository.save(av);
    }

    public void safeDelete(Availability obj){
        repository.delete(obj);
    }

    public Optional<Availability> findById(Integer idAvailability) {
        return repository.findById(idAvailability);
    }

    public Optional<Availability> findByProduct_IdProductAndStore_IdStore(Integer idProduct, Integer idStore){
        return repository.findByProduct_IdProductAndStore_IdStore(idProduct, idStore);
    }
}
