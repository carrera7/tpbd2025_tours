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

@Entity
@Table(name = "item_de_servicio")
public class ItemService {

    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    @Column(name = "item_de_servicio_id")
    Long id;

    @Column(name = "Cantidad")
    private int quantity;

    @Column(name = "compra")
    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "compras_id", referencedColumnName = "id") // si se llega a romper el id de esto, es por los nombres
    private Purchase purchase;

    @Column(name = "servicio")
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
