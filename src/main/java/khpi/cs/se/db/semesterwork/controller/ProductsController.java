package khpi.cs.se.db.semesterwork.controller;

import khpi.cs.se.db.semesterwork.cloudStorage.MyCloudStorage;
import khpi.cs.se.db.semesterwork.model.Product;
import khpi.cs.se.db.semesterwork.model.ProductType;
import khpi.cs.se.db.semesterwork.service.ProductService;
import khpi.cs.se.db.semesterwork.service.ProductTypeService;
import khpi.cs.se.db.semesterwork.utils.CollectionUtils4Controllers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;

@Controller
@RequestMapping("/products")
public class ProductsController {

    @Autowired
    private ProductService productService;

    @Autowired
    private ProductTypeService productTypeService;

    @GetMapping
    public String productTypePage(Model model) {
        Iterable<ProductType> list = productTypeService.getByIdProductType();
        model.addAttribute("productTypes", CollectionUtils4Controllers.groupObjects(list));
        return "categoryProduct";
    }

    @GetMapping(value = "/{id}")
    public String backToProductsPage(@PathVariable("id") Integer id, Model model) {
        Iterable<Product> products = productService.findAllByIdProductType(id);
        model.addAttribute("products", CollectionUtils4Controllers.groupObjects(products));
        return "product";
    }

    @PostMapping(value = "/{id}")
    public String productsPage(@PathVariable("id") Integer id, Model model) {
        Iterable<Product> products = productService.findAllByIdProductType(id);
        model.addAttribute("products", CollectionUtils4Controllers.groupObjects(products));
        return "product";
    }

    @PostMapping(value = "/{id}", params = "update")
    public String updateProdRow(@PathVariable("id") Integer id,
                                @RequestParam("photo") MultipartFile photo,
                                @RequestParam String title,
                                @RequestParam String description,
                                @RequestParam Integer idProduct,
                                @RequestParam Integer price,
                                Model model) {
        Product prod = productService.findByIdProduct(idProduct);
        prod.setTitle(title);
        prod.setDescription(description);
        prod.setPrice(price);
        if (!photo.isEmpty()) {
            try {
                prod.setUrlPhoto(MyCloudStorage.uploadPhoto(photo, "" + title.hashCode() + prod.getIdProduct()));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        productService.save(prod);
        Iterable<Product> products = productService.findAllByIdProductType(id);
        model.addAttribute("products", CollectionUtils4Controllers.groupObjects(products));
        return "product";
    }

    @PostMapping(value = "/{id}", params = "deleteAll")
    public String deleteProdType(@PathVariable("id") Integer id, Model model) {
        ProductType type = productTypeService.findByIdProductType(id).get();
        productService.deleteAll(type.getProducts());
        productTypeService.delete(type);
        Iterable<ProductType> list = productTypeService.getByIdProductType();
        model.addAttribute("productTypes", CollectionUtils4Controllers.groupObjects(list));
        return "categoryProduct";
    }

    @PostMapping(value = "/{id}", params = "delete")
    public String deleteProdRow(@PathVariable("id") Integer id,
                                @ModelAttribute Product prod,
                                Model model) {
        try {
            productService.deleteByIdProduct(prod.getIdProduct());
        } catch(Exception e){
            model.addAttribute("message", "Can`t delete. May be some orders or supply have references to this product.");
            return "error";
        }
        Iterable<Product> products = productService.findAllByIdProductType(id);
        model.addAttribute("products", CollectionUtils4Controllers.groupObjects(products));
        return "product";
    }

    @PostMapping(params = "addCategory")
    public String addCategory(@ModelAttribute ProductType type ,Model model){
        productTypeService.save(type);
        Iterable<ProductType> list = productTypeService.getByIdProductType();
        ((ArrayList<ProductType>)list).
                stream().
                filter(x -> x.getProducts() == null).
                forEach(x -> x.setProducts(new ArrayList<Product>()));
        model.addAttribute("productTypes", CollectionUtils4Controllers.groupObjects(list));
        return "categoryProduct";
    }

    @PostMapping(value = "/{id}", params = "addProduct")
    public String addCategory(@PathVariable Integer id,
                              @ModelAttribute Product prod,
                              @RequestParam("photo") MultipartFile photo,
                              Model model){
        prod.setProductType(productTypeService.findByIdProductType(id).get());
        if (!photo.isEmpty()) {
            try {
                prod.setUrlPhoto(MyCloudStorage.uploadPhoto(photo, "" + prod.getTitle().hashCode() + prod.getIdProduct()));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        productService.save(prod);
        Iterable<Product> list = productService.findAllByIdProductType(id);
        model.addAttribute("products", CollectionUtils4Controllers.groupObjects(list));
        return "product";
    }
}
