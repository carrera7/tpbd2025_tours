package unlp.info.bd2.model;


import java.util.List;

import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToMany;

@Entity
@DiscriminatorValue("Chofer")
public class DriverUser extends User {

    @Column(name = "expediente")
    private String expedient;

    @ManyToMany
    //@Column(name="ruta") si se necesita personalizar la relacion usar @jointable
    private List<Route> routes;
    
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
