package unlp.info.bd2.repositories;
import java.util.List;
import java.util.Date;
import unlp.info.bd2.model.Purchase;
import unlp.info.bd2.model.Route;
import unlp.info.bd2.model.Service;
import unlp.info.bd2.model.Stop;
import unlp.info.bd2.model.Supplier;
import unlp.info.bd2.model.TourGuideUser;
import unlp.info.bd2.model.User;

public interface ToursRepository {

    List<Purchase> getAllPurchasesOfUsername(String username);
    List<User> getUserSpendingMoreThan(float mount);
    List<Supplier> getTopNSuppliersInPurchases(int n);
    List<Purchase> getTop10MoreExpensivePurchasesInServices();
    List<User> getTop5UsersMorePurchases();
    long getCountOfPurchasesBetweenDates(Date start, Date end);
    List<Route> getRoutesWithStop(Stop stop);
    Long getMaxStopOfRoutes();
    List<Route> getRoutsNotSell();
    List<Route> getTop3RoutesWithMaxRating();
    Service getMostDemandedService();
    List<Service> getServiceNoAddedToPurchases();
    List<TourGuideUser> getTourGuidesWithRating1();

 // crud de hibernate, buscar en documentacion
   // add
    //delete 
    //update
  // no se implementa en ToursRepositoryImpl ??
}
