package unlp.info.bd2.model;


import java.util.ArrayList;
/**import java.util.Date;**/
import java.util.List;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;

@Entity
@Table(name = "guias_turisticos")
public class TourGuideUser extends User {

    @Id
    @Column(name = "educacion")
    private String education;

    @ManyToMany
    @Column(name="ruta")
    private List<Route> routes;


    public String getEducation() {
        return education;
    }

    public void setEducation(String education) {
        this.education = education;
    }

    public List<Route> getRoutes() {
        return routes;
    }

    public void setRoutes(List<Route> routes) {
        this.routes = routes;
    }

    /*
     * agregar una ruta a la lista de rutas
     * @param route la ruta a agregar
     * @return void
     */

    public void addRoute(Route route) {
        if (this.routes == null) {
            this.routes = new ArrayList<>();
        }
        this.routes.add(route);
    }

    public boolean canBeDesactivated() {
        return super.canBeDesactivated();
    }

}
