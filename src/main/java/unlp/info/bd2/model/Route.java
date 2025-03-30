package unlp.info.bd2.model;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

@Entity
@Table(name = "routes")
public class Route {

    @Id 
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "route_id")
    private Long id;

    @Column(name = "nombre")
    private String name;

    @Column(name = "precio")
    private float price;

    @Column(name = "km_totales")
    private float totalKm;

    @Column(name = "numero_maximo_usuarios")
    private int maxNumberUsers;

    /*
     * modificacion a @OneToMany
     * @OneToMany(mappedBy = "route", cascade = CascadeType.ALL, orphanRemoval = true)
     * @JoinColumn(name = "route_id")
     * private List<Stop> stops;    
     */

    @OneToMany(mappedBy = "route", cascade = CascadeType.ALL, orphanRemoval = true)    @Column(name = "parada")
    private List<Stop> stops;

    @ManyToMany
    @Column(name = "lista_de_chofer")
    private List<DriverUser> driverList;

    @ManyToMany
    private List<TourGuideUser> tourGuideList;

    public Route() { // es necesario el constructor para instanciar las listas
        this.stops = new ArrayList<Stop>();
        this.driverList = new ArrayList<DriverUser>();
        this.tourGuideList = new ArrayList<TourGuideUser>();
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

    public void addDriver(DriverUser driver) {
        this.driverList.add(driver);
    }

    public void addTourGuide(TourGuideUser tourGuide){
        this.tourGuideList.add(tourGuide);
    }

}
