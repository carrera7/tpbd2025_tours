package unlp.info.bd2.model;


import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.*;

@Entity
@DiscriminatorValue("DRIVER") // Solo si estás usando herencia con SINGLE_TABLE
public class DriverUser extends User {

    public DriverUser(){    
    }

    @Column(name = "expedient", length = 100, nullable = false, unique = true)
    private String expedient;

    @ManyToMany(mappedBy = "driverList")
    private List<Route> routes = new ArrayList<>();;

    public String getExpedient() {
        return expedient;
    }

    public void setExpedient(String expedient) {
        this.expedient = expedient;
    }

    public List<Route> getRoutes() {
        return routes;
    }

    public void setRouts(List<Route> routs) {
        this.routes = routs;
    }
}
