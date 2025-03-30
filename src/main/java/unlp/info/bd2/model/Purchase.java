package unlp.info.bd2.model;

import java.util.Date;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

@Entity
@Table(name = "compras")
public class Purchase {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "compras_id")
    Long id;

    @Column(name = "codigo")
    private String code;

    @Column(name = "precio_total")
    private float totalPrice;

    @Column(name = "fecha")
    private Date date;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "usuarios_id", referencedColumnName = "id")
    @Column(name="usuario")
    private User user;

    @OneToOne
    @Column(name="ruta")
    private Route route;

    @OneToOne
    @Column(name="revision")
    private Review review;

    @OneToMany
    @Column(name="lista_de_item_de_servicio")
    private List<ItemService> itemServiceList;


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

    /*
     *  agraga un nuevo item de servicio a la lista de item de servicio de la compra
     *  @param itemService el item de servicio a agregar
     *  @return void 
     */

    public void addItemService(ItemService itemService) {
        this.itemServiceList.add(itemService);
    }


    /*
     *  agrega una nueva revision a la compra
     *  @param rating la calificacion de la revision
     *  @param comment el comentario de la revision
     *  @return Review la revision creada 
     * 
     * falta implementar la logica si es que existe una revision
     *  
     */
    public Review addReview(int rating, String comment) {
        this.review = new Review(rating, comment, this);
        return this.review;
    }


}

 