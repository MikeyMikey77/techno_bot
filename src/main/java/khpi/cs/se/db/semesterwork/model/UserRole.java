package khpi.cs.se.db.semesterwork.model;

import javax.persistence.*;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Objects;

@Entity
public class UserRole {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Integer idRole;

    @Column
    @NotNull
    private String title;

    @Column
    private String description;

    @Column
    @Max(value = 30)
    @Min(value = 5)
    @NotNull
    private Integer discount;

    @OneToMany(mappedBy = "userRole", fetch = FetchType.LAZY)
    private Collection<User> users = new ArrayList<>();

    public Integer getIdRole() {
        return idRole;
    }

    public void setIdRole(Integer idRole) {
        this.idRole = idRole;
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

    public Collection<User> getUsers() {
        return users;
    }

    public void setUsers(Collection<User> users) {
        this.users = users;
    }

    public Integer getDiscount() {
        return discount;
    }

    public void setDiscount(Integer discount) {
        this.discount = discount;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof UserRole)) return false;
        UserRole userRole = (UserRole) o;
        return Objects.equals(getIdRole(), userRole.getIdRole()) &&
                Objects.equals(getTitle(), userRole.getTitle());
    }

    @Override
    public int hashCode() {

        return Objects.hash(getIdRole(), getTitle());
    }


}
