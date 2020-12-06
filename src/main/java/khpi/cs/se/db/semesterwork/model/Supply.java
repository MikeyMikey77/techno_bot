package khpi.cs.se.db.semesterwork.model;

import khpi.cs.se.db.semesterwork.listeners.SupplyListener;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Date;
import java.util.Objects;

@Entity
@EntityListeners(SupplyListener.class)
public class Supply implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column
    @NotNull
    private Integer idSupply;

    @Temporal(javax.persistence.TemporalType.DATE)
    @Column
    @NotNull
    private Date dateOfCreation = new Date();

    @Column
    @NotNull
    private Integer count;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "idStore")
    private Store store;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "idProduct")
    private Product product;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "idSupplier")
    private Supplier supplier;

    public Supply() {
    }

    public Supply(@NotNull Integer count, @NotNull Store store, @NotNull Product product, @NotNull Supplier supplier) {
        this.count = count;
        this.store = store;
        this.product = product;
        this.supplier = supplier;
    }

    public Integer getIdSupply() {
        return idSupply;
    }

    public void setIdSupply(Integer idSupply) {
        this.idSupply = idSupply;
    }

    public Date getDateOfCreation() {
        return dateOfCreation;
    }

    public void setDateOfCreation(Date dateOfCreation) {
        this.dateOfCreation = dateOfCreation;
    }

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }

    public Store getStore() {
        return store;
    }

    public void setStore(Store store) {
        this.store = store;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public Supplier getSupplier() {
        return supplier;
    }

    public void setSupplier(Supplier supplier) {
        this.supplier = supplier;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Supply)) return false;
        Supply supply = (Supply) o;
        return Objects.equals(getIdSupply(), supply.getIdSupply()) &&
                Objects.equals(getDateOfCreation(), supply.getDateOfCreation()) &&
                Objects.equals(getCount(), supply.getCount()) &&
                Objects.equals(getStore(), supply.getStore()) &&
                Objects.equals(getProduct(), supply.getProduct()) &&
                Objects.equals(getSupplier(), supply.getSupplier());
    }

    @Override
    public int hashCode() {

        return Objects.hash(getIdSupply(), getDateOfCreation(), getCount(), getStore(), getProduct(), getSupplier());
    }
}
