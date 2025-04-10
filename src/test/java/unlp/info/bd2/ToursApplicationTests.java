package unlp.info.bd2;


import java.util.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.support.AnnotationConfigContextLoader;
import org.springframework.transaction.annotation.Transactional;

import unlp.info.bd2.config.AppConfig;
import unlp.info.bd2.config.HibernateConfiguration;
import unlp.info.bd2.model.*;
import unlp.info.bd2.services.ToursService;
import unlp.info.bd2.utils.ToursException;
import static org.junit.jupiter.api.Assertions.*;




@SpringBootTest
@ContextConfiguration(classes = {HibernateConfiguration.class, AppConfig.class}, loader = AnnotationConfigContextLoader.class)
@ExtendWith(SpringExtension.class)
@Transactional
@Rollback(true)
class ToursApplicationTests {

	@Autowired
	private ToursService toursService; 
	private Date dob1;
	private Date dob2;
	private Date dpri;
	private Date dyes;

	@BeforeEach
	public void setUp(){
		Calendar cal1 = Calendar.getInstance();
		cal1.set(1980, Calendar.APRIL, 5);
		this.dob1 = cal1.getTime();
		cal1.set(1992, Calendar.SEPTEMBER, 16);
		this.dob2 = cal1.getTime();
		cal1.set(2022, Calendar.SEPTEMBER, 21);
		this.dpri = cal1.getTime();
		cal1.set(2024,Calendar.MARCH, 20);
		this.dyes = cal1.getTime();
	}


	@Test
	void createAndGetUserTest()  throws ToursException {
		User user1 = this.toursService.createUser("user1", "1234", "Usuario Uno", "user1@gmail.com", dob1, "000111222333");
		assertNotNull(user1.getId());
		assertEquals("user1", user1.getUsername());
		assertEquals("Usuario Uno", user1.getName());
		assertEquals("user1@gmail.com", user1.getEmail());
		assertEquals(dob1, user1.getBirthdate());
		DriverUser driverUser1 = this.toursService.createDriverUser("userD", "1234", "Usuario Driver", "userd@gmail.com", dob2, "000111222444", "exp...");
		assertNotNull(driverUser1.getId());
		TourGuideUser tourGuideUser1 = this.toursService.createTourGuideUser("userG", "1234", "Usuario TourGuide", "userg@gmail.com", dob2, "000111222555", "edu...");
		assertNotNull(tourGuideUser1.getId());

		Optional<User> opUserFromDB = this.toursService.getUserById(user1.getId());
		assertTrue(opUserFromDB.isPresent());
		User user = opUserFromDB.get();
		assertEquals(user1.getId(), user.getId());
		assertEquals("user1", user.getUsername());
		assertEquals("Usuario Uno", user.getName());
		assertEquals("user1@gmail.com", user.getEmail());
		assertTrue(user.getPurchaseList().isEmpty());

		Optional<User> opUserFromDB2 = this.toursService.getUserByUsername("userD");
		assertTrue(opUserFromDB2.isPresent());
		DriverUser driverUser = (DriverUser) opUserFromDB2.get();
		assertEquals(driverUser.getId(), driverUser1.getId());
		assertEquals(driverUser.getExpedient(), "exp...");

		assertThrows(ToursException.class, () -> this.toursService.createUser("userD", "1234", "Otro usuario", "otromail@gmail.com", dob1, "000111222999"), "Constraint Violation");
	}

	@Test
	void updateUserTest()  throws ToursException {
		User user1 = this.toursService.createUser("user1", "1234", "Usuario Uno", "user1@gmail.com", dob1, "000111222333");
		DriverUser driverUser = this.toursService.createDriverUser("userD", "1234", "Usuario Driver", "userd@gmail.com", dob2, "000111222444", "exp...");

		assertEquals("000111222333", user1.getPhoneNumber());
		user1.setPhoneNumber("000000000000");
		user1 = this.toursService.updateUser(user1);
		assertNotEquals("000111222333", user1.getPhoneNumber());
		assertEquals("000000000000", user1.getPhoneNumber());

		assertEquals("exp...", driverUser.getExpedient());
		driverUser.setExpedient("nuevo expediente");
		driverUser = (DriverUser) this.toursService.updateUser(driverUser);
		assertNotEquals("exp...", driverUser.getExpedient());
		assertEquals("nuevo expediente", driverUser.getExpedient());

		user1.setUsername("user2");
		this.toursService.updateUser(user1);
		Optional<User> opUserFromDB = this.toursService.getUserByUsername("user2");
		assertTrue(opUserFromDB.isEmpty());
		Optional<User> opUnmodifiedUserFromDB = this.toursService.getUserByUsername("user1");
		assertTrue(opUnmodifiedUserFromDB.isPresent());
		User unmodifiedUserFromDB = opUnmodifiedUserFromDB.get();
		assertEquals(unmodifiedUserFromDB.getId(), user1.getId());
	}

