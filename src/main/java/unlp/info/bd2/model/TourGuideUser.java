package unlp.info.bd2.model;


import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import jakarta.persistence.*;

@Entity
@DiscriminatorValue("TOUR_GUIDE")
public class TourGuideUser extends User {

    public TourGuideUser(){

    }


    @Column(name = "education", length = 255)
    private String education;

    @ManyToMany(mappedBy = "tourGuideList")
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

    //agrego metodos
    public TourGuideUser(String username, String password, String fullName, String email,
                         Date birthdate, String phoneNumber, String education) {
        super(username, password, fullName, email, birthdate, phoneNumber);
        this.setEducation(education);
        this.routes = new ArrayList<>();
    }
}
