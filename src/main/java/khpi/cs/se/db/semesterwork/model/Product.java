package khpi.cs.se.db.semesterwork.model;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Collection;
import java.util.Objects;

@Entity
public class Product implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column
    @NotNull
    private Integer idProduct;

    @Column
    @NotNull
    private String title;

    @Column(length = 1000)
    private String description;

    @Column
    private String urlPhoto = "http://res.cloudinary.com/student-khpi/image/upload/v1529079774/noimage.jpg";

    @Column
    @NotNull
    private Integer price;

    @ManyToOne
    @JoinColumn(name = "idProductType")
    private ProductType productType;


    @OneToMany(mappedBy = "product", fetch = FetchType.LAZY)
    private Collection<Availability> availabilities;

    @OneToMany(mappedBy = "product", fetch = FetchType.LAZY)
    private Collection<ProductInOrder> productInOrders;

    @OneToMany(mappedBy = "product", fetch = FetchType.LAZY)
    private Collection<Supply> supplies;

    public Product() {
    }

    public Product(@NotNull String title,
                   @NotNull String description,
                   String urlPhoto,
                   ProductType productType,
                   Collection<Availability> availabilities,
                   Collection<ProductInOrder> productInOrders,
                   Collection<Supply> supplies) {
        this.title = title;
        this.description = description;
        this.urlPhoto = urlPhoto;
        this.productType = productType;
        this.availabilities = availabilities;
        this.productInOrders = productInOrders;
        this.supplies = supplies;
    }

    public Integer getIdProduct() {
        return idProduct;
    }

    public void setIdProduct(Integer idProduct) {
        this.idProduct = idProduct;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getUrlPhoto() {
        return urlPhoto;
    }

    public void setUrlPhoto(String urlPhoto) {
        this.urlPhoto = urlPhoto;
    }

    public ProductType getProductType() {
        return productType;
    }

    public void setProductType(ProductType productType) {
        this.productType = productType;
    }

    public Collection<Availability> getAvailabilities() {
        return availabilities;
    }

    public void setAvailabilities(Collection<Availability> availabilities) {
        this.availabilities = availabilities;
    }

    public Collection<ProductInOrder> getProductInOrders() {
        return productInOrders;
    }

    public void setProductInOrders(Collection<ProductInOrder> productInOrders) {
        this.productInOrders = productInOrders;
    }

    public Collection<Supply> getSupplies() {
        return supplies;
    }

    public void setSupplies(Collection<Supply> supplies) {
        this.supplies = supplies;
    }

    public Integer getPrice() {
        return price;
    }

    public void setPrice(Integer price) {
        this.price = price;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Product)) return false;
        Product product = (Product) o;
        return Objects.equals(getIdProduct(), product.getIdProduct()) &&
                Objects.equals(getTitle(), product.getTitle()) &&
                Objects.equals(getDescription(), product.getDescription()) &&
                Objects.equals(getUrlPhoto(), product.getUrlPhoto()) &&
                Objects.equals(getProductType(), product.getProductType()) &&
                Objects.equals(getAvailabilities(), product.getAvailabilities()) &&
                Objects.equals(getProductInOrders(), product.getProductInOrders()) &&
                Objects.equals(getSupplies(), product.getSupplies());
    }

    @Override
    public int hashCode() {

        return Objects.hash(getIdProduct(), getTitle(), getDescription(), getUrlPhoto(), getProductType(), getAvailabilities(), getProductInOrders(), getSupplies());
    }

    @Override
    public String toString() {
        return "Product{" +
                "idProduct=" + idProduct +
                ", title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", urlPhoto='" + urlPhoto + '\'' +
                ", productType=" + productType +
                '}';
    }
}
