package unlp.info.bd2.model;


import java.util.List;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.DiscriminatorValue;


@Entity
@DiscriminatorValue("Chofer")
public class DriverUser extends User {

    public DriverUser(){
        
    }

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

    /* agregar una ruta a la lista de rutas
     * @param route
     */

    public void addRoute(Route route) {
        if (this.routes == null) {
            this.routes = new java.util.ArrayList<>();
        }
        this.routes.add(route);
    }

    public boolean canBeDesactivated() {
        return super.canBeDesactivated();
    }
}
