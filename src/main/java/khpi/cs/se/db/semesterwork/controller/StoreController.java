package khpi.cs.se.db.semesterwork.controller;

import khpi.cs.se.db.semesterwork.cloudStorage.MyCloudStorage;
import khpi.cs.se.db.semesterwork.model.Availability;
import khpi.cs.se.db.semesterwork.model.Product;
import khpi.cs.se.db.semesterwork.model.Store;
import khpi.cs.se.db.semesterwork.service.AvailabilityService;
import khpi.cs.se.db.semesterwork.service.ProductService;
import khpi.cs.se.db.semesterwork.service.StoreService;
import khpi.cs.se.db.semesterwork.utils.CollectionUtils4Controllers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@Controller
@RequestMapping("/stores")
public class StoreController {

    private StoreService storeService;
    private ProductService productService;
    private AvailabilityService availabilityService;

    @Autowired
    StoreController(StoreService storeService, ProductService productService, AvailabilityService availabilityService){
        this.productService = productService;
        this.storeService = storeService;
        this.availabilityService = availabilityService;
    }

    @GetMapping()
    public String tablePage(Model model) {
        model.addAttribute("data", storeService.getAll());
        return "store";
    }

    @PostMapping(params = "update")
    public String updateRow(@ModelAttribute Store store, Model model) {
        storeService.save(store);
        model.addAttribute("data", storeService.getAll());
        return "store";
    }

    @PostMapping(params = "delete")
    public String deleteRow(@ModelAttribute Store store, Model model) {
        store = storeService.findById(store.getIdStore()).get();
        storeService.safeDelete(store);
        model.addAttribute("data", storeService.getAll());
        return "store";
    }

    @PostMapping(params = "addStore")
    public String addRow(@ModelAttribute Store store, Model model) {
        storeService.save(store);
        model.addAttribute("data", storeService.getAll());
        return "store";
    }

    @GetMapping("/{id}")
    public String availabilities(@PathVariable Integer id, Model model){
        model.addAttribute("availabilities", CollectionUtils4Controllers.groupObjects(storeService.findById(id).get().getAvailabilities()));
        return "availableProd";
    }

    @PostMapping(value = "/{id}", params = "updateProd")
    public String update(@PathVariable("id") Integer id,@RequestParam("photo") MultipartFile photo,
                         @RequestParam String title, @RequestParam String description,
                         @RequestParam Integer idProduct,Model model) {

        Product prod = productService.findByIdProduct(idProduct);
        prod.setTitle(title);
        prod.setDescription(description);
        if (!photo.isEmpty()) {
            try {
                prod.setUrlPhoto(MyCloudStorage.uploadPhoto(photo, title + prod.getIdProduct()));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        productService.save(prod);
        model.addAttribute("availabilities",  CollectionUtils4Controllers.groupObjects(storeService.findById(id).get().getAvailabilities()));
        return "availableProd";
    }

    @PostMapping(value = "/{id}", params = "deleteProd")
    public String delete(@PathVariable Integer id, @ModelAttribute Product prod, Model model) {
        prod = productService.findByIdProduct(prod.getIdProduct());
        productService.safeDelete(prod);
        model.addAttribute("availabilities",  CollectionUtils4Controllers.groupObjects(storeService.findById(id).get().getAvailabilities()));
        return "availableProd";
    }

    @GetMapping(value = "/{id}/add_product")
    public String addProd(@PathVariable Integer id, Model model){
        Iterable<Product> prods = productService.findAll();
        model.addAttribute("products", CollectionUtils4Controllers.groupObjects(prods));
        return "addProductToStore";
    }

    @PostMapping(value = "/{id}/add_product", params = "add")
    public String add(@PathVariable("id") Integer id, @RequestParam Integer count,
                      @RequestParam Integer idProduct, Model model) {
        Product prod = productService.findByIdProduct(idProduct);
        Store store = storeService.findById(id).get();
        Availability availability = new Availability();
        availability.setProduct(prod);
        availability.setStore(store);
        availability.setCount(count);
        availabilityService.save(availability);
        model.addAttribute("availabilities",  CollectionUtils4Controllers.groupObjects(storeService.findById(id).get().getAvailabilities()));
        return "availableProd";
    }
}
