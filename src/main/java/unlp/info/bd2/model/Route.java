package unlp.info.bd2.model;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.*;

@Entity
@Table(name = "routes")
public class Route {

    public Route(){

    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)   
    private Long id;

    @Column(name = "name", length = 100, nullable = false)
    private String name;

    @Column(name = "price", nullable = false)
    private float price;

    @Column(name="totalKm")
    private float totalKm;

    @Column(name="maxNumberUsers")
    private int maxNumberUsers;

    @ManyToMany(fetch = FetchType.LAZY) // o EAGER si realmente se usa siempre
    @JoinTable(
        name = "route_stop",
        joinColumns = @JoinColumn(name = "route_id"),
        inverseJoinColumns = @JoinColumn(name = "stop_id")
    )
    private List<Stop> stops = new ArrayList<>();

    @ManyToMany(fetch = FetchType.LAZY) // o EAGER si realmente se usa siempre
    @JoinTable(
        name = "route_driver",
        joinColumns = @JoinColumn(name = "route_id"),
        inverseJoinColumns = @JoinColumn(name = "driver_id")
    )
    private List<DriverUser> driverList = new ArrayList<>();

    @ManyToMany(fetch = FetchType.LAZY, cascade ={CascadeType.PERSIST, CascadeType.MERGE}) // o EAGER si realmente se usa siempre, consultar el cascade type si tiene sentido , maas que nada el merge 
    @JoinTable(
        name = "route_tour_guide",
        joinColumns = @JoinColumn(name = "route_id"),
        inverseJoinColumns = @JoinColumn(name = "tour_guide_id")
    )
    private List<TourGuideUser> tourGuideList = new ArrayList<>();

    public Route(String name, float price, float totalKm, int maxNumberOfUsers, List<Stop> stops){
       this.setName(name);
       this.setPrice(price);
       this.setTotalKm(totalKm);
       this.setMaxNumberUsers(maxNumberOfUsers);
       this.setStops(stops); 
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

    public void addDriver(DriverUser driverUser) {
        if (driverUser != null && !driverList.contains(driverUser)) {
            driverList.add(driverUser);
        }
    }

    public void addTourGuide(TourGuideUser tourGuideUser) {
        if (tourGuideUser != null && !tourGuideList.contains(tourGuideUser)) {
            tourGuideList.add(tourGuideUser);
        }
    }
    
    

}
