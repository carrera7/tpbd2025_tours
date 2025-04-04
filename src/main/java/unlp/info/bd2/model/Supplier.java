package unlp.info.bd2.model;

import java.util.List;

/**import jakarta.annotation.Generated;
import jakarta.persistence.CascadeType;**/
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
/**import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;**/
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

@Entity
@Table(name = "proveedor")
public class Supplier {

    public Supplier(){

    }

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "proveedor_id")
    private Long id;

    @Column(name = "nombre_de_negocio")
    private String businessName;

    @Column(name = "numero_authenticacion")
    private String authorizationNumber;

    @OneToMany(mappedBy = "supplier")   
    private List<Service> services;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getBusinessName() {
        return businessName;
    }

    public void setBusinessName(String businessName) {
        this.businessName = businessName;
    }

    public String getAuthorizationNumber() {
        return authorizationNumber;
    }

    public void setAuthorizationNumber(String authorizationNumber) {
        this.authorizationNumber = authorizationNumber;
    }

    public List<Service> getServices() {
        return services;
    }

    public void setServices(List<Service> services) {
        this.services = services;
    }

}
