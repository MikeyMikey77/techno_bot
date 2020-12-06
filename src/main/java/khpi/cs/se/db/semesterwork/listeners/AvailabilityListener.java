package khpi.cs.se.db.semesterwork.listeners;

import khpi.cs.se.db.semesterwork.model.Availability;
import khpi.cs.se.db.semesterwork.service.AvailabilityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.persistence.PrePersist;
import java.util.Optional;

@Component
public class AvailabilityListener {

    private static AvailabilityService availabilityService;

    @Autowired
    public void init(AvailabilityService service) {
        availabilityService = service;
    }

    @PrePersist
    public void prePersist(Availability availability) {
        Optional<Availability> av = availabilityService.findByProduct_IdProductAndStore_IdStore(
                availability.getProduct().getIdProduct(), availability.getStore().getIdStore());
        if(av.isPresent()){
            availability.setIdAvailability(av.get().getIdAvailability());
            availability.setCount(availability.getCount()+av.get().getCount());
        }
    }


}
