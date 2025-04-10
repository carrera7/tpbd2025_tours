package unlp.info.bd2;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.stream.Collectors;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.support.AnnotationConfigContextLoader;
import org.springframework.transaction.annotation.Transactional;

import junit.framework.Assert;
import unlp.info.bd2.config.AppConfig;
import unlp.info.bd2.config.HibernateConfiguration;
import unlp.info.bd2.model.Route;
import unlp.info.bd2.model.Service;
import unlp.info.bd2.model.Stop;
import unlp.info.bd2.model.TourGuideUser;
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
    void getRoutesWithStopTest() throws ToursException {
        Stop stop1 = this.service.getStopByNameStart("Diagonal Norte").get(0);
        Stop stop2 = this.service.getStopByNameStart("Teatro Colón").get(0);
        Stop stop3 = this.service.getStopByNameStart("La Boca").get(0);

        List<Route> routes1 = this.service.getRoutesWithStop(stop1);
        assertEquals(2, routes1.size());
        assertEquals(routes1.stream().map(Route::getName).collect(Collectors.toList()), List.of("City Tour", "Ruta vacia"));
        //this.assertListEquality(routes1.stream().map(Route::getName).collect(Collectors.toList()), List.of("City Tour", "Ruta vacia"));
        List<Route> routes2 = this.service.getRoutesWithStop(stop2);
        assertEquals(2, routes2.size());
        assertEquals(routes2.stream().map(Route::getName).collect(Collectors.toList()), List.of("Historical Adventure", "Architectural Expedition"));
        List<Route> routes3 = this.service.getRoutesWithStop(stop3);
        assertEquals(0, routes3.size());
    }


    @Test
    void getMaxStopOfRoutesTest() throws ToursException {
        Integer maxStopOfRoutes = this.service.getMaxStopOfRoutes();
        assertEquals(9, maxStopOfRoutes);
    }


    @Test
    void getRoutsNotSellTest() throws ToursException {
        List<Route> routsNotSell = this.service.getRoutsNotSell();
        assertEquals(1, routsNotSell.size());
        // Transforma la lista de rutas en una lista de nombres de rutas
        // y compara con la lista esperada
        this.assertListEquality(routsNotSell.stream().map(Route::getName).collect(Collectors.toList()), List.of("Ruta vacia"));
    }

    @Test
    void getTop3RoutesWithMaxRatingTest() throws ToursException {
        List<Route> routesWithMaxRating = this.service.getTop3RoutesWithMaxRating();
        assertEquals(3, routesWithMaxRating.size());
        // Transforma la lista de rutas en una lista de nombres de rutas
        // y compara con la lista esperada
        this.assertListEquality(routesWithMaxRating.stream().map(Route::getName).collect(Collectors.toList()), List.of("City Tour", "Historical Adventure", "Architectural Expedition"));
    }

    // @Test
    // void getMostDemandedServiceTest() throws ToursException {
    //     Service mostDemandedService = this.service.getMostDemandedService();
    //     assertEquals("souvenir t-shirt", mostDemandedService.getName());
    //     assertEquals("I love Buenos Aires t-shirt", mostDemandedService.getDescription());
    // }

    @Test
    void getServiceNoAddedToPurchasesTest() throws ToursException {
        List<Service> serviceNoAddedToPurchases = this.service.getServiceNoAddedToPurchases();
        assertEquals(2, serviceNoAddedToPurchases.size());
        this.assertListEquality(serviceNoAddedToPurchases.stream().map(Service::getName).collect(Collectors.toList()), List.of("Architectural Expedition Book", "souvenir retrato"));
    }

    @Test
    void getTourGuidesWithRating1Test() throws ToursException {
        List<TourGuideUser> tourGuidesWithRating1 = this.service.getTourGuidesWithRating1();
        assertEquals(3, tourGuidesWithRating1.size());
        this.assertListEquality(tourGuidesWithRating1.stream().map(TourGuideUser::getUsername).collect(Collectors.toList()), List.of("userG1", "userG3", "userG4"));
    }

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