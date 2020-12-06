package khpi.cs.se.db.semesterwork.model;

import org.hibernate.annotations.ColumnDefault;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Objects;

@Entity
public class Supplier implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column
    @NotNull
    private Integer idSupplier;

    @Column
    @NotNull
    private String supplierCompany;

    @OneToMany(mappedBy = "supplier", fetch = FetchType.EAGER)
    private Collection<Supply> supplies= new ArrayList<>();

    public Supplier() {
    }

    public Integer getIdSupplier() {
        return idSupplier;
    }

    public void setIdSupplier(Integer idSupplier) {
        this.idSupplier = idSupplier;
    }

    public String getSupplierCompany() {
        return supplierCompany;
    }

    public void setSupplierCompany(String supplierCompany) {
        this.supplierCompany = supplierCompany;
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
        if (!(o instanceof Supplier)) return false;
        Supplier supplier = (Supplier) o;
        return Objects.equals(getIdSupplier(), supplier.getIdSupplier()) &&
                Objects.equals(getSupplierCompany(), supplier.getSupplierCompany()) &&
                Objects.equals(getSupplies(), supplier.getSupplies());
    }

    @Override
    public int hashCode() {

        return Objects.hash(getIdSupplier(), getSupplierCompany(), getSupplies());
    }
}
