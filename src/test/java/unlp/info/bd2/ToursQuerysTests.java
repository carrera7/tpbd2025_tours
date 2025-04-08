package unlp.info.bd2;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.support.AnnotationConfigContextLoader;
import org.springframework.transaction.annotation.Transactional;

import unlp.info.bd2.config.AppConfig;
import unlp.info.bd2.config.HibernateConfiguration;
import unlp.info.bd2.model.Route;
import unlp.info.bd2.model.Stop;
import unlp.info.bd2.services.ToursService;
import unlp.info.bd2.utils.DBInitializer;
import unlp.info.bd2.utils.ToursException;






@SpringBootTest
@ContextConfiguration(classes = {HibernateConfiguration.class, AppConfig.class, DBInitializer.class}, loader = AnnotationConfigContextLoader.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@Transactional
@Rollback(true)
public class ToursQuerysTests {
    
    @Autowired
    DBInitializer initializer;

    @Autowired
    ToursService service;

    SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");

  @BeforeAll
    public void prepareDB() throws Exception {
        this.initializer.prepareDB();
    }

    @Test
    void init() {

    }
 
    @Test
    void getAllPurchasesOfUsernameTest() throws ToursException {
        List<Purchase> purchases1 = this.service.getAllPurchasesOfUsername("user1");
        assertEquals(6, purchases1.size());
        this.assertListEquality(purchases1.stream().map(Purchase::getCode).collect(Collectors.toList()), Arrays.asList("P001", "P005","P009", "P011", "P015", "P019"));
        List<Purchase> purchases2 = this.service.getAllPurchasesOfUsername("user3");
        assertEquals(1, purchases2.size());
        this.assertListEquality(purchases2.stream().map(Purchase::getCode).collect(Collectors.toList()), Arrays.asList("P003"));
        
        List<Purchase> purchases3 = this.service.getAllPurchasesOfUsername("user5");
        assertEquals(0, purchases3.size());
    }


    @Test
    void getUserSpendingMoreThanTest() throws ToursException {
        List<User> users1 = this.service.getUserSpendingMoreThan(500);
        assertEquals(6, users1.size());
        this.assertListEquality(users1.stream().map(User::getUsername).collect(Collectors.toList()), Arrays.asList("user3", "user4", "user6", "user7", "user9", "user10"));
        List<User> users2 = this.service.getUserSpendingMoreThan(800);
        assertEquals(3, users2.size());
        this.assertListEquality(users2.stream().map(User::getUsername).collect(Collectors.toList()), Arrays.asList("user4", "user7", "user10"));
        List<User> users3 = this.service.getUserSpendingMoreThan(1000);
        assertEquals(0, users3.size());
    }

/* 
//     @Test
//     void getTopNSuppliersInPurchasesTest() throws ToursException {
//         List<Supplier> suppliers1 = this.service.getTopNSuppliersInPurchases(3);
//         assertEquals(3, suppliers1.size());
//         this.assertListEquality(suppliers1.stream().map(Supplier::getAuthorizationNumber).collect(Collectors.toList()), Arrays.asList("12345", "54321", "67890"));
//         List<Supplier> suppliers2 = this.service.getTopNSuppliersInPurchases(1);
//         assertEquals(1, suppliers2.size());
//         this.assertListEquality(suppliers2.stream().map(Supplier::getAuthorizationNumber).collect(Collectors.toList()), Arrays.asList("12345"));
//     }

//     @Test
//     void getTop10MoreExpensivePurchasesInServicesTest() throws ToursException {
//         List<Purchase> purchases = this.service.getTop10MoreExpensivePurchasesInServices();
//         assertEquals(10, purchases.size());
//         this.assertListEquality(purchases.stream().map(Purchase::getCode).collect(Collectors.toList()), Arrays.asList("P004", "P007", "P008", "P010", "P012", "P013", "P014", "P017", "P018", "P020"));
//     }

//     @Test
//     void getTop5UsersMorePurchasesTest() throws ToursException {
//         List<User> usersMorePurchases = this.service.getTop5UsersMorePurchases();
//         assertEquals(5, usersMorePurchases.size());
//         this.assertListEquality(usersMorePurchases.stream().map(User::getUsername).collect(Collectors.toList()), Arrays.asList("user1", "user2", "user6", "user7", "user10"));
//     }

//     @Test
//     void getCountOfPurchasesBetweenDatesTest() throws ToursException {
//         LocalDate today = LocalDate.now();
//         long countOfPurchasesBetweenDates1 = this.service.getCountOfPurchasesBetweenDates(Date.valueOf(today.minusDays(25)), Date.valueOf(today.minusDays(15)));
//         assertEquals(7, countOfPurchasesBetweenDates1);
//         long countOfPurchasesBetweenDates2 = this.service.getCountOfPurchasesBetweenDates(Date.valueOf(today.minusDays(13)), Date.valueOf(today.minusDays(0)));
//         assertEquals(7, countOfPurchasesBetweenDates2);
//         long countOfPurchasesBetweenDates3 = this.service.getCountOfPurchasesBetweenDates(Date.valueOf(today.minusDays(26)), Date.valueOf(today.minusDays(22)));
//         assertEquals(0, countOfPurchasesBetweenDates3);
//     }

*/
    @Test
    void getRoutesWithStopTest() throws ToursException {
        Stop stop1 = this.service.getStopByNameStart("Diagonal Norte").get(0);
        Stop stop2 = this.service.getStopByNameStart("Teatro Colón").get(0);
        Stop stop3 = this.service.getStopByNameStart("La Boca").get(0);

        List<Route> routes1 = this.service.getRoutesWithStop(stop1);
        assertEquals(2, routes1.size());
        this.assertListEquality(routes1.stream().map(Route::getName).collect(Collectors.toList()), List.of("City Tour", "Ruta vacia"));
        List<Route> routes2 = this.service.getRoutesWithStop(stop2);
        assertEquals(2, routes2.size());
        this.assertListEquality(routes2.stream().map(Route::getName).collect(Collectors.toList()), List.of("Historical Adventure", "Architectural Expedition"));
        List<Route> routes3 = this.service.getRoutesWithStop(stop3);
        assertEquals(0, routes3.size());
    }


    @Test
    void getMaxStopOfRoutesTest() throws ToursException {
        Integer maxStopOfRoutes = this.service.getMaxStopOfRoutes();
        assertEquals(9, maxStopOfRoutes);
    }


//     @Test
//     void getRoutsNotSellTest() throws ToursException {
//         List<Route> routsNotSell = this.service.getRoutsNotSell();
//         assertEquals(1, routsNotSell.size());
//         this.assertListEquality(routsNotSell.stream().map(Route::getName).collect(Collectors.toList()), List.of("Ruta vacia"));
//     }

//     @Test
//     void getTop3RoutesWithMaxRatingTest() throws ToursException {
//         List<Route> routesWithMaxRating = this.service.getTop3RoutesWithMaxRating();
//         assertEquals(3, routesWithMaxRating.size());
//         this.assertListEquality(routesWithMaxRating.stream().map(Route::getName).collect(Collectors.toList()), List.of("City Tour", "Historical Adventure", "Architectural Expedition"));
//     }

//     @Test
//     void getMostDemandedServiceTest() throws ToursException {
//         Service mostDemandedService = this.service.getMostDemandedService();
//         assertEquals("souvenir t-shirt", mostDemandedService.getName());
//         assertEquals("I love Buenos Aires t-shirt", mostDemandedService.getDescription());
//     }

//     @Test
//     void getServiceNoAddedToPurchasesTest() throws ToursException {
//         List<Service> serviceNoAddedToPurchases = this.service.getServiceNoAddedToPurchases();
//         assertEquals(2, serviceNoAddedToPurchases.size());
//         this.assertListEquality(serviceNoAddedToPurchases.stream().map(Service::getName).collect(Collectors.toList()), List.of("Architectural Expedition Book", "souvenir retrato"));
//     }

//     @Test
//     void getTourGuidesWithRating1Test() throws ToursException {
//         List<TourGuideUser> tourGuidesWithRating1 = this.service.getTourGuidesWithRating1();
//         assertEquals(3, tourGuidesWithRating1.size());
//         this.assertListEquality(tourGuidesWithRating1.stream().map(TourGuideUser::getUsername).collect(Collectors.toList()), List.of("userG1", "userG3", "userG4"));
//     }

    private <T> void assertListEquality(List<T> list1, List<T> list2) {
        if (list1.size() != list2.size()) {
            Assert.fail("Lists have different size");
        }

        for (T objectInList1 : list1) {
            if (!list2.contains(objectInList1)) {
                Assert.fail(objectInList1 + " is not present in list2");
            }
        }
    }

}