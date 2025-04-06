package unlp.info.bd2.model;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.*;

@Entity
@Table(name = "supplier")
public class Supplier {

    public Supplier(){
        this.services = new ArrayList<Service>(); // esta bien instanciarlo??
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // Suponiendo autoincremental
    private Long id;
    
    @Column(name="businessName",columnDefinition = "TEXT")
    private String businessName;

    @Column(name="authorizationNumber",columnDefinition = "TEXT", unique = true)
    private String authorizationNumber;

    @OneToMany(mappedBy = "supplier", cascade = CascadeType.ALL, orphanRemoval = true)
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
