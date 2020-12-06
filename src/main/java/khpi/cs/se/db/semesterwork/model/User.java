package khpi.cs.se.db.semesterwork.model;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.Objects;

@Entity
@Table(name = "usr")
public class User implements Serializable {

    @Id
    @Column
    @NotNull
    private Integer idUser;

    @Column
    @NotNull
    private Long chatId;

    @Column
    private String username;

    @Column
    private String password;

    @Column
    private Boolean isActive;

    @Column
    private String fName;

    @Column
    private String lName;

    @Column
    @Temporal(TemporalType.DATE)
    private Date dateOfBirth;

    @Column
    @Enumerated(EnumType.STRING)
    private Gender gender = Gender.INDEFINITE;

    @Column
    @Enumerated(value = EnumType.STRING)
    private BotState botState = BotState.MAIN_MENU;

    @Column
    private String country;

    @Column
    private String city;

    @Column
    private String street;

    @Column
    private Integer housenumber;

    @ManyToOne
    @JoinColumn(name="idUserRole")
    private UserRole userRole;

    @OneToMany(mappedBy = "user")
    private Collection<Order> orders = new ArrayList<>();

    @OneToOne
    @JoinColumn(name = "idBasket")
    private Basket basket;

    public User() {
    }

    public User(@NotNull Integer idUser, @NotNull Long chatId, @NotNull String username, @NotNull String password,
                Boolean isActive, String fName, String lName, Date dateOfBirth, Gender gender,
                BotState botState, UserRole userRole) {
        this.idUser = idUser;
        this.chatId = chatId;
        this.username = username;
        this.password = password;
        this.isActive = isActive;
        this.fName = fName;
        this.lName = lName;
        this.dateOfBirth = dateOfBirth;
        this.gender = gender;
        this.botState = botState;
        this.userRole = userRole;
    }

    public Integer getIdUser() {
        return idUser;
    }

    public void setIdUser(Integer idUser) {
        this.idUser = idUser;
    }

    public Long getChatId() {
        return chatId;
    }

    public void setChatId(Long chatId) {
        this.chatId = chatId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Boolean getActive() {
        return isActive;
    }

    public void setActive(Boolean active) {
        isActive = active;
    }

    public String getfName() {
        return fName;
    }

    public void setfName(String fName) {
        this.fName = fName;
    }

    public String getlName() {
        return lName;
    }

    public void setlName(String lName) {
        this.lName = lName;
    }

    public Date getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(Date dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public Gender getGender() {
        return gender;
    }

    public void setGender(Gender gender) {
        this.gender = gender;
    }

    public BotState getBotState() {
        return botState;
    }

    public void setBotState(BotState botState) {
        this.botState = botState;
    }

    public UserRole getUserRole() {
        return userRole;
    }

    public void setUserRole(UserRole userRole) {
        this.userRole = userRole;
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

    public Collection<Order> getOrders() {
        return orders;
    }

    public void setOrders(Collection<Order> orders) {
        this.orders = orders;
    }

    public Basket getBasket() {
        return basket;
    }

    public void setBasket(Basket basket) {
        this.basket = basket;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof User)) return false;
        User user = (User) o;
        return Objects.equals(getIdUser(), user.getIdUser()) &&
                Objects.equals(getChatId(), user.getChatId()) &&
                Objects.equals(getUsername(), user.getUsername()) &&
                Objects.equals(getPassword(), user.getPassword()) &&
                Objects.equals(isActive, user.isActive) &&
                Objects.equals(getfName(), user.getfName()) &&
                Objects.equals(getlName(), user.getlName()) &&
                Objects.equals(getDateOfBirth(), user.getDateOfBirth()) &&
                getGender() == user.getGender() &&
                getBotState() == user.getBotState() &&
                Objects.equals(getCountry(), user.getCountry()) &&
                Objects.equals(getCity(), user.getCity()) &&
                Objects.equals(getStreet(), user.getStreet()) &&
                Objects.equals(getHousenumber(), user.getHousenumber())&&
                Objects.equals(getUserRole(), user.getUserRole());
    }

    @Override
    public int hashCode() {

        return Objects.hash(getIdUser(), getChatId(), getUsername(), getPassword(), isActive, getfName(), getlName(),
                getDateOfBirth(), getGender(), getBotState(), getCountry(), getCity(), getStreet(),
                getHousenumber(), getUserRole());
    }
}
