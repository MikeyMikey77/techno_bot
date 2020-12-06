package khpi.cs.se.db.semesterwork.model;

import khpi.cs.se.db.semesterwork.listeners.ProductInOrderListener;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Objects;

@Entity
@EntityListeners(ProductInOrderListener.class)
public class ProductInOrder implements Serializable {

    @Id
    @Column
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @NotNull
    private Integer idProductInOrder;


    @ManyToOne
    @JoinColumn(name = "idProduct")
    private Product product;

    @ManyToOne
    @JoinColumn(name = "idOrder")
    private Order order;

    @Column
    @NotNull
    private int count;

    public ProductInOrder() {
    }

    public ProductInOrder(Product product, Order order, int count) {
        this.product = product;
        this.order = order;
        this.count = count;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public Integer getIdProductInOrder() {
        return idProductInOrder;
    }

    public void setIdProductInOrder(Integer idProductInOrder) {
        this.idProductInOrder = idProductInOrder;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public Order getOrder() {
        return order;
    }

    public void setOrder(Order order) {
        this.order = order;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ProductInOrder)) return false;
        ProductInOrder that = (ProductInOrder) o;
        return Objects.equals(getIdProductInOrder(), that.getIdProductInOrder()) &&
                Objects.equals(getProduct(), that.getProduct()) &&
                Objects.equals(getOrder(), that.getOrder());
    }

    @Override
    public int hashCode() {

        return Objects.hash(getIdProductInOrder(), getProduct(), getOrder());
    }
}
