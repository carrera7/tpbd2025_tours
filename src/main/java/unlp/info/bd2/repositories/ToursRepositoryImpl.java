package unlp.info.bd2.repositories;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import unlp.info.bd2.model.ItemService;
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
        session.getCurrentSession().save(o);
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
    @Transactional
    public List<User> getUserSpendingMoreThan(float amount) {
        return session.getCurrentSession()
            .createQuery("""
                SELECT p.user
                FROM Purchase p
                GROUP BY p.user
                HAVING SUM(p.totalPrice) > :amount
            """, User.class)
            .setParameter("amount", amount)
            .getResultList();
    }
    
    @Override
    @Transactional
    public List<Supplier> getTopNSuppliersInPurchases(int n) {
        return session.getCurrentSession()
            .createQuery("""
                SELECT s
                FROM ItemService i
                JOIN i.service srv
                JOIN srv.supplier s
                GROUP BY s
                ORDER BY COUNT(i) DESC
            """, Supplier.class)
            .setMaxResults(n)
            .getResultList();
    }
    

    @Override
    @Transactional
    public List<Purchase> getTop10MoreExpensivePurchasesInServices() {
        Session currentSession = session.getCurrentSession();

        // 1. Obtener todos los ItemService
        List<ItemService> allItemServices = currentSession
            .createQuery("FROM ItemService", ItemService.class)
            .getResultList();

        // 2. Mapear Purchase -> Total de servicios (sumar quantity * price)
        Map<Purchase, Float> purchaseServiceCosts = new HashMap<>();
        for (ItemService item : allItemServices) {
            Purchase purchase = item.getPurchase();
            float cost = item.getQuantity() * item.getService().getPrice();
            purchaseServiceCosts.put(purchase, purchaseServiceCosts.getOrDefault(purchase, 0f) + cost);
        }

        // 3. Ordenar las compras por el total invertido en servicios, de mayor a menor
        List<Map.Entry<Purchase, Float>> sortedPurchases = purchaseServiceCosts.entrySet()
            .stream()
            .sorted(Map.Entry.<Purchase, Float>comparingByValue().reversed())
            .limit(10)
            .toList();

        // 4. Retornar solo las compras
        return sortedPurchases.stream()
            .map(Map.Entry::getKey)
            .toList();
    }

    @Override
    @Transactional
    public List<User> getTop5UsersMorePurchases() {
        Session currentSession = session.getCurrentSession();

        // 1. Obtener todos los Purchases
        List<Purchase> allPurchases = currentSession
            .createQuery("FROM Purchase", Purchase.class)
            .getResultList();

        // 2. Mapear User -> cantidad de compras realizadas
        Map<User, Integer> userPurchaseCounts = new HashMap<>();
        for (Purchase purchase : allPurchases) {
            User user = purchase.getUser();
            userPurchaseCounts.put(user, userPurchaseCounts.getOrDefault(user, 0) + 1);
        }

        // 3. Ordenar los usuarios por cantidad de compras, de mayor a menor
        List<Map.Entry<User, Integer>> sortedUsers = userPurchaseCounts.entrySet()
            .stream()
            .sorted(Map.Entry.<User, Integer>comparingByValue().reversed())
            .limit(5)
            .toList();

        // 4. Retornar solo los usuarios
        return sortedUsers.stream()
            .map(Map.Entry::getKey)
            .toList();
    }

    @Override
    @Transactional
    public long getCountOfPurchasesBetweenDates(Date start, Date end) {
        Session currentSession = session.getCurrentSession();
        
        Long count = currentSession
            .createQuery(
                "SELECT COUNT(p) FROM Purchase p WHERE p.date >= :start AND p.date <= :end",
                Long.class
            )
            .setParameter("start", start)
            .setParameter("end", end)
            .getSingleResult();
            
        return count;
    }

    @Override
    @Transactional
    public List<Route> getRoutesWithStop(Stop stop) {
        Session currentSession = session.getCurrentSession();
        
        List<Route> routes = currentSession
            .createQuery(
                "SELECT DISTINCT r FROM Route r JOIN r.stops s WHERE s = :stop",
                Route.class
            )
            .setParameter("stop", stop)
            .getResultList();
        
        return routes;
    }
    
    @Override
    @Transactional
    public Integer getMaxStopCountOfRoutes() {
        Session currentSesion = session.getCurrentSession();
        String hql = """
            SELECT MAX(SIZE(r.stops))
            FROM Route r
        """;
        Integer  stopCount  =currentSesion.createQuery(hql, Integer.class).getSingleResult();
        return stopCount;
    }

    
    @Override
    @Transactional
    public List<Route> getRoutsNotSell() {
        Session currentSession = session.getCurrentSession();
    
        List<Route> routesNotSold = currentSession
            .createQuery(
                "SELECT r FROM Route r WHERE r.id NOT IN (" +
                "SELECT DISTINCT p.route.id FROM Purchase p WHERE p.route IS NOT NULL)",
                Route.class
            )
            .getResultList();
    
        return routesNotSold;
    }
    
    @Override
    @Transactional
    public List<Route> getTop3RoutesWithMaxRating() {
        Session currentSession = session.getCurrentSession();

        List<Route> topRoutes = currentSession
            .createQuery(
                "SELECT r FROM Review rev " +
                "JOIN rev.purchase p " +
                "JOIN p.route r " +
                "GROUP BY r.id " +
                "ORDER BY AVG(rev.rating) DESC", 
                Route.class
            )
            .setMaxResults(3)
            .getResultList();

        return topRoutes;
    }

    @Override
    @Transactional
    public Service getMostDemandedService() {
        Session currentSession = session.getCurrentSession();

        List<Service> result = currentSession
            .createQuery(
                "SELECT s FROM ItemService i " +
                "JOIN i.service s " +
                "GROUP BY s.id " +
                "ORDER BY COUNT(i.id) DESC", 
                Service.class
            )
            .setMaxResults(1)
            .getResultList();

        return result.isEmpty() ? null : result.get(0);
    }

    @Override
    @Transactional
    public List<Service> getServiceNoAddedToPurchases() {
        Session currentSession = session.getCurrentSession();

        return currentSession
            .createQuery(
                "SELECT s FROM Service s " +
                "WHERE s.id NOT IN (" +
                "SELECT DISTINCT iser.service.id FROM ItemService iser" +
                ")", Service.class)
            .getResultList();
    }

    @Override
    @Transactional
    public List<TourGuideUser> getTourGuidesWithRating1() {
        Session currentSession = session.getCurrentSession();

        // Usamos DISTINCT para evitar guías duplicados en caso de múltiples reviews con rating 1 en la misma ruta
        String hql = "SELECT DISTINCT tg FROM Review r " +
                    "JOIN r.purchase p " +
                    "JOIN p.route ro " +
                    "JOIN ro.tourGuideList tg " +
                    "WHERE r.rating = 1";

        return currentSession
                .createQuery(hql, TourGuideUser.class)
                .getResultList();
    }

    @Override
    @Transactional
    public Optional<Stop> findStopByName(String name) {
        Session currentSession = session.getCurrentSession();
    
        List<Stop> results = currentSession
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
    @Transactional
    public Optional<Route> getRouteById(Long id) {
        Session currentSession = session.getCurrentSession();
        Route route = currentSession.get(Route.class, id);
        return Optional.ofNullable(route);
    }
    
    @Override
    @Transactional
    public List<Route> findRoutesBelowPrice(float price) {
        Session currentSession = session.getCurrentSession();
        return currentSession.createQuery(
                "FROM Route r WHERE r.price < :price", Route.class)
            .setParameter("price", price)
            .getResultList();
    }
    
    @Override
    @Transactional
    public List<Route> findRoutesWithStop(Stop stop) {
        Session currentSession = session.getCurrentSession();
    
        String hql = """
            SELECT r
            FROM Route r
            JOIN r.stops s
            WHERE s = :stop
        """;
    
        return currentSession.createQuery(hql, Route.class)
                             .setParameter("stop", stop)
                             .getResultList();
    }

}