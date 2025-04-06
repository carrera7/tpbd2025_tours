package unlp.info.bd2.repositories;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.jms.JmsProperties.Listener.Session;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

import unlp.info.bd2.model.Purchase;
import unlp.info.bd2.model.Route;
import unlp.info.bd2.model.Service;
import unlp.info.bd2.model.Stop;
import unlp.info.bd2.model.Supplier;
import unlp.info.bd2.model.TourGuideUser;
import unlp.info.bd2.model.User;

public class ToursRepositoryImpl implements ToursRepository{

    @Autowired
    private SessionFactory session;

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public <T> T save(T o) {
        entityManager.persist(o);
        return o;
    }

    @Override
    public List<Purchase> getAllPurchasesOfUsername(String username) {
        return session.getCurrentSession().createQuery(
            "FROM User u WHERE u.username = :username", Purchase.class)
            .setParameter("username", username)
            .getResultList();
    }

    @Override
    public List<User> getUserSpendingMoreThan(float mount) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getUserSpendingMoreThan'");
    }

    @Override
    public List<Supplier> getTopNSuppliersInPurchases(int n) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getTopNSuppliersInPurchases'");
    }

    @Override
    public List<Purchase> getTop10MoreExpensivePurchasesInServices() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getTop10MoreExpensivePurchasesInServices'");
    }

    @Override
    public List<User> getTop5UsersMorePurchases() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getTop5UsersMorePurchases'");
    }

    @Override
    public long getCountOfPurchasesBetweenDates(Date start, Date end) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getCountOfPurchasesBetweenDates'");
    }

    @Override
    public List<Route> getRoutesWithStop(Stop stop) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getRoutesWithStop'");
    }

    @Override
    public Long getMaxStopOfRoutes() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getMaxStopOfRoutes'");
    }

    @Override
    public List<Route> getRoutsNotSell() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getRoutsNotSell'");
    }

    @Override
    public List<Route> getTop3RoutesWithMaxRating() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getTop3RoutesWithMaxRating'");
    }

    @Override
    public Service getMostDemandedService() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getMostDemandedService'");
    }

    @Override
    public List<Service> getServiceNoAddedToPurchases() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getServiceNoAddedToPurchases'");
    }

    @Override
    public List<TourGuideUser> getTourGuidesWithRating1() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getTourGuidesWithRating1'");
    }

    @Override
    public Optional<Stop> findStopByName(String name) {
        List<Stop> results = entityManager
            .createQuery("FROM Stop s WHERE s.name = :name", Stop.class)
            .setParameter("name", name)
            .getResultList();

        return results.stream().findFirst();
    }

    @Override
    public List<Stop> findStopByNameStartingWith(String prefix) {
        String query =  "FROM Stop s WHERE s.name LIKE :prefix";
        return session.getCurrentSession().createQuery(query, Stop.class)
            .setParameter("prefix", prefix + "%")
            .getResultList();
    }

    @Override
    public Optional<Route> getRouteById(Long id) {
        Route route = entityManager.find(Route.class, id);
        return Optional.ofNullable(route);
    }

    @Override
    public List<Route> findRoutesBelowPrice(float price) {
        return entityManager.createQuery(
                "FROM Route r WHERE r.price < :price", Route.class)
            .setParameter("price", price)
            .getResultList();
    }

    @Override
    public List<Route> findRoutesWithStop(Stop stop) {
        String jpql = """
            SELECT r
            FROM Route r
            JOIN r.stops s
            WHERE s = :stop
        """;
    
        return entityManager.createQuery(jpql, Route.class)
                            .setParameter("stop", stop)
                            .getResultList();
    }

}