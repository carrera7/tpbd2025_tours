package unlp.info.bd2.model;

import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
/**import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;**/
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

@Entity
@Table (name = "paradas")
public class Stop {

    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)/*Deberia de ser auto o Identity? */
    @Column(name = "paradas_id")
    private Long id;

    @Column(name = "nombre")
    private String name;

    @Column(name = "descripcion")
    private String description;

    /*
     * modificacion a @OneToMany
     * @OneToMany(mappedBy = "route", cascade = CascadeType.ALL, orphanRemoval = true)
     * @JoinColumn(name = "route_id")
     * private List<Stop> stops;
     */


    // @OneToMany(mappedBy = "stop", cascade = CascadeType.ALL, orphanRemoval = true)
    // private Route route;
    @ManyToMany(mappedBy = "paradas")
    private List<Route> routes;

    public Stop(){}

    public Stop(String name, String description) {
        this.name = name;
        this.description = description;
    }   


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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
