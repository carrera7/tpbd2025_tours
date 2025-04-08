package unlp.info.bd2.services;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;

import jakarta.persistence.NoResultException;
import jakarta.transaction.Transactional;
import unlp.info.bd2.model.DriverUser;
import unlp.info.bd2.model.ItemService;
import unlp.info.bd2.model.Purchase;
import unlp.info.bd2.model.Review;
import unlp.info.bd2.model.Route;
import unlp.info.bd2.model.Service;
import unlp.info.bd2.model.Stop;
import unlp.info.bd2.model.Supplier;
import unlp.info.bd2.model.TourGuideUser;
import unlp.info.bd2.model.User;
import unlp.info.bd2.repositories.ToursRepository;
import unlp.info.bd2.utils.ToursException;

@org.springframework.stereotype.Service
public class ToursServiceImpl implements ToursService{

    private ToursRepository tourRepository;

    @Autowired
    private SessionFactory sessionFactory;

    public ToursServiceImpl(ToursRepository repository){
        this.tourRepository= repository;
    }

    @Override
    @Transactional
    public User createUser(String username, String password, String fullName, String email, Date birthdate,
            String phoneNumber) throws ToursException {
        try {
            User user = new User(username, password, fullName, email, birthdate, phoneNumber);
            sessionFactory.getCurrentSession().persist(user);
            return user;
        } catch (Exception e) {
            throw new ToursException("Error al crear y persistir User: " + e.getMessage());
        }
    }
    
    @Override
    @Transactional
    public DriverUser createDriverUser(String username, String password, String fullName, String email, Date birthdate,
            String phoneNumber, String expedient) throws ToursException {
        try {
            DriverUser driverUser = new DriverUser(username, password, fullName, email, birthdate, phoneNumber, expedient);
            sessionFactory.getCurrentSession().persist(driverUser);
            return driverUser;
        } catch (Exception e) {
            throw new ToursException("Error al crear y persistir DriverUser: " + e.getMessage());
        }
    }    
    
    @Override
    @Transactional
    public TourGuideUser createTourGuideUser(String username, String password, String fullName, String email,
            Date birthdate, String phoneNumber, String education) throws ToursException {
        try {
            TourGuideUser tourGuideUser = new TourGuideUser(username, password, fullName, email, birthdate, phoneNumber, education);
            sessionFactory.getCurrentSession().persist(tourGuideUser);
            return tourGuideUser;
        } catch (Exception e) {
            throw new ToursException("Error al crear y persistir TourGuideUser: " + e.getMessage());
        }
    }
    
    @Override
    @Transactional
    public Optional<User> getUserById(Long id) throws ToursException {
        try {
            User user = sessionFactory.getCurrentSession().get(User.class, id);
            return Optional.ofNullable(user);
        } catch (Exception e) {
            throw new ToursException("Error al obtener el usuario con ID: " + id);
        }
    }
    
    @Override
    @Transactional
    public Optional<User> getUserByUsername(String username) throws ToursException {
        try {
            String hql = "FROM User u WHERE u.username = :username";
            List<User> result = sessionFactory.getCurrentSession()
                .createQuery(hql, User.class)
                .setParameter("username", username)
                .getResultList();
    
            return result.isEmpty() ? Optional.empty() : Optional.of(result.get(0));
        } catch (Exception e) {
            throw new ToursException("Error al obtener usuario por nombre de usuario: " + username);
        }
    }
    

    @Override
    @Transactional
    public User updateUser(User user) throws ToursException {
        try {
            User existingUser = sessionFactory.getCurrentSession().get(User.class, user.getId());
            if (existingUser == null) {
                throw new ToursException("No se encontró el usuario con ID: " + user.getId());
            }
    
            existingUser.setUsername(user.getUsername());
            existingUser.setPassword(user.getPassword());
            existingUser.setName(user.getName());
            existingUser.setEmail(user.getEmail());
            existingUser.setBirthdate(user.getBirthdate());
            existingUser.setPhoneNumber(user.getPhoneNumber());
            existingUser.setActive(user.isActive());
    
            return existingUser;
        } catch (Exception e) {
            throw new ToursException("Error al actualizar el usuario: " + e.getMessage());
        }
    }
    

