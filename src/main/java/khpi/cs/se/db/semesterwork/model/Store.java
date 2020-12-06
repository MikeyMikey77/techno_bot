package khpi.cs.se.db.semesterwork.model;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;

@Entity
public class Store implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column
    @NotNull
    private Integer idStore;

    @Column
    @NotNull
    private String title;

    @Column
    @NotNull
    private String country;

    @Column
    @NotNull
    private String city;

    @Column
    @NotNull
    private String street;

    @Column
    @NotNull
    private Integer housenumber;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, mappedBy = "store")
    private Collection<Availability> availabilities = new ArrayList<>();

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, mappedBy = "store")
    private Collection<Supply> supplies = new ArrayList<>();

    @OneToMany(mappedBy = "store")
    private List<Order> orders;


    public Store() {
    }

    public Store(@NotNull String title,
                 @NotNull String country,
                 @NotNull String city,
                 @NotNull String street,
                 @NotNull Integer housenumber) {
        this.title = title;
        this.country = country;
        this.city = city;
        this.street = street;
        this.housenumber = housenumber;
    }

    public Integer getIdStore() {
        return idStore;
    }

    public void setIdStore(Integer idStore) {
        this.idStore = idStore;
    }

    public List<Order> getOrders() {
        return orders;
    }

    public void setOrders(List<Order> orders) {
        this.orders = orders;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public Integer getHousenumber() {
        return housenumber;
    }

    public void setHousenumber(Integer housenumber) {
        this.housenumber = housenumber;
    }

    public Collection<Availability> getAvailabilities() {
        return availabilities;
    }

    public void setAvailabilities(Collection<Availability> availabilities) {
        this.availabilities = availabilities;
    }

    public Collection<Supply> getSupplies() {
        return supplies;
    }

    public void setSupplies(Collection<Supply> supplies) {
        this.supplies = supplies;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Store)) return false;
        Store store = (Store) o;
        return Objects.equals(getIdStore(), store.getIdStore()) &&
                Objects.equals(getTitle(), store.getTitle()) &&
                Objects.equals(getCountry(), store.getCountry()) &&
                Objects.equals(getCity(), store.getCity()) &&
                Objects.equals(getStreet(), store.getStreet()) &&
                Objects.equals(getHousenumber(), store.getHousenumber()) &&
                Objects.equals(getAvailabilities(), store.getAvailabilities()) &&
                Objects.equals(getSupplies(), store.getSupplies());
    }

    @Override
    public int hashCode() {

        return Objects.hash(getIdStore(), getTitle(), getCountry(), getCity(), getStreet(), getHousenumber(), getAvailabilities(), getSupplies());
    }

    @Override
    public String toString() {
        return "Store{" +
                "idStore=" + idStore +
                ", title='" + title + '\'' +
                ", country='" + country + '\'' +
                ", city='" + city + '\'' +
                ", street='" + street + '\'' +
                ", housenumber=" + housenumber +
                ", availabilities=" + availabilities +
                ", supplies=" + supplies +
                '}';
    }
}
