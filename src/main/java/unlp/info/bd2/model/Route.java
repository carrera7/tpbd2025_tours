package unlp.info.bd2.model;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;

@Entity
@Table(name = "routes")
public class Route {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_routes")
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "price")
    private float price;

    @Column(name = "total_km")
    private float totalKm;

    @Column(name = "max_number_users")
    private int maxNumberUsers;

    /*
     * modificacion a @OneToMany
     * 
     * @OneToMany(mappedBy = "route", cascade = CascadeType.ALL, orphanRemoval =
     * true)
     * 
     * @JoinColumn(name = "route_id")
     * private List<Stop> stops;
     */

    // @OneToMany(mappedBy = "route", cascade = CascadeType.ALL, orphanRemoval =
    // true) @Column(name = "parada")
    // private List<Stop> stops;|

    @ManyToMany(fetch = FetchType.EAGER )// ver el estandar por default, el persis solo permite guardado , pero como en los test genera las paradas y despues las rutas , se pueden scar todo tipo de cascade tyoe
    //no va ophanage removal porque sis e queire tneer paradas sueltas para aa futuro asociar a rutas
    @JoinTable(name = "route_stop", joinColumns = @JoinColumn(name = "id_route", nullable = false), inverseJoinColumns = @JoinColumn(name = "id_stop", nullable = false) )
    private List<Stop> stops;

    @ManyToMany
    @Column(name = "lista_de_chofer")
    private List<DriverUser> driverList;

    @ManyToMany
    private List<TourGuideUser> tourGuideList;


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

    public float getTotalKm() {
        return totalKm;
    }

    public void setTotalKm(float totalKm) {
        this.totalKm = totalKm;
    }

    public int getMaxNumberUsers() {
        return maxNumberUsers;
    }

    public void setMaxNumberUsers(int maxNumberUsers) {
        this.maxNumberUsers = maxNumberUsers;
    }

    public List<Stop> getStops() {
        return stops;
    }

    public void setStops(List<Stop> stops) {
        this.stops = stops;
    }

    public List<DriverUser> getDriverList() {
        return driverList;
    }

    public void setDriverList(List<DriverUser> driverList) {
        this.driverList = driverList;
    }

    public List<TourGuideUser> getTourGuideList() {
        return tourGuideList;
    }

    public void setTourGuideList(List<TourGuideUser> tourGuideList) {
        this.tourGuideList = tourGuideList;
    }

}
