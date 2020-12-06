package khpi.cs.se.db.semesterwork.model;

import khpi.cs.se.db.semesterwork.listeners.AvailabilityListener;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Objects;

@Entity
@EntityListeners(AvailabilityListener.class)
public class Availability implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column
    @NotNull
    private Integer idAvailability;

    @ManyToOne
    @JoinColumn(name = "idProduct")
    private Product product;

    @ManyToOne
    @JoinColumn(name = "idStore")
    private Store store;

    @Column
    @NotNull
    private Integer count;

    public Availability() {
    }

    public Availability(@NotNull Integer idAvailability, Product product, Store store) {
        this.idAvailability = idAvailability;
        this.product = product;
        this.store = store;
    }

    public Integer getIdAvailability() {
        return idAvailability;
    }

    public void setIdAvailability(Integer idAvailability) {
        this.idAvailability = idAvailability;
    }

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public Store getStore() {
        return store;
    }

    public void setStore(Store store) {
        this.store = store;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Availability)) return false;
        Availability that = (Availability) o;
        return Objects.equals(getIdAvailability(), that.getIdAvailability()) &&
                Objects.equals(getProduct(), that.getProduct()) &&
                Objects.equals(getStore(), that.getStore());
    }

    @Override
    public int hashCode() {

        return Objects.hash(getIdAvailability(), getProduct(), getStore());
    }

    @Override
    public String toString() {
        return "Availability{" +
                "idAvailability=" + idAvailability +
                ", product=" + product +
                ", store=" + store +
                ", count=" + count +
                '}';
    }
}
