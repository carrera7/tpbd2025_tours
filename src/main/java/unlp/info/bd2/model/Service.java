package unlp.info.bd2.model;

/**import java.util.ArrayList;**/
import java.util.List;

/**import jakarta.annotation.Generated;**/
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

@Entity
@Table(name = "servicios")
public class Service {

    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    @Column(name = "servicios_id")
    private Long id;

    @Column(name = "nombre")
    private String name;

    @Column(name = "precio")
    private float price;

    @Column(name = "descripcion")
    private String description;

    @OneToMany
    @Column(name="lista_de_item_de_servicios")
    private List<ItemService> itemServiceList;

    @ManyToOne(cascade=CascadeType.ALL)
    @JoinColumn(name = "proveedor_id", referencedColumnName = "id")
    @Column(name="proveedor")
    private Supplier supplier;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public float getPrice() {
        return price;
    }

    public void setPrice(float price) {
        this.price = price;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<ItemService> getItemServiceList() {
        return itemServiceList;
    }

    public void setItemServiceList(List<ItemService> itemServiceList) {
        this.itemServiceList = itemServiceList;
    }

    public Supplier getSupplier() {
        return supplier;
    }

    public void setSupplier(Supplier supplier) {
        this.supplier = supplier;
    }
}
