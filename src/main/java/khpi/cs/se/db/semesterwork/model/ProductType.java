package khpi.cs.se.db.semesterwork.model;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Objects;

@Entity
public class ProductType implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column
    @NotNull
    private Integer idProductType;

    @Column(length = 20)
    @NotNull
    private String title;

    @Column(length = 1000)
    private String  description;

    @OneToMany(mappedBy = "productType",fetch = FetchType.EAGER)
    private Collection<Product> products = new ArrayList<>();


    public ProductType() {
    }

    public ProductType(@NotNull String title, String description) {
        this.title = title;
        this.description = description;
    }

    public Collection<Product> getProducts() {
        return products;
    }

    public void setProducts(Collection<Product> products) {
        this.products = products;
    }

    public Integer getIdProductType() {
        return idProductType;
    }

    public void setIdProductType(Integer idProductType) {
        this.idProductType = idProductType;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ProductType)) return false;
        ProductType that = (ProductType) o;
        return Objects.equals(getIdProductType(), that.getIdProductType()) &&
                Objects.equals(getTitle(), that.getTitle()) &&
                Objects.equals(getDescription(), that.getDescription());
    }

    @Override
    public int hashCode() {

        return Objects.hash(getIdProductType(), getTitle(), getDescription());
    }

    @Override
    public String toString() {
        return "ProductType{" +
                "idProductType=" + idProductType +
                ", title='" + title + '\'' +
                ", description='" + description + '\'' +
                '}';
    }
}
