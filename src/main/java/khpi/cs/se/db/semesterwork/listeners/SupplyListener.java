package khpi.cs.se.db.semesterwork.listeners;

import khpi.cs.se.db.semesterwork.model.Availability;
import khpi.cs.se.db.semesterwork.model.Supply;
import khpi.cs.se.db.semesterwork.service.AvailabilityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.persistence.PostPersist;
import javax.persistence.PrePersist;

@Component
public class SupplyListener {

    private static AvailabilityService availabilityService;

    @Autowired
    public void init(AvailabilityService service){
        availabilityService = service;
    }

    @PrePersist
    public void postPersist(Supply supply){
        boolean isExist = false;
        for (Availability item  : supply.getStore().getAvailabilities()) {
            if(item.getProduct().equals(supply.getProduct())){
                item = availabilityService.findById(item.getIdAvailability()).get();
                item.setCount(item.getCount()+supply.getCount());
                isExist = true;
                availabilityService.save(item);
            }
        }
        if(!isExist){
            Availability availability = new Availability();
            availability.setStore(supply.getStore());
            availability.setProduct(supply.getProduct());
            availability.setCount(supply.getCount());
            availabilityService.save(availability);
        }
    }
}
