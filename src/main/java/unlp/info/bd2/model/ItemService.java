package unlp.info.bd2.model;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.FetchType;
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
    @ManyToOne(optional = false, fetch = FetchType.EAGER)
    @JoinColumn(name = "purchase_id", nullable = false)
    private Purchase purchase;

    // Relación Muchos a Uno solo Service
    @ManyToOne(fetch = FetchType.EAGER, optional=false)
    @JoinColumn(name = "service_id", nullable = false)
    private Service service;

    public ItemService(Service service, int quantity, Purchase purchase) {
        this.setService(service);
        this.setQuantity(quantity);
        this.setPurchase(purchase);
    }
    

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
