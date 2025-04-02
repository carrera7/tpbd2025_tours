package unlp.info.bd2.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table (name = "stops")
public class Stop {

    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)/*Deberia de ser auto o Identity? trabajamos con sql */ 
    @Column(name = "id_stop")
    private Long id;
    @Column(name = "name")
    private String name;

    @Column(name = "description")
    private String description;

    /*
     * modificacion a @OneToMany
     * @OneToMany(mappedBy = "route", cascade = CascadeType.ALL, orphanRemoval = true)
     * @JoinColumn(name = "route_id")
     * private List<Stop> stops;
     */


    // @OneToMany(mappedBy = "stop", cascade = CascadeType.ALL, orphanRemoval = true)
    // private Route route;
    // @ManyToMany(mappedBy = "stops")
    // private List<Route> routes  no es necesario ssi es bidereccional ;

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