    @Override
    @Transactional
    public void deleteUser(User user) throws ToursException {
        try {
            User managedUser = sessionFactory.getCurrentSession().contains(user)
                ? user
                : (User) sessionFactory.getCurrentSession().merge(user);
            
            sessionFactory.getCurrentSession().remove(managedUser);
        } catch (Exception e) {
            throw new ToursException("Error al eliminar el usuario con ID: " + user.getId());
        }
    }
    

    @Override
    @Transactional
    public Stop createStop(String name, String description) throws ToursException {
        Optional<Stop> existingStop = tourRepository.findStopByName(name);
        if (existingStop.isPresent()) {
            throw new ToursException("Ya existe una parada con ese nombre");
        }
    
        Stop stop = new Stop();
        stop.setName(name);
        stop.setDescription(description);
        System.out.println("Guardando la parada: " + stop);
    
        sessionFactory.getCurrentSession().persist(stop);
        return stop;
    }
    


    @Override
    public List<Stop> getStopByNameStart(String name) {
        return tourRepository.findStopByNameStartingWith(name);
    }

    @Override
    @Transactional
    public Route createRoute(String name, float price, float totalKm, int maxNumberOfUsers, List<Stop> stops)
            throws ToursException {
        if (name == null || name.isEmpty()) {
            throw new ToursException("El nombre no puede estar vacío");
        }
    
        try {
            Route route = new Route(name, price, totalKm, maxNumberOfUsers, stops);
            route.setStops(stops); // por si el constructor no lo hace
            sessionFactory.getCurrentSession().persist(route);
            return route;
        } catch (Exception e) {
            throw new ToursException("Error al crear la ruta: " + e.getMessage());
        }
    }

    @Override
    public Optional<Route> getRouteById(Long id)  {
        return tourRepository.getRouteById(id);
    }

    /**
     * busca y devuelve todas las rutas (Route) cuya columna price (precio) sea menor al valor pasado como parámetro.
     */

    @Override
    public List<Route> getRoutesBelowPrice(float price){
        return tourRepository.findRoutesBelowPrice(price);
    }
    
    /**
     * asocia un conductor (DriverUser) identificado por su username a una ruta (Route) identificada por su id.
     * Es decir, si tenés una ruta de turismo y querés asignarle un conductor específico a esa ruta, este método lo hace.
     */
    @Override
    @Transactional
    public void assignDriverByUsername(String username, Long idRoute) throws ToursException {
        try {
            Session session = sessionFactory.getCurrentSession();
    
            // Buscar el DriverUser por username
            String hql = "FROM DriverUser d WHERE d.username = :username";
            List<DriverUser> drivers = session.createQuery(hql, DriverUser.class)
                                              .setParameter("username", username)
                                              .getResultList();
    
            if (drivers.isEmpty()) {
                throw new ToursException("No se encontró un conductor con username: " + username);
            }
    
            DriverUser driver = drivers.get(0);
    
            // Buscar la Route por ID
            Route route = session.get(Route.class, idRoute);
            if (route == null) {
                throw new ToursException("No se encontró la ruta con ID: " + idRoute);
            }
    
            // Asociar el conductor a la ruta
            route.addDriver(driver);  // Asocia al lado de la ruta
            driver.getRoutes().add(route);  // Asociación bidireccional
    
            // merge por si alguno no está en el contexto de persistencia
            session.merge(route);
            session.merge(driver);
    
        } catch (Exception e) {
            throw new ToursException("Error al asignar el conductor a la ruta: " + e.getMessage());
        }
    }
    

