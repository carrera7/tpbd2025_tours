package unlp.info.bd2.model;


import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class TourGuideUser extends User {

    private String education;

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

}
