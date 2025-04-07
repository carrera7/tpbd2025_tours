package unlp.info.bd2.services;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
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

    @PersistenceContext
    private EntityManager entityManager;

    private ToursRepository tourRepository;

    public ToursServiceImpl(ToursRepository repository){
        this.tourRepository= repository;
    }

    @Override
    @Transactional
    public User createUser(String username, String password, String fullName, String email, Date birthdate,
            String phoneNumber) throws ToursException {
        try {
            User user = new User(username, password, fullName, email, birthdate, phoneNumber);
            entityManager.persist(user);
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
            entityManager.persist(driverUser);
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
            entityManager.persist(tourGuideUser);
            return tourGuideUser;
        } catch (Exception e) {
            throw new ToursException("Error al crear y persistir TourGuideUser: " + e.getMessage());
        }
    }

    @Override
    @Transactional
    public Optional<User> getUserById(Long id) throws ToursException {
        try {
            User user = entityManager.find(User.class, id); // Hibernate resolverá si es DriverUser o TourGuideUser
            return Optional.ofNullable(user);
        } catch (Exception e) {
            throw new ToursException("Error al obtener el usuario con ID: " + id);
        }
    }

    @Override
    public Optional<User> getUserByUsername(String username) throws ToursException {
        try {
            TypedQuery<User> query = entityManager.createQuery(
                "SELECT u FROM User u WHERE u.username = :username", User.class);
            query.setParameter("username", username);
    
            List<User> result = query.getResultList();
    
            return result.isEmpty() ? Optional.empty() : Optional.of(result.get(0));
        } catch (Exception e) {
            throw new ToursException("Error al obtener usuario por nombre de usuario: " + username);
        }
    }

    /*o mejor utiliso un EntityTransaction  */
    @Override
    public User updateUser(User user) throws ToursException {
        User existingUser = entityManager.find(User.class, user.getId());
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

    }

    @Override
    @Transactional
    public void deleteUser(User user) throws ToursException {
        try {
            // Nos aseguramos de que el usuario esté gestionado por el EntityManager
            User managedUser = entityManager.contains(user) ? user : entityManager.merge(user);
            
            entityManager.remove(managedUser);
        } catch (Exception e) {
            throw new ToursException("Error al eliminar el usuario con ID: " + user.getId());
        }
    }

    @Override
    public Stop createStop(String name, String description) throws ToursException {
        Optional<Stop> existingStop = tourRepository.findStopByName(name);
        if (existingStop.isPresent()) {
            throw new ToursException("Ya existe una parada con ese nombre");
        }
        Stop stop = new Stop();
        stop.setName(name);
        stop.setDescription(description);
        System.out.println("Guardando la parada: " + stop);
        return tourRepository.save(stop);
    }


    @Override
    public List<Stop> getStopByNameStart(String name) {
        return tourRepository.findStopByNameStartingWith(name);
    }

    @Override
    public Route createRoute(String name, float price, float totalKm, int maxNumberOfUsers, List<Stop> stops)
            throws ToursException {
        if (name == null || name.isEmpty()) {
        throw new ToursException("El nombre no puede estar vacío");
        }

        try {
            Route route = new Route(name, price, totalKm, maxNumberOfUsers, stops);
            route.setStops(stops); // si no lo hace el constructor
            return tourRepository.save(route);
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
            // Buscar el DriverUser por username
            TypedQuery<DriverUser> query = entityManager.createQuery(
                "SELECT d FROM DriverUser d WHERE d.username = :username", DriverUser.class);
            query.setParameter("username", username);
            List<DriverUser> drivers = query.getResultList();
    
            if (drivers.isEmpty()) {
                throw new ToursException("No se encontró un conductor con username: " + username);
            }
    
            DriverUser driver = drivers.get(0);
    
            // Buscar la Route por ID
            Route route = entityManager.find(Route.class, idRoute);
            if (route == null) {
                throw new ToursException("No se encontró la ruta con ID: " + idRoute);
            }
    
            // Asociar el conductor a la ruta
            route.addDriver(driver);  // agrega a la lista de la ruta
            driver.getRoutes().add(route); // agrega también en el lado del DriverUser (opcional, para mantener consistencia bidireccional)
    
            // merge por si alguno no está en el contexto de persistencia
            entityManager.merge(route);
            entityManager.merge(driver);
    
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
            // Buscar guía turístico por username
            TypedQuery<TourGuideUser> guideQuery = entityManager.createQuery(
                "SELECT g FROM TourGuideUser g WHERE g.username = :username", TourGuideUser.class);
            guideQuery.setParameter("username", username);
            List<TourGuideUser> guides = guideQuery.getResultList();

            if (guides.isEmpty()) {
                throw new ToursException("Guía turístico no encontrado con username: " + username);
            }

            TourGuideUser guide = guides.get(0);

            // Buscar la ruta por ID
            Route route = entityManager.find(Route.class, idRoute);
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
            entityManager.merge(route);
            entityManager.merge(guide);

        } catch (Exception e) {
            throw new ToursException("Error al asignar guía turístico con username: " + username + " a la ruta ID: " + idRoute);
        }
    }

    @Override
    @Transactional
    public Supplier createSupplier(String businessName, String authorizationNumber) throws ToursException {
        try {
            Supplier supplier = new Supplier();
            supplier.setBusinessName(businessName);
            supplier.setAuthorizationNumber(authorizationNumber);

            entityManager.persist(supplier);
            return supplier;
        } catch (Exception e) {
            throw new ToursException("Error al crear el proveedor con nombre: " + businessName + e);
        }
    }

    @Override
    @Transactional
    public Service addServiceToSupplier(String name, float price, String description, Supplier supplier) throws ToursException {
        try {
            // Crear nuevo servicio
            Service service = new Service();
            service.setName(name);
            service.setPrice(price); // Esta validación ya está implementada en el setter
            service.setDescription(description);
            service.setSupplier(supplier);
    
            // Persistir el servicio
            entityManager.persist(service);
    
            // Actualizar la lista de servicios del proveedor (si no está null)
            if (supplier.getServices() != null) {
                supplier.getServices().add(service);
            }
    
            return service;
        } catch (Exception e) {
            throw new ToursException("Error al agregar el servicio '" + name + "' al proveedor con ID: " + supplier.getId() + e);
        }
    }
    

    @Override
    @Transactional
    public Service updateServicePriceById(Long id, float newPrice) throws ToursException {
        try {
            // Buscar el servicio por ID
            Service service = entityManager.find(Service.class, id);
            
            // Verificar si existe
            if (service == null) {
                throw new ToursException("No se encontró un servicio con ID: " + id);
            }
    
            // Actualizar el precio (valida internamente en el setter)
            service.setPrice(newPrice);
    
            // El cambio se sincroniza automáticamente por el contexto de persistencia
            return service;
    
        } catch (IllegalArgumentException e) {
            throw new ToursException("Precio inválido: " + e.getMessage());
        } catch (Exception e) {
            throw new ToursException("Error al actualizar el precio del servicio con ID: " + id + e);
        }
    }
    

    @Override
    public Optional<Supplier> getSupplierById(Long id) {
        Supplier supplier = entityManager.find(Supplier.class, id);
        return Optional.ofNullable(supplier);
    }
    

    @Override
    public Optional<Supplier> getSupplierByAuthorizationNumber(String authorizationNumber) {
        try {
            Supplier supplier = entityManager
                .createQuery("SELECT s FROM Supplier s WHERE s.authorizationNumber = :authNumber", Supplier.class)
                .setParameter("authNumber", authorizationNumber)
                .getSingleResult();
            return Optional.of(supplier);
        } catch (NoResultException e) {
            return Optional.empty();
        }
    }


    @Override
    public Optional<Service> getServiceByNameAndSupplierId(String name, Long id) throws ToursException {
        try {
            Service service = entityManager
                .createQuery("SELECT s FROM Service s WHERE s.name = :name AND s.supplier.id = :supplierId", Service.class)
                .setParameter("name", name)
                .setParameter("supplierId", id)
                .getSingleResult();
            return Optional.of(service);
        } catch (NoResultException e) {
            return Optional.empty();
        } catch (Exception e) {
            throw new ToursException("Error retrieving service by name and supplier ID" + e);
        }
    }
    

    @Override
    @Transactional
    public Purchase createPurchase(String code, Route route, User user) throws ToursException {
        try {
            Purchase purchase = new Purchase(code, route, user);
            entityManager.persist(purchase);
            return purchase;
        } catch (Exception e) {
            throw new ToursException("Error while creating a new purchase" + e);
        }
    }
    

    @Override
    @Transactional
    public Purchase createPurchase(String code, Date date, Route route, User user) throws ToursException {
        try {
            // Verificamos si ya existe una compra con ese código (opcional)
            TypedQuery<Purchase> query = entityManager.createQuery(
                "SELECT p FROM Purchase p WHERE p.code = :code", Purchase.class);
            query.setParameter("code", code);
    
            List<Purchase> existing = query.getResultList();
            if (!existing.isEmpty()) {
                throw new ToursException("Ya existe una compra con el código: " + code);
            }
    
            Purchase purchase = new Purchase(code, date, route, user);
            entityManager.persist(purchase);
            return purchase;
        } catch (ToursException te) {
            throw te;
        } catch (Exception e) {
            throw new ToursException("Error al crear la compra" + e);
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
    
            // Crear el nuevo ItemService
            ItemService item = new ItemService(service, quantity, purchase);
    
            // Actualizar el precio total de la compra
            float additionalCost = service.getPrice() * quantity;
            purchase.setTotalPrice(purchase.getTotalPrice() + additionalCost);
    
            // Persistir el ItemService (como está en cascada, no es obligatorio persistir purchase de nuevo)
            entityManager.persist(item);
    
            return item;
        } catch (Exception e) {
            throw new ToursException("Error al agregar el servicio a la compra" + e);
        }
    }
    

    @Override
    public Optional<Purchase> getPurchaseByCode(String code) {
        try {
            Purchase purchase = entityManager.createQuery(
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
            Purchase managedPurchase = entityManager.contains(purchase) 
                ? purchase 
                : entityManager.merge(purchase); // aseguramos que está gestionado
    
            entityManager.remove(managedPurchase);
        } catch (Exception e) {
            throw new ToursException("Error al eliminar la compra con código: " + purchase.getCode() + "error:" + e);
        }
    }
    

    @Override
    @Transactional
    public Review addReviewToPurchase(int rating, String comment, Purchase purchase) throws ToursException {
        try {
            // Verificar si la compra ya tiene una reseña
            if (purchase.getReview() != null) {
                throw new ToursException("La compra ya tiene una reseña asociada.");
            }
    
            // Crear nueva Review
            Review review = new Review(rating, comment, purchase);
    
            // Asociar la review a la compra
            purchase.setReview(review);
    
            // Persistir la review
            entityManager.persist(review);
    
            return review;
        } catch (Exception e) {
            throw new ToursException("Error al agregar la reseña a la compra: " + e.getMessage() + "error" + e);
        }
    }
    
    

    @Override
    @Transactional
    public List<Purchase> getAllPurchasesOfUsername(String username) {
        String queryStr = "SELECT p FROM Purchase p WHERE p.user.username = :username";
        TypedQuery<Purchase> query = entityManager.createQuery(queryStr, Purchase.class);
        query.setParameter("username", username);
        return query.getResultList();
    }
    
    /**
     * Consulta la base de datos para obtener todos los usuarios cuyo total de dinero gastado en compras (Purchase) 
     * supera el valor mount que pasás como parámetro.
     */
    @Override
    public List<User> getUserSpendingMoreThan(float mount) {
        String jpql = "SELECT u FROM User u JOIN u.purchaseList p GROUP BY u HAVING SUM(p.totalPrice) > :amount";
        return entityManager.createQuery(jpql, User.class)
                            .setParameter("amount", mount)
                            .getResultList();
    }
    
    /**
     * Obtener los proveedores (Suppliers) que más han estado involucrados en compras (Purchase) a través de los servicios (Service) que ofrecen.
     */
    @Override
    public List<Supplier> getTopNSuppliersInPurchases(int n) {
        String jpql = """
            SELECT s
            FROM Supplier s
            JOIN s.services serv
            JOIN serv.itemServiceList iserv
            JOIN iserv.purchase p
            GROUP BY s
            ORDER BY COUNT(DISTINCT p.id) DESC
        """;
    
        return entityManager.createQuery(jpql, Supplier.class)
                            .setMaxResults(n)
                            .getResultList();
    }
    
    
    /**
     * sumar los precios de todos los ItemService por cada Purchase y devolver las 10 compras con la suma más alta.
     */
    @Override
    public List<Purchase> getTop10MoreExpensivePurchasesInServices() {
        String jpql = """
            SELECT p
            FROM Purchase p
            JOIN p.itemServiceList i
            GROUP BY p
            ORDER BY SUM(i.quantity * i.service.price) DESC
        """;
    
        return entityManager.createQuery(jpql, Purchase.class)
                            .setMaxResults(10)
                            .getResultList();
    }    
    
    /** 
     * Debe devolver una lista con los 5 usuarios (User) que realizaron más 
     * compras (Purchase) en el sistema, ordenados de mayor a menor.
    */
    @Override
    public List<User> getTop5UsersMorePurchases() {
        String jpql = """
            SELECT p.user
            FROM Purchase p
            WHERE p.user IS NOT NULL
            GROUP BY p.user
            ORDER BY COUNT(p.id) DESC
        """;
    
        return entityManager.createQuery(jpql, User.class)
                            .setMaxResults(5)
                            .getResultList();
    }
    
    
    /**
     * debe devolver la cantidad total de compras (Purchase) realizadas entre dos fechas dadas (start y end).
     */
    @Override
    public long getCountOfPurchasesBetweenDates(Date start, Date end) {
        String jpql = """
            SELECT COUNT(p)
            FROM Purchase p
            WHERE p.date BETWEEN :start AND :end
        """;
    
        return entityManager.createQuery(jpql, Long.class)
                            .setParameter("start", start)
                            .setParameter("end", end)
                            .getSingleResult();
    }
    
    /**
     * Debe devolver todas las rutas (Route) que incluyan la parada (Stop) dada como parámetro.
     */
    @Override
    public List<Route> getRoutesWithStop(Stop stop) {
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
    
    /**
     * debe devolver la cantidad máxima de paradas (Stop) que tiene alguna ruta (Route).
     */
    @Override
    public Integer getMaxStopOfRoutes() {
        String jpql = """
            SELECT MAX(SIZE(r.stops))
            FROM Route r
        """;
    
        return entityManager.createQuery(jpql, Integer.class)
                            .getSingleResult();
    }
    
    /**
     * debe devolver una lista de todas las rutas (Route) que nunca fueron vendidas, 
     * es decir, que no tienen ninguna compra (PurchasIntegere) asociada.
     */
    @Override
    public List<Route> getRoutsNotSell() {
        String jpql = """
            SELECT r
            FROM Route r
            WHERE r NOT IN (
                SELECT DISTINCT p.route
                FROM Purchase p
            )
        """;
    
        return entityManager.createQuery(jpql, Route.class)
                            .getResultList();
    }
    
    /**
     * debe devolver las 3 rutas (Route) con mayor calificación promedio, considerando las calificaciones 
     * (Review) asociadas a las compras (Purchase) de cada ruta.
     */
    @Override
    public List<Route> getTop3RoutesWithMaxRating() {
        String jpql = """
            SELECT p.route
            FROM Review r
            JOIN r.purchase p
            GROUP BY p.route
            ORDER BY AVG(r.rating) DESC
        """;
    
        return entityManager.createQuery(jpql, Route.class)
                            .setMaxResults(3)
                            .getResultList();
    }
    
    /**
     * Devuelve el servicio que fue más veces agregado en compras, es decir, 
     * el servicio que aparece más frecuentemente en los ítems de servicio (ItemService), 
     * sin importar cuántas unidades se compraron.
     */
    @Override
    public Service getMostDemandedService() {
        String jpql = """
            SELECT iserv.service
            FROM ItemService iserv
            GROUP BY iserv.service
            ORDER BY COUNT(iserv) DESC
        """;
    
        return entityManager.createQuery(jpql, Service.class)
                            .setMaxResults(1)
                            .getSingleResult();
    }
    
    /**
     * devuelve una lista con todos los Service que nunca fueron utilizados en una compra, es decir, 
     * servicios que están registrados en el sistema pero nadie los ha adquirido aún
     */
    @Override
    public List<Service> getServiceNoAddedToPurchases() {
        String jpql = """
            SELECT s
            FROM Service s
            WHERE s NOT IN (
                SELECT DISTINCT iserv.service
                FROM ItemService iserv
            )
        """;
    
        return entityManager.createQuery(jpql, Service.class).getResultList();
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
    public List<TourGuideUser> getTourGuidesWithRating1() {
        // Usamos un set para evitar duplicados
        Set<TourGuideUser> tourGuidesWithRating1 = new HashSet<>();

        // Obtener todas las reviews con rating 1
        List<Review> reviews = entityManager
            .createQuery("SELECT r FROM Review r WHERE r.rating = 1", Review.class)
            .getResultList();

        for (Review review : reviews) {
            Route route = review.getPurchase().getRoute();
            List<TourGuideUser> tourGuides = route.getTourGuideList();
            tourGuidesWithRating1.addAll(tourGuides);
        }

        return new ArrayList<>(tourGuidesWithRating1);
    }


}