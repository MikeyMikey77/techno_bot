package khpi.cs.se.db.semesterwork.model;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Collection;
import java.util.Date;
import java.util.Objects;

@Entity
@Table(name = "ord")
public class Order implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column
    @NotNull
    private Integer idOrder;

    @Column
    @NotNull
    @Temporal(javax.persistence.TemporalType.DATE)
    private Date dateOfCreation = new Date();

    @OneToMany(mappedBy = "order")
    private Collection<ProductInOrder> productInOrders;

    @ManyToOne
    @JoinColumn(name = "idOrderState")
    private OrderState orderState;

    @ManyToOne
    @JoinColumn(name = "idUser")
    private User user;

    @ManyToOne
    @JoinColumn(name = "idStore")
    private Store store;

    @Column
    private Integer cost;

    public Order() {
    }

    public Order(OrderState orderState, Collection<ProductInOrder> productInOrders, User user) {
        this.orderState = orderState;
        this.productInOrders = productInOrders;
        this.user = user;
    }

    public Integer getIdOrder() {
        return idOrder;
    }

    public void setIdOrder(Integer idOrder) {
        this.idOrder = idOrder;
    }

    public Store getStore() {
        return store;
    }

    public void setStore(Store store) {
        this.store = store;
    }

    public Date getDateOfCreation() {
        return dateOfCreation;
    }

    public void setDateOfCreation(Date dateOfCreation) {
        this.dateOfCreation = dateOfCreation;
    }

    public OrderState getOrderState() {
        return orderState;
    }

    public void setOrderState(OrderState orderState) {
        this.orderState = orderState;
    }

    public Collection<ProductInOrder> getProductInOrders() {
        return productInOrders;
    }

    public void setProductInOrders(Collection<ProductInOrder> productInOrders) {
        this.productInOrders = productInOrders;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Integer getCost() {
        return cost;
    }

    public void setCost(Integer cost) {
        this.cost = cost;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Order)) return false;
        Order order = (Order) o;
        return Objects.equals(getIdOrder(), order.getIdOrder()) &&
                Objects.equals(getDateOfCreation(), order.getDateOfCreation()) &&
                Objects.equals(getOrderState(), order.getOrderState()) &&
                Objects.equals(getProductInOrders(), order.getProductInOrders()) &&
                Objects.equals(getUser(), order.getUser());
    }

    @Override
    public int hashCode() {

        return Objects.hash(getIdOrder(), getDateOfCreation(), getOrderState(), getProductInOrders(), getUser());
    }
}