	@Test
	void createAndGetRoutesAndStopsTest() throws ToursException {
		Stop stop1 = this.toursService.createStop("Estadio Monumental", "Estadio de River Plate");
		assertNotNull(stop1.getId());
		Stop stop2 = this.toursService.createStop("Estadio La Bombonera", "Estadio de Boca Junions");
		Stop stop3 = this.toursService.createStop("Estadio Libertadores de America", "Estadio de Independiente");
	
	 
	
		List<Stop> stopList1 = this.toursService.getStopByNameStart("Estadio L");
		assertEquals(2, stopList1.size());
		List<Stop> stopList2 = this.toursService.getStopByNameStart("Estadio");
		assertEquals(3, stopList2.size());
		List<Stop> stopList3 = this.toursService.getStopByNameStart("Monumental");
		assertEquals(0,  stopList3.size());
	
	 

		List<Stop> stops1 = new ArrayList<Stop>(Arrays.asList(stop1, stop2, stop3));
		List<Stop> stops2 = new ArrayList<Stop>(Arrays.asList(stop3, stop2));
	
	
		Route route1 = this.toursService.createRoute("Estadios", 20000, 55.5f, 3, stops1);
		assertNotNull(route1.getId()); 

		Optional<Route> opRoute1 = this.toursService.getRouteById(route1.getId());
		assertTrue(opRoute1.isPresent());
		Route route3 = opRoute1.get();
		assertEquals("Estadios", route3.getName());
		assertEquals(3, route3.getStops().size());
		

		Route route2 = this.toursService.createRoute("Estadios 2", 15000, 30f, 2, stops2);
		List<Route> listRoutes1 = this.toursService.getRoutesBelowPrice(50000f);
		assertEquals(2, listRoutes1.size());
		List<Route> listRoutes2 = this.toursService.getRoutesBelowPrice(17000f);
		assertEquals(1, listRoutes2.size());
		Route routeFromList = listRoutes2.get(0);
		assertEquals("Estadios 2", routeFromList.getName());
	}


	@Test
	void assignWorkersToRoutesTest() throws ToursException {
		Stop stop1 = this.toursService.createStop("Estadio Monumental", "Estadio de River Plate");
		Stop stop2 = this.toursService.createStop("Estadio La Bombonera", "Estadio de Boca Junions");
		Stop stop3 = this.toursService.createStop("Estadio Libertadores de America", "Estadio de Independiente");
		List<Stop> stops1 = new ArrayList<Stop>(Arrays.asList(stop1,stop2, stop3));
		Route route1 = this.toursService.createRoute("Estadios", 20000, 55.5f, 3, stops1);
		DriverUser driverUser1 = this.toursService.createDriverUser("userD1", "1234", "Usuario Driver", "userd1@gmail.com", dob2, "000111222444", "exp...");
		DriverUser driverUser2 = this.toursService.createDriverUser("userD2", "1234", "Usuario Driver", "userd2@gmail.com", dob2, "000111222444", "exp...");
		TourGuideUser tourGuideUser1 = this.toursService.createTourGuideUser("userG1", "1234", "Usuario TourGuide", "userg1@gmail.com", dob2, "000111222555", "edu...");

		this.toursService.assignDriverByUsername(driverUser1.getUsername(), route1.getId());
		this.toursService.assignDriverByUsername(driverUser2.getUsername(), route1.getId());
		this.toursService.assignTourGuideByUsername(tourGuideUser1.getUsername(), route1.getId());
		Optional<Route> optionalRoute = this.toursService.getRouteById(route1.getId());
		assertTrue(optionalRoute.isPresent());
		Route route = optionalRoute.get();
		assertEquals(2, route.getDriverList().size());
		assertEquals(1, route.getTourGuideList().size());
		assertEquals("userG1", route.getTourGuideList().get(0).getUsername());

		assertThrows(ToursException.class, () -> this.toursService.assignTourGuideByUsername("user_no_existente", tourGuideUser1.getId()) , "No pudo realizarse la asignación");
		assertThrows(ToursException.class, () -> this.toursService.assignDriverByUsername(driverUser1.getUsername(), 1000000L) , "No pudo realizarse la asignación");
	}