    /**
     * Busca un guia turistico por su nombre y una ruta por su id y si existen los asocia 
     */
    @Override
    @Transactional
    public void assignTourGuideByUsername(String username, Long idRoute) throws ToursException {
        try {
            Session session = sessionFactory.getCurrentSession();
    
            // Buscar guía turístico por username
            String hql = "FROM TourGuideUser g WHERE g.username = :username";
            List<TourGuideUser> guides = session.createQuery(hql, TourGuideUser.class)
                                                .setParameter("username", username)
                                                .getResultList();
    
            if (guides.isEmpty()) {
                throw new ToursException("Guía turístico no encontrado con username: " + username);
            }
    
            TourGuideUser guide = guides.get(0);
    
            // Buscar la ruta por ID
            Route route = session.get(Route.class, idRoute);
            if (route == null) {
                throw new ToursException("Ruta no encontrada con ID: " + idRoute);
            }
    
            // Asignar guía a la ruta
            if (!route.getTourGuideList().contains(guide)) {
                route.getTourGuideList().add(guide);
            }
    
            // Asignar ruta al guía turístico (bidireccional)
            if (guide.getRoutes() == null) {
                guide.setRoutes(new ArrayList<>());
            }
            if (!guide.getRoutes().contains(route)) {
                guide.getRoutes().add(route);
            }
    
            // Guardar los cambios
            session.merge(route);
            session.merge(guide);
    
        } catch (Exception e) {
            throw new ToursException("Error al asignar guía turístico con username: " + username + " a la ruta ID: " + idRoute);
        }
    }
    

    @Override
    @Transactional
    public Supplier createSupplier(String businessName, String authorizationNumber) throws ToursException {
        try {
            Session session = sessionFactory.getCurrentSession();

            Supplier supplier = new Supplier();
            supplier.setBusinessName(businessName);
            supplier.setAuthorizationNumber(authorizationNumber);

            session.persist(supplier);
            return supplier;
        } catch (Exception e) {
            throw new ToursException("Error al crear el proveedor con nombre: " + businessName + " - " + e.getMessage());
        }
    }


    @Override
    @Transactional
    public Service addServiceToSupplier(String name, float price, String description, Supplier supplier) throws ToursException {
        try {
            Session session = sessionFactory.getCurrentSession();
    
            // Crear nuevo servicio
            Service service = new Service();
            service.setName(name);
            service.setPrice(price); // Validación en setter
            service.setDescription(description);
            service.setSupplier(supplier);
    
            // Persistir el servicio
            session.persist(service);
    
            // Asegurar que la relación sea bidireccional
            if (supplier.getServices() != null) {
                supplier.getServices().add(service);
            }
    
            return service;
        } catch (Exception e) {
            throw new ToursException("Error alfetch = FetchType.LAZY agregar el servicio '" + name + "' al proveedor con ID: " + supplier.getId() + " - " + e.getMessage());
        }
    }
    
    @Override
    @Transactional
    public Service updateServicePriceById(Long id, float newPrice) throws ToursException {
        try {
            Session session = sessionFactory.getCurrentSession();

            // Buscar el servicio por ID
            Service service = session.get(Service.class, id);

            // Verificar si existe
            if (service == null) {
                throw new ToursException("No se encontró un servicio con ID: " + id);
            }

            // Actualizar el precio (valida internamente en el setter)
            service.setPrice(newPrice);

            // No es necesario llamar a update(), ya que el objeto está gestionado por la sesión
            return service;

        } catch (IllegalArgumentException e) {
            throw new ToursException("Precio inválido: " + e.getMessage());
        } catch (Exception e) {
            throw new ToursException("Error al actualizar el precio del servicio con ID: " + id + " - " + e.getMessage());
        }
    }

    @Override
    public Optional<Supplier> getSupplierById(Long id) {
        Session session = sessionFactory.getCurrentSession();
        Supplier supplier = session.get(Supplier.class, id);
        return Optional.ofNullable(supplier);
    }
    
    @Override
    public Optional<Supplier> getSupplierByAuthorizationNumber(String authorizationNumber) {
        try {
            Session session = sessionFactory.getCurrentSession();
            Supplier supplier = session.createQuery(
                    "FROM Supplier s WHERE s.authorizationNumber = :authNumber", Supplier.class)
                    .setParameter("authNumber", authorizationNumber)
                    .uniqueResult();
            return Optional.ofNullable(supplier);
        } catch (Exception e) {
            return Optional.empty();
        }
    }
    
