package khpi.cs.se.db.semesterwork.service;

import khpi.cs.se.db.semesterwork.dao.SupplierRepository;
import khpi.cs.se.db.semesterwork.model.Supplier;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class SupplierService {

    private SupplierRepository repository;
    private SupplyService supplyService;

    @Autowired
    SupplierService(SupplierRepository repository, SupplyService supplyService) {
        this.repository = repository;
        this.supplyService = supplyService;
    }

    public void save(Supplier e) {
        repository.save(e);
    }

    public void safeDalete(Supplier obj) {
        if (obj.getSupplies() != null)
            obj.getSupplies().forEach(x -> supplyService.safeDelete(x));
        repository.delete(obj);
    }

    public Iterable<Supplier> findAllByOrderByIdSupplier() {
        return repository.findAllByOrderByIdSupplier();
    }

    public Optional<Supplier> findById(Integer idSupplier) {
        return repository.findById(idSupplier);
    }

}