	@Test
	void createAndGetSupplierAndService() throws ToursException {
		Supplier supplier1 = this.toursService.createSupplier("Supplier1", "000111");
		assertNotNull(supplier1.getId());
		assertEquals("Supplier1" , supplier1.getBusinessName());
		Service service1 = this.toursService.addServiceToSupplier("Servicio1", 500f, "primer servicio", supplier1);
		assertNotNull(service1.getId());
		assertEquals("Servicio1", service1.getName());
		assertEquals(supplier1.getId(), service1.getSupplier().getId());
		assertEquals(supplier1.getServices().get(0).getId(), service1.getId());

		Optional<Supplier> optionalSupplier1 = this.toursService.getSupplierById(supplier1.getId());
		assertTrue(optionalSupplier1.isPresent());
		Supplier supplier2 = optionalSupplier1.get();
		assertEquals(supplier1.getId(), supplier2.getId());
		assertEquals("000111", supplier2.getAuthorizationNumber());
		Optional<Supplier> optionalSupplier2 = this.toursService.getSupplierByAuthorizationNumber("000111");
		assertTrue(optionalSupplier2.isPresent());
		Supplier supplier3 = optionalSupplier2.get();
		assertEquals(supplier1.getId(), supplier3.getId());
		Optional<Supplier> optionalSupplier3 = this.toursService.getSupplierByAuthorizationNumber("001111");
		assertFalse(optionalSupplier3.isPresent());

		Optional<Service> optionalService1 = this.toursService.getServiceByNameAndSupplierId("Servicio1", supplier1.getId());
		assertTrue(optionalService1.isPresent());
		Service service2 = optionalService1.get();
		assertEquals(service1.getId(), service2.getId());
		assertEquals(service1.getDescription(),"primer servicio");
		Optional<Service> optionalService2 = this.toursService.getServiceByNameAndSupplierId("Servicio2", supplier1.getId());
		assertFalse(optionalService2.isPresent());

		assertThrows(ToursException.class, () -> this.toursService.createSupplier("Supplier2", "000111"), "Constraint Violation");
	}

	@Test
	void updateServicePriceTest() throws ToursException {
		Supplier supplier1 = this.toursService.createSupplier("Supplier1", "000111");
		Service service1 = this.toursService.addServiceToSupplier("Servicio1", 500f, "primer servicio", supplier1);
		assertEquals(500f, service1.getPrice());

		Service service2 = this.toursService.updateServicePriceById(service1.getId(), 600f);
		assertEquals(600f, service2.getPrice());

		assertThrows(ToursException.class, () -> this.toursService.updateServicePriceById(100000L, 500f), "No existe el producto");
	}


	@Test
	void createAndGetPurchaseTest() throws ToursException {
		User user1 = this.toursService.createUser("user1", "1234", "Usuario Uno", "user1@gmail.com", dob1, "000111222333");
		Stop stop1 = this.toursService.createStop("Estadio Monumental", "Estadio de River Plate");
		Stop stop2 = this.toursService.createStop("Estadio La Bombonera", "Estadio de Boca Junions");
		Stop stop3 = this.toursService.createStop("Estadio Libertadores de America", "Estadio de Independiente");
		List<Stop> stops1 = new ArrayList<Stop>(Arrays.asList(stop1,stop2, stop3));
		Route route1 = this.toursService.createRoute("Estadios", 20000, 55.5f, 2, stops1);
		Supplier supplier1 = this.toursService.createSupplier("Supplier1", "000111");
		Supplier supplier2 = this.toursService.createSupplier("Supplier2", "000222");
		Service service1 = this.toursService.addServiceToSupplier("Servicio1", 500f, "primer servicio", supplier1);
		Service service2 = this.toursService.addServiceToSupplier("Servicio2", 1000f, "segundo servicio", supplier2);

		Purchase purchase1 = this.toursService.createPurchase("100", dyes, route1, user1);
		assertNotNull(purchase1.getId());
		assertEquals(route1.getPrice(), purchase1.getTotalPrice());
		assertEquals(1, user1.getPurchaseList().size());

		ItemService itemService1 = this.toursService.addItemToPurchase(service1, 1, purchase1);
		assertNotNull(itemService1.getId());
		assertEquals(supplier1.getId(), itemService1.getService().getId());
		assertEquals(purchase1.getId(), itemService1.getPurchase().getId());
		ItemService itemService2 = this.toursService.addItemToPurchase(service2, 2, purchase1);

		Optional<Purchase> optionalPurchase1 = this.toursService.getPurchaseByCode("100");
		assertTrue(optionalPurchase1.isPresent());
		Purchase purchase3 = optionalPurchase1.get();
		assertEquals(purchase1.getId(), purchase3.getId());
		assertDoesNotThrow(() -> purchase3.getItemServiceList().size());
		assertEquals(2, purchase3.getItemServiceList().size());
		assertEquals(22500, purchase3.getTotalPrice());

		this.toursService.createPurchase("101", dyes, route1, user1);

		// assertThrows(ToursException.class, () -> this.toursService.createPurchase("200", dyes, route1, user1), "No puede realizarse la compra");
		assertThrows(ToursException.class, () -> this.toursService.createPurchase("100", route1, user1), "Constraint Violation");
	}
 
	@Test
	void removePurchaseAndItems() throws ToursException {
		User user1 = this.toursService.createUser("user1", "1234", "Usuario Uno", "user1@gmail.com", dob1, "000111222333");
		Stop stop1 = this.toursService.createStop("Estadio Monumental", "Estadio de River Plate");
		Stop stop2 = this.toursService.createStop("Estadio La Bombonera", "Estadio de Boca Junions");
		Stop stop3 = this.toursService.createStop("Estadio Libertadores de America", "Estadio de Independiente");
		List<Stop> stops1 = new ArrayList<Stop>(Arrays.asList(stop1,stop2, stop3));
		Route route1 = this.toursService.createRoute("Estadios", 20000, 55.5f, 2, stops1);
		Supplier supplier1 = this.toursService.createSupplier("Supplier1", "000111");
		Service service1 = this.toursService.addServiceToSupplier("Servicio1", 500f, "primer servicio", supplier1);
		Service service2 = this.toursService.addServiceToSupplier("Servicio2", 1000f, "segundo servicio", supplier1);
		Purchase purchase1 = this.toursService.createPurchase("100", dyes, route1, user1);
		ItemService itemService1 = this.toursService.addItemToPurchase(service1, 1, purchase1);
		ItemService itemService2 = this.toursService.addItemToPurchase(service2, 2, purchase1);
		assertEquals(1, service1.getItemServiceList().size());

		this.toursService.deletePurchase(purchase1);
		Optional<Purchase> purchase = this.toursService.getPurchaseByCode("100");
		assertFalse(purchase.isPresent());
	}

	@Test
	void addReviewToPurchaseTest() throws ToursException {
		User user1 = this.toursService.createUser("user1", "1234", "Usuario Uno", "user1@gmail.com", dob1, "000111222333");
		Stop stop1 = this.toursService.createStop("Estadio Monumental", "Estadio de River Plate");
		Stop stop2 = this.toursService.createStop("Estadio La Bombonera", "Estadio de Boca Junions");
		Stop stop3 = this.toursService.createStop("Estadio Libertadores de America", "Estadio de Independiente");
		List<Stop> stops1 = new ArrayList<Stop>(Arrays.asList(stop1,stop2, stop3));
		Route route1 = this.toursService.createRoute("Estadios", 20000, 55.5f, 2, stops1);
		Purchase purchase1 = this.toursService.createPurchase("100", dyes, route1, user1);

		Review review = this.toursService.addReviewToPurchase(5, "un comentario", purchase1);
		Optional<Purchase> optionalPurchase = this.toursService.getPurchaseByCode("100");
		assertTrue(optionalPurchase.isPresent());
		Purchase purchase = optionalPurchase.get();
		assertNotNull(review.getId());
		assertNotNull(purchase.getReview());
		assertEquals(purchase.getId(), review.getPurchase().getId());
	}
 
	@Test
	void deleteUserTest() throws ToursException {
		User user1 = this.toursService.createUser("user1", "1234", "Usuario Uno", "user1@gmail.com", dob1, "000111222333");

		assertTrue(user1.isActive());
		this.toursService.deleteUser(user1);
		assertTrue(this.toursService.getUserByUsername("user1").isEmpty());

		User user2 = this.toursService.createUser("user2", "1234", "Usuario Dos", "user2@gmail.com", dob2, "000111222334");
		Stop stop1 = this.toursService.createStop("Estadio Monumental", "Estadio de River Plate");
		Stop stop2 = this.toursService.createStop("Estadio La Bombonera", "Estadio de Boca Junions");
		Stop stop3 = this.toursService.createStop("Estadio Libertadores de America", "Estadio de Independiente");
		List<Stop> stops1 = new ArrayList<Stop>(Arrays.asList(stop1, stop2, stop3));
		Route route1 = this.toursService.createRoute("Estadios", 20000, 55.5f, 2, stops1);
		Purchase purchase1 = this.toursService.createPurchase("100", dyes, route1, user2);
		assertTrue(user2.isActive());
		this.toursService.deleteUser(user2);
		Optional<User> optionalUser2 = this.toursService.getUserByUsername("user2");
		assertTrue(optionalUser2.isPresent());
		User user2b = optionalUser2.get();
		assertFalse(user2b.isActive());

		assertThrows(ToursException.class, () -> this.toursService.deleteUser(user2b), "El usuario se encuentra desactivado");

		TourGuideUser tourGuideUser = this.toursService.createTourGuideUser("userG", "1234|", "Usuario TourGuide", "userg@gmail.com", dob2, "000111222555", "edu...");
		this.toursService.assignTourGuideByUsername(tourGuideUser.getUsername(), route1.getId());
		assertTrue(tourGuideUser.isActive());
		assertThrows(ToursException.class, () -> this.toursService.deleteUser(tourGuideUser), "El usuario no puede ser desactivado");
	}

	@Test
    void testStopFields() {
        Stop stop = new Stop();
	}

} 