    @Override
    public Optional<Service> getServiceByNameAndSupplierId(String name, Long id) throws ToursException {
        try {
            Session session = sessionFactory.getCurrentSession();
            Service service = session.createQuery(
                    "FROM Service s WHERE s.name = :name AND s.supplier.id = :supplierId", Service.class)
                    .setParameter("name", name)
                    .setParameter("supplierId", id)
                    .uniqueResult();
            return Optional.ofNullable(service);
        } catch (Exception e) {
            throw new ToursException("Error retrieving service by name and supplier ID: " + e.getMessage());
        }
    }
    
    // si la compra no tiene fecha, se le asigna la fecha actual?
    @Override
    @Transactional
    public Purchase createPurchase(String code, Route route, User user) throws ToursException {
        throw new ToursException("Constraint Violation");  // Porque falta la fecha
    }
    
    @Override
    @Transactional
    public Purchase createPurchase(String code, Date date, Route route, User user) throws ToursException {
        try {
            if (code == null || date == null || route == null || user == null) {
                throw new ToursException("No puede realizarse la compra");
            }
    
            Session session = sessionFactory.getCurrentSession();
    
            // Verificamos si ya existe una compra con ese código
            Purchase existing = session.createQuery(
                    "FROM Purchase p WHERE p.code = :code", Purchase.class)
                    .setParameter("code", code)
                    .uniqueResult();
    
            if (existing != null) {
                throw new ToursException("No puede realizarse la compra");
            }
    
            Purchase purchase = new Purchase(code, date, route, user);
            session.persist(purchase);
            return purchase;
        } catch (ToursException te) {
            throw te;
        } catch (Exception e) {
            throw new ToursException("Error al crear la compra: " + e.getMessage());
        }
    }

    @Override
    @Transactional
    public ItemService addItemToPurchase(Service service, int quantity, Purchase purchase) throws ToursException {
        try {
            if (service == null || purchase == null) {
                throw new ToursException("Service y Purchase no pueden ser null");
            }

            if (quantity <= 0) {
                throw new ToursException("La cantidad debe ser mayor a 0");
            }

            Session session = sessionFactory.getCurrentSession();

            // Crear el nuevo ItemService
            ItemService item = new ItemService(service, quantity, purchase);

            // Actualizar el precio total de la compra
            float additionalCost = service.getPrice() * quantity;
            purchase.setTotalPrice(purchase.getTotalPrice() + additionalCost);

            purchase.getItemServiceList().add(item);

            // Persistir el ItemService
            session.persist(item);

            return item;
        } catch (Exception e) {
            throw new ToursException("Error al agregar el servicio a la compra: " + e.getMessage());
        }
    }

    @Override
    public Optional<Purchase> getPurchaseByCode(String code) {
        try {
            Session session = sessionFactory.getCurrentSession();
    
            Purchase purchase = session.createQuery(
                    "SELECT p FROM Purchase p WHERE p.code = :code", Purchase.class)
                    .setParameter("code", code)
                    .getSingleResult();
    
            return Optional.of(purchase);
        } catch (NoResultException e) {
            return Optional.empty();
        }
    }
    
    
    /**
     * es parte de una expresión condicional ternaria, que es una forma compacta de escribir un if-else. Su estructura es:
     * condición ? valor_si_verdadero : valor_si_falso;
     */
    @Override
    @Transactional
    public void deletePurchase(Purchase purchase) throws ToursException {
        try {
            Session session = sessionFactory.getCurrentSession();

            Purchase managedPurchase = session.contains(purchase)
                ? purchase
                : session.merge(purchase); // aseguramos que está gestionado

            session.remove(managedPurchase);
        } catch (Exception e) {
            throw new ToursException("Error al eliminar la compra con código: " + purchase.getCode() + " error: " + e);
        }
    }

    @Override
    @Transactional
    public Review addReviewToPurchase(int rating, String comment, Purchase purchase) throws ToursException {
        try {
            Session session = sessionFactory.getCurrentSession();
    
            // Verificar si la compra ya tiene una reseña
            if (purchase.getReview() != null) {
                throw new ToursException("La compra ya tiene una reseña asociada.");
            }
    
            // Crear nueva Review
            Review review = new Review(rating, comment, purchase);
    
            // Asociar la review a la compra
            purchase.setReview(review);
    
            // Persistir la review
            session.persist(review);
    
            return review;
        } catch (Exception e) {
            throw new ToursException("Error al agregar la reseña a la compra: " + e.getMessage() + " error: " + e);
        }
    }
    
    @Override
    @Transactional
    public List<Purchase> getAllPurchasesOfUsername(String username) {
        Session session = sessionFactory.getCurrentSession();
        String hql = "FROM Purchase p WHERE p.user.username = :username";
        return session.createQuery(hql, Purchase.class)
                    .setParameter("username", username)
                    .getResultList();
    }

    /**
     * Consulta la base de datos para obtener todos los usuarios cuyo total de dinero gastado en compras (Purchase)
     * supera el valor mount que pasás como parámetro.
     */
    @Override
    @Transactional
    public List<User> getUserSpendingMoreThan(float mount) {
        Session session = sessionFactory.getCurrentSession();
        String hql = "SELECT u FROM User u JOIN u.purchaseList p GROUP BY u HAVING SUM(p.totalPrice) > :amount";
        return session.createQuery(hql, User.class)
                    .setParameter("amount", mount)
                    .getResultList();
    }

    
    /**
     * Obtener los proveedores (Suppliers) que más han estado involucrados en compras (Purchase) a través de los servicios (Service) que ofrecen.
     */
    @Override
    @Transactional
    public List<Supplier> getTopNSuppliersInPurchases(int n) {
        Session session = sessionFactory.getCurrentSession();
        String hql = """
            SELECT s
            FROM Supplier s
            JOIN s.services serv
            JOIN serv.itemServiceList iserv
            GROUP BY s
            ORDER BY COUNT(iserv.purchase) DESC
        """;

        return session.createQuery(hql, Supplier.class)
                    .setMaxResults(n)
                    .getResultList();
    }

    /**
     * Sumar los precios de todos los ItemService por cada Purchase y devolver las 10 compras con la suma más alta.
     */
    @Override
    @Transactional
    public List<Purchase> getTop10MoreExpensivePurchasesInServices() {
        Session session = sessionFactory.getCurrentSession();
        String hql = """
            SELECT p
            FROM Purchase p
            JOIN p.itemServiceList i
            GROUP BY p
            ORDER BY SUM(i.price) DESC
        """;

        return session.createQuery(hql, Purchase.class)
                    .setMaxResults(10)
                    .getResultList();
    }

    /** 
     * Debe devolver una lista con los 5 usuarios (User) que realizaron más 
     * compras (Purchase) en el sistema, ordenados de mayor a menor.
    */
    @Override
    @Transactional
    public List<User> getTop5UsersMorePurchases() {
        Session session = sessionFactory.getCurrentSession();
        String hql = """
            SELECT p.user
            FROM Purchase p
            GROUP BY p.user
            ORDER BY COUNT(p) DESC
        """;

        return session.createQuery(hql, User.class)
                    .setMaxResults(5)
                    .getResultList();
    }
    
    /**
     * debe devolver la cantidad total de compras (Purchase) realizadas entre dos fechas dadas (start y end).
     */
    @Override
    @Transactional
    public long getCountOfPurchasesBetweenDates(Date start, Date end) {
        Session session = sessionFactory.getCurrentSession();
        String hql = """
            SELECT COUNT(p)
            FROM Purchase p
            WHERE p.date BETWEEN :start AND :end
        """;
    
        return session.createQuery(hql, Long.class)
                      .setParameter("start", start)
                      .setParameter("end", end)
                      .getSingleResult();
    }
    
