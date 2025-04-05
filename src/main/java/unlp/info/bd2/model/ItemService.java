package unlp.info.bd2.model;
import jakarta.persistence.*;


@Entity
@Table(name = "itemService")
public class ItemService {

    public ItemService(){

    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "quantity")
    private int quantity;

    /**La anotación @ManyToOne(optional = false) 
     * asegura que cada ItemService debe estar vinculado a un Purchase 
     * y un Service. cascade = CascadeType.ALL en la relación con Purchase 
     * simula la composición: si se elimina un Purchase, se eliminan sus ItemService. 
     * */
    // Relación Muchos a Uno con Purchase (composición: Purchase tiene muchos ItemService)
    @ManyToOne(optional = false, cascade = CascadeType.ALL)
    @JoinColumn(name = "purchase_id", nullable = false)
    private Purchase purchase;

    // Relación Muchos a Uno solo Service
    @ManyToOne
    @JoinColumn(name = "service_id", nullable = false)
    private Service service;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }


    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public Purchase getPurchase() {
        return purchase;
    }

    public void setPurchase(Purchase purchase) {
        this.purchase = purchase;
    }

    public Service getService() {
        return service;
    }

    public void setService(Service service) {
        this.service = service;
    }
}
