package khpi.cs.se.db.semesterwork.service;

import khpi.cs.se.db.semesterwork.dao.SupplyRepository;
import khpi.cs.se.db.semesterwork.model.Supply;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicBoolean;

@Service
public class SupplyService {

    private SupplyRepository repository;

    @Autowired
    SupplyService(SupplyRepository repository){
        this.repository = repository;
    }

    public void save(Supply e){
        repository.save(e);
    }

    public void safeDelete(Supply obj){
        repository.delete(obj);
    }

    public Optional<Supply> findById(Integer idSupply) {
        return repository.findById(idSupply);
    }

    public List<Supply> findBySupplier_IdSupplier(Integer id) {
        return repository.findBySupplier_IdSupplier(id);
    }
}
