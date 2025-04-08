package unlp.info.bd2.model;

import java.util.Date;
import java.util.List;
import java.util.ArrayList;
import jakarta.persistence.*;

@Entity
@Table(name = "purchase")
public class Purchase {

    public Purchase(){

    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "code", length = 255, nullable = false, unique = true)
    private String code;

    @Column(name = "total_price")
    private float totalPrice;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "date", nullable = false)
    private Date date;

    // Relación con User
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    // Relación con Route
    @ManyToOne(optional = false)
    @JoinColumn(name = "route_id", nullable = false)
    private Route route;

    // Relación uno a uno con Review (opcional)
    @OneToOne(mappedBy = "purchase", cascade = CascadeType.ALL,orphanRemoval = true)
    private Review review;

    // Relación con ItemService
    @OneToMany(mappedBy = "purchase", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ItemService> itemServiceList =  new ArrayList<>();;

    public Purchase(String code, Route route, User user){
        this.setCode(code);
        this.setRoute(route);
        this.setUser(user);
        this.date = new Date();
        this.totalPrice = route.getPrice();
    }

    public Purchase(String code, Date date, Route route, User user){
        this.setCode(code);
        this.setRoute(route);
        this.setUser(user);
        this.setDate(date);
        this.totalPrice = route.getPrice();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public float getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(float totalPrice) {
        this.totalPrice = totalPrice;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Route getRoute() {
        return route;
    }

    public void setRoute(Route route) {
        this.route = route;
    }

    public Review getReview() {
        return review;
    }

    public void setReview(Review review) {
        this.review = review;
    }

    
    public List<ItemService> getItemServiceList() {
        return itemServiceList;
    }

    public void setItemServiceList(List<ItemService> itemServiceList) {
        this.itemServiceList = itemServiceList;
    }
}
