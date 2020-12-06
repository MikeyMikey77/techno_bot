package khpi.cs.se.db.semesterwork.model;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Objects;

@Entity
public class OrderState implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column
    @NotNull
    private Integer idOrderState;

    @Column
    @NotNull
    private String title;

    @Column
    private String  description;

    @OneToMany(mappedBy = "orderState")
    private Collection<Order> orders = new ArrayList<>();

    public OrderState() {
    }

    public OrderState(@NotNull String title, String description) {
        this.title = title;
        this.description = description;
    }

    public Collection<Order> getOrders() {
        return orders;
    }

    public void setOrders(Collection<Order> orders) {
        this.orders = orders;
    }

    public Integer getIdOrderState() {
        return idOrderState;
    }

    public void setIdOrderState(Integer idOrderState) {
        this.idOrderState = idOrderState;
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
        if (!(o instanceof OrderState)) return false;
        OrderState that = (OrderState) o;
        return Objects.equals(getIdOrderState(), that.getIdOrderState()) &&
                Objects.equals(getTitle(), that.getTitle()) &&
                Objects.equals(getDescription(), that.getDescription());
    }

    @Override
    public int hashCode() {

        return Objects.hash(getIdOrderState(), getTitle(), getDescription());
    }
}