    /**
     * Debe devolver todas las rutas (Route) que incluyan la parada (Stop) dada como parámetro.
     */
    @Override
    @Transactional
    public List<Route> getRoutesWithStop(Stop stop) {
        Session session = sessionFactory.getCurrentSession();
        String hql = """
            SELECT r
            FROM Route r
            JOIN r.stops s
            WHERE s = :stop
        """;

        return session.createQuery(hql, Route.class)
                    .setParameter("stop", stop)
                    .getResultList();
    }

    
    /**
     * debe devolver la cantidad máxima de paradas (Stop) que tiene alguna ruta (Route).
     */
    @Override
    @Transactional
    public Integer getMaxStopOfRoutes() {
        Session session = sessionFactory.getCurrentSession();
        String hql = """
            SELECT MAX(SIZE(r.stops))
            FROM Route r
        """;
    
        return session.createQuery(hql, Integer.class)
                      .getSingleResult();
    }
    
    /**
     * debe devolver una lista de todas las rutas (Route) que nunca fueron vendidas, 
     * es decir, que no tienen ninguna compra (PurchasIntegere) asociada.
     */
    @Override
    @Transactional
    public List<Route> getRoutsNotSell() {
        Session session = sessionFactory.getCurrentSession();
        String hql = """
            SELECT r
            FROM Route r
            WHERE r NOT IN (
                SELECT DISTINCT p.route
                FROM Purchase p
            )
        """;
    
        return session.createQuery(hql, Route.class)
                      .getResultList();
    }
    
    /**
     * debe devolver las 3 rutas (Route) con mayor calificación promedio, considerando las calificaciones 
     * (Review) asociadas a las compras (Purchase) de cada ruta.
     */
    @Override
    @Transactional
    public List<Route> getTop3RoutesWithMaxRating() {
        Session session = sessionFactory.getCurrentSession();
        String hql = """
            SELECT p.route
            FROM Review r
            JOIN r.purchase p
            GROUP BY p.route
            ORDER BY AVG(r.rating) DESC
        """;
    
        return session.createQuery(hql, Route.class)
                      .setMaxResults(3)
                      .getResultList();
    }
    

    /**
     * Devuelve el servicio que fue más veces agregado en compras, es decir, 
     * el servicio que aparece más frecuentemente en los ítems de servicio (ItemService), 
     * sin importar cuántas unidades se compraron.
     */
    @Override
    @Transactional
    public Service getMostDemandedService() {
        Session session = sessionFactory.getCurrentSession();
        String hql = """
            SELECT iserv.service
            FROM ItemService iserv
            GROUP BY iserv.service
            ORDER BY COUNT(iserv) DESC
        """;
    
        return session.createQuery(hql, Service.class)
                      .setMaxResults(1)
                      .getSingleResult();
    }
    
    /**
     * devuelve una lista con todos los Service que nunca fueron utilizados en una compra, es decir, 
     * servicios que están registrados en el sistema pero nadie los ha adquirido aún
     */
    @Override
    @Transactional
    public List<Service> getServiceNoAddedToPurchases() {
        Session session = sessionFactory.getCurrentSession();
        String hql = """
            SELECT s
            FROM Service s
            WHERE s NOT IN (
                SELECT DISTINCT iserv.service
                FROM ItemService iserv
            )
        """;
    
        return session.createQuery(hql, Service.class)
                      .getResultList();
    }
    
    
    /**
     * Consulta todas las Review con rating == 1.
     * Por cada Review, obtiene: La Purchase asociada, la Route asociada a esa compra 
     * y la lista de TourGuideUser de esa ruta.
     * Añade todos los guías turísticos a un Set (para evitar duplicados).
     * Devuelve la lista de guías turísticos sin duplicados.
     * 
     * Consultar si esta bien 
     */
    @Override
    @Transactional
    public List<TourGuideUser> getTourGuidesWithRating1() {
        Session session = sessionFactory.getCurrentSession();
        
        // Usamos un set para evitar duplicados
        Set<TourGuideUser> tourGuidesWithRating1 = new HashSet<>();

        // Obtener todas las reviews con rating 1
        List<Review> reviews = session
            .createQuery("FROM Review r WHERE r.rating = 1", Review.class)
            .getResultList();

        for (Review review : reviews) {
            Route route = review.getPurchase().getRoute();
            List<TourGuideUser> tourGuides = route.getTourGuideList();
            tourGuidesWithRating1.addAll(tourGuides);
        }

        return new ArrayList<>(tourGuidesWithRating1);
    }
}