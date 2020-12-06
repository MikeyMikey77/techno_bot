package khpi.cs.se.db.semesterwork.controller;

import khpi.cs.se.db.semesterwork.model.Supplier;
import khpi.cs.se.db.semesterwork.model.Supply;
import khpi.cs.se.db.semesterwork.service.ProductService;
import khpi.cs.se.db.semesterwork.service.StoreService;
import khpi.cs.se.db.semesterwork.service.SupplierService;
import khpi.cs.se.db.semesterwork.service.SupplyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/supplies")
public class SupplyController {

    private SupplyService supplyService;
    private SupplierService supplierService;
    private StoreService storeService;
    private ProductService productService;

    @Autowired
    public SupplyController(SupplyService supplyService, SupplierService supplierService,
                            ProductService productService, StoreService storeService){
        this.supplierService = supplierService;
        this.supplyService = supplyService;
        this.productService = productService;
        this.storeService = storeService;
    }

    @GetMapping
    public String supplierPage(Model model){
        model.addAttribute("suppliers", supplierService.findAllByOrderByIdSupplier());
        return "supplier";
    }

    @PostMapping(params = "update")
    public String updateSupplier(@ModelAttribute Supplier supplier, Model model){
        Supplier temp = supplierService.findById(supplier.getIdSupplier()).get();
        temp.setSupplierCompany(supplier.getSupplierCompany());
        supplierService.save(temp);
        model.addAttribute("suppliers",supplierService.findAllByOrderByIdSupplier());
        return "supplier";
    }

    @PostMapping(params = "delete")
    public String deleteSupplier(@ModelAttribute Supplier supplier, Model model){
        supplier= supplierService.findById(supplier.getIdSupplier()).get();
        supplierService.safeDalete(supplier);
        model.addAttribute("suppliers",supplierService.findAllByOrderByIdSupplier());
        return "supplier";
    }

    @PostMapping(params = "addSupplier")
    public String addSuppliers(@ModelAttribute Supplier supplier, Model model){
        supplierService.save(supplier);
        model.addAttribute("suppliers", supplierService.findAllByOrderByIdSupplier());
        return "supplier";
    }

    @GetMapping(value = "/{id}")
    public String backToSuppliesPage(@PathVariable Integer id, Model model){
        model.addAttribute("products",productService.findAll());
        model.addAttribute("stores", storeService.getAll());
        model.addAttribute("supplies", supplyService.findBySupplier_IdSupplier(id));
        return "supply";
    }

    @PostMapping(value = "/{id}", params = "supplies")
    public String suppliesPage(@PathVariable Integer id, Model model){
        model.addAttribute("products",productService.findAll());
        model.addAttribute("stores", storeService.getAll());
        model.addAttribute("supplies", supplyService.findBySupplier_IdSupplier(id));
        return "supply";
    }

    @PostMapping(value = "/{id}", params = "update")
    public String updateSupply(@PathVariable Integer id, @RequestParam Integer count, @RequestParam Integer storeId,
                               @RequestParam Integer prodId, @RequestParam Integer idSupply, Model model){
        Supply supply = supplyService.findById(idSupply).get();
        supply.setCount(count);
        supply.setSupplier(supplierService.findById(id).get());
        supply.setProduct(productService.findByIdProduct(prodId));
        supply.setStore(storeService.findById(storeId).get());
        supplyService.save(supply);
        model.addAttribute("products",productService.findAll());
        model.addAttribute("stores", storeService.getAll());
        model.addAttribute("supplies", supplyService.findBySupplier_IdSupplier(id));
        return "supply";
    }

    @PostMapping(value = "/{id}", params = "delete")
    public String updateSupply(@PathVariable Integer id, @RequestParam Integer idSupply, Model model){
        Supply supply = supplyService.findById(idSupply).get();
        supplyService.safeDelete(supply);
        model.addAttribute("products",productService.findAll());
        model.addAttribute("stores", storeService.getAll());
        model.addAttribute("supplies", supplyService.findBySupplier_IdSupplier(id));
        return "supply";
    }

    @PostMapping(value = "/{id}", params = "addSupply")
    public String addSupply(@PathVariable Integer id, @RequestParam Integer count, @RequestParam Integer storeId,
                               @RequestParam Integer prodId, Model model){
        Supply supply = new Supply();
        supply.setCount(count);
        supply.setSupplier(supplierService.findById(id).get());
        supply.setProduct(productService.findByIdProduct(prodId));
        supply.setStore(storeService.findById(storeId).get());
        supplyService.save(supply);
        model.addAttribute("products",productService.findAll());
        model.addAttribute("stores", storeService.getAll());
        model.addAttribute("supplies", supplyService.findBySupplier_IdSupplier(id));
        return "supply";
    }
}
