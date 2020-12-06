package khpi.cs.se.db.semesterwork.model;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Objects;

@Entity
public class Basket implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Integer idBasket;

    @OneToOne(mappedBy = "basket")
    private User user;

    @OneToMany(fetch = FetchType.EAGER, mappedBy = "basket", orphanRemoval = true)
    private Collection<ProductInBasket> productsInBasket = new ArrayList<ProductInBasket>();

    @Column
    private Integer count;

    public User getUser() {
        return user;
    }

    public Basket setUser(User user) {
        this.user = user;
        return this;
    }

    public Collection<ProductInBasket> getProductsInBasket() {
        return productsInBasket;
    }

    public void setProductsInBasket(Collection<ProductInBasket> productsInBasket) {
        this.productsInBasket = productsInBasket;
    }

    public Integer getIdBasket() {
        return idBasket;
    }

    public void setIdBasket(Integer idBasket) {
        this.idBasket = idBasket;
    }

    public Integer getCount() {
        return count;
    }

    public Basket setCount(Integer count) {
        this.count = count;
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Basket basket = (Basket) o;
        return Objects.equals(user, basket.user) &&
                Objects.equals(productsInBasket, basket.productsInBasket) &&
                Objects.equals(count, basket.count);
    }

    @Override
    public int hashCode() {

        return Objects.hash(user, productsInBasket, count);
    }
}
