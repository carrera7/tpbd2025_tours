package unlp.info.bd2.model;

import java.util.ArrayList;
import java.util.List;


import jakarta.persistence.*;

@Entity
@Table(name = "Service")
public class Service {
    
    public Service(){
        
    }

    @Id
    private Long id;
    
    @Column(name="name",length = 155)
    private String name;

    @Column(name="price")
    private float price;

    @Column(name="description",length = 255)
    private String description;

    @OneToMany(mappedBy = "service", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ItemService> itemServiceList;

    @ManyToOne
    @JoinColumn(name = "supplier_id") // Esta será la columna FK en la tabla "Service"
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
    if (price < 0) {
        throw new IllegalArgumentException("El precio no puede ser negativo.");
    }
    if (price > 1_000_000) {
        throw new IllegalArgumentException("El precio es demasiado alto.");
    }
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
