package khpi.cs.se.db.semesterwork.model;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Objects;

@Entity
public class ProductInBasket implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "idBasket")
    private Basket basket;

    @ManyToOne
    @JoinColumn(name = "idProduct")
    private Product product;

    @Column
    private Integer count;

    public Integer getId() {
        return id;
    }

    public ProductInBasket setId(Integer id) {
        this.id = id;
        return this;
    }

    public Basket getBasket() {
        return basket;
    }

    public ProductInBasket setBasket(Basket basket) {
        this.basket = basket;
        return this;
    }

    public Product getProduct() {
        return product;
    }

    public ProductInBasket setProduct(Product product) {
        this.product = product;
        return this;
    }

    public Integer getCount() {
        return count;
    }

    public ProductInBasket setCount(Integer count) {
        this.count = count;
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ProductInBasket that = (ProductInBasket) o;
        return Objects.equals(id, that.id) &&
                Objects.equals(basket, that.basket) &&
                Objects.equals(product, that.product) &&
                Objects.equals(count, that.count);
    }

    @Override
    public int hashCode() {

        return Objects.hash(id, basket, product, count);
    }
}
