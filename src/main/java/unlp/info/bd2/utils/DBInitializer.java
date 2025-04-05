package unlp.info.bd2.utils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import unlp.info.bd2.model.*;
import unlp.info.bd2.services.ToursService;

import java.time.LocalDate;
import java.util.Calendar;
import java.util.Date;
import java.util.*;

public class DBInitializer {

    @Autowired
    ToursService toursService;

    @Transactional
    public void prepareDB() throws ToursException {
        
        Calendar cal1 = Calendar.getInstance();
        cal1.set(1980, Calendar.APRIL, 5);
        Date dob1 = cal1.getTime();
        cal1.set(2022, Calendar.SEPTEMBER, 21);
        Date doa1 = cal1.getTime();
        LocalDate today = LocalDate.now();

        // Suppliers
        Supplier supplier1 = this.toursService.createSupplier("ABC Tours", "12345");
        Supplier supplier2 = this.toursService.createSupplier("XYZ Travel Agency", "67890");
        Supplier supplier3 = this.toursService.createSupplier("Adventure Explorers", "54321");
        Supplier supplier4 = this.toursService.createSupplier("Sunset Adventures", "98765");
        Supplier supplier5 = this.toursService.createSupplier("Dream Destinations", "24680");

        // Servicios
        Service service1 = this.toursService.addServiceToSupplier("Gaucho Day Local Bakery", 3, "Tortas fritas y churros", supplier1);
        Service service2 = this.toursService.addServiceToSupplier("Souvenir mugs", 7, "Printed Mugs", supplier2);
        Service service3 = this.toursService.addServiceToSupplier("Delta Coffe", 4, "Coffee and tea shop", supplier3);
        Service service4 = this.toursService.addServiceToSupplier("Architectural Expedition Book", 25, "Most famous architectures with details", supplier4);
        Service service5 = this.toursService.addServiceToSupplier("Historical City Tour e-book", 40, "Learn about the city's history", supplier2);
        Service service6 = this.toursService.addServiceToSupplier("souvenir t-shirt", 10, "I love Buenos Aires t-shirt", supplier1);
        Service service7 = this.toursService.addServiceToSupplier("souvenir t-shirt", 10, "I love Argentina t-shirt", supplier1);
        Service service8 = this.toursService.addServiceToSupplier("souvenir photograph", 5, "Souvenir photo at tourist spot", supplier3);
        Service service9 = this.toursService.addServiceToSupplier("souvenir retrato", 5, "Another Souvenir", supplier3);

        // Stops
        Stop stop1 = this.toursService.createStop ("Diagonal Norte",	"Obelisco / Catedral Metropolitana / Casa Rosada / Museo del Bicentenario / Galería Güemes / Cabildo de Buenos Aires");
        Stop stop2 = this.toursService.createStop ("Avenida de Mayo",	"Casa Rosada / Museo del Bicentenario / Café Tortoni / Manzana de las Luces / Cabildo de Buenos Aires / Teatro Avenida");
        Stop stop3 = this.toursService.createStop ("Plaza del Congreso",	"Congreso de la Nación Argentina / Palacio Barolo / Pasaje Rivarola / Hotel Castelar / Monumento a los Dos Congresos");
        Stop stop4 = this.toursService.createStop ("Paseo de la Historieta",	"Escultura de Mafalda / Puente de la Mujer / Fragata Sarmiento / Basílica de San Francisco /  Plaza de Mayo");
        Stop stop5 = this.toursService.createStop ("Usina del Arte",	"Caminito / Museo Quinquela Martín / Museo del Cine / La Torre del Fantasma / Estadio Boca Juniors");
        Stop stop6 = this.toursService.createStop ("Puerto Madero",	"Puente de la Mujer / Buque Escuela Corbeta Uruguay / Museo del Humor / Centro Cultural Néstor Kirchner / Experiencias Náuticas");
        Stop stop7 = this.toursService.createStop ("Río de la Plata",	"Reserva Ecológica / Colección de Arte Amalia Lacroze de Fortabat / Circuitos Gastronómicos Peatonales / Centro Cultural Néstor Kirchner / Galerías Pacífico");
        Stop stop8 = this.toursService.createStop ("Museo Nacional de Bellas Artes",	"Floralis Genérica / Biblioteca Nacional / Basílica Nuestra Señora del Pilar / Centro Cultural Recoleta / Cementerio de la Recoleta");
        Stop stop9 = this.toursService.createStop ("Teatro Colón",	"Obelisco / Teatro Colón / Teatro Cervantes / Galerías Pacífico / Plaza San Martín");
        Stop stop10 = this.toursService.createStop ("Planetario",	"Planetario Galileo Galileo / Museo de Artes Plásticas Sívori / Paseo el Rosedal / Ecoparque Interactivo / Jardín Japonés");
        Stop stop11 = this.toursService.createStop ("Bosques de Palermo",	"Mezquita Centro Cultural Islámico Rey Fahd / Campo de Polo / Hipódromo Argentino / Rosedal / Planetario Galileo Galilei");
        Stop stop12 = this.toursService.createStop ("San Telmo",	"Plaza Dorrego / Iglesia San Pedro Telmo / Museo MAMBA / Museo MACBA / Mercado de San Telmo");
        Stop stop13 = this.toursService.createStop ("La Boca - Caminito",	"Usina del Arte / Museo Quinquela Martín / Teatro de la Ribera / Puente Transbordador / Estadio Boca Juniors");
        Stop stop14 = this.toursService.createStop ("Belgrano - Barrio Chino",	"Museo Enrique Larreta / Arco Inaugural Barrio Chino / Barrancas de Belgrano / Museo Sarmiento / Iglesia Inmaculada Concepción del Belgrano");
        Stop stop15 = this.toursService.createStop ("Recoleta",	"Centro Cultural Recoleta / Cementerio de la Recoleta / Basílica Nuestra Señora del Pilar / Museo Nacional de Bellas Artes / Floralis Genérica");
        Stop stop16 = this.toursService.createStop ("El Monumental (Estadio River Plate)",	"Museo Deportivo River / Jardín de Esculturas / Paseo de las Américas y Converse Skate Plaza / Museo Islas Malvinas e Islas del Atlántico Sur / Centro Cultural de la Memoria Haroldo Conti / Club Hípico Argentino");
        Stop stop17 = this.toursService.createStop ("Costanera Sur",	"Paseo de la Gloria / Monumento al Tango / Puente de la Mujer / Fragata Sarmiento / Parque Micaela Bastidas");
        Stop stop18 = this.toursService.createStop ("Av 9 de Julio",	"Peatonal Florida / Teatro Colón / Plaza San Martín / Galerías Pacífico / Museo de Arte Hispanoamericano Isaac Fernández Blanco");
        Stop stop19 = this.toursService.createStop ("Plaza Italia",	"Museo Evita / Jardín Japonés / Jardín Botánico Carlos Thays / Rosedal / Ecoparque Interactivo");
        Stop stop20 = this.toursService.createStop ("Delta",	"Delta / Tigre");

        // Users
        User user1 = this.toursService.createUser("user1", "1234", "Usuario Uno", "user1@gmail.com", dob1, "000111222333");
        User user2 = this.toursService.createUser("user2", "1234", "Usuario Dos", "user2@gmail.com", dob1, "000111222333");
        User user3 = this.toursService.createUser("user3", "1234", "Usuario Tres", "user3@gmail.com", dob1, "000111222333");
        User user4 = this.toursService.createUser("user4", "1234", "Usuario Cuatro", "user4@gmail.com", dob1, "000111222333");
        User user5 = this.toursService.createUser("user5", "1234", "Usuario Cinco", "user5@gmail.com", dob1, "000111222333"); // USUARIO SIN COMPRAS
        User user6 = this.toursService.createUser("user6", "1234", "Usuario Seis", "user6@gmail.com", dob1, "000111222333");
        User user7 = this.toursService.createUser("user7", "1234", "Usuario Siete", "user7@gmail.com", dob1, "000111222333");
        User user8 = this.toursService.createUser("user8", "1234", "Usuario Ocho", "user8@gmail.com", dob1, "000111222333");
        User user9 = this.toursService.createUser("user9", "1234", "Usuario Nueve", "user9@gmail.com", dob1, "000111222333");
        User user10 = this.toursService.createUser("user10", "1234", "Usuario Diez", "user10@gmail.com", dob1, "000111222333");

        // Driver Users
        DriverUser driverUser1 = this.toursService.createDriverUser("userD1", "1234", "Usuario Driver", "userd@gmail.com", dob1, "000111222444", "exp...");
        DriverUser driverUser2 = this.toursService.createDriverUser("userD2", "1234", "Usuario Driver2", "userd2@gmail.com", dob1, "000111222444", "exp...");
        DriverUser driverUser3 = this.toursService.createDriverUser("userD3", "1234", "Usuario Driver3", "userd3@gmail.com", dob1, "000111222444", "exp...");
        DriverUser driverUser4 = this.toursService.createDriverUser("userD4", "1234", "Usuario Driver4", "userd4@gmail.com", dob1, "000111222444", "exp...");

        // TourGuide Users
        TourGuideUser tourGuideUser1 = this.toursService.createTourGuideUser("userG1", "1234", "Usuario TourGuide", "userg@gmail.com", dob1, "000111222555", "edu...");
        TourGuideUser tourGuideUser2 = this.toursService.createTourGuideUser("userG2", "1234", "Usuario TourGuide2", "userg2@gmail.com", dob1, "000111222555", "edu...");
        TourGuideUser tourGuideUser3 = this.toursService.createTourGuideUser("userG3", "1234", "Usuario TourGuide3", "userg3@gmail.com", dob1, "000111222555", "edu...");
        TourGuideUser tourGuideUser4 = this.toursService.createTourGuideUser("userG4", "1234", "Usuario TourGuide4", "userg4@gmail.com", dob1, "000111222555", "edu...");

        // Routes
        List<Stop> stopsRoute1 = new ArrayList<Stop>(Arrays.asList(stop1, stop2, stop3, stop4, stop6, stop14, stop15, stop17, stop19));
        Route route1 = this.toursService.createRoute("City Tour", 200, 62,10, stopsRoute1);
        route1.addDriver(driverUser1);
        route1.addDriver(driverUser2);
        route1.addTourGuide(tourGuideUser1);

        List<Stop> stopsRoute2 = new ArrayList<Stop>(Arrays.asList(stop2, stop3, stop7, stop9, stop18, stop19));
        Route route2 = this.toursService.createRoute("Historical Adventure", 300, 68,10, stopsRoute2);
        route2.addDriver(driverUser2);
        route2.addDriver(driverUser3);
        route2.addTourGuide(tourGuideUser2);
        route2.addTourGuide(tourGuideUser3);

        List<Stop> stopsRoute3 = new ArrayList<Stop>(Arrays.asList(stop5, stop6, stop8,  stop9, stop14, stop15));
        Route route3 = this.toursService.createRoute("Architectural Expedition", 500, 55,15, stopsRoute3);
        route3.addDriver(driverUser3);
        route3.addTourGuide(tourGuideUser3);

        List<Stop> stopsRoute4 = new ArrayList<Stop>(Arrays.asList(stop7, stop11, stop20));
        Route route4 = this.toursService.createRoute("Delta Tour", 800, 75,10, stopsRoute4);
        route4.addDriver(driverUser4);
        route4.addTourGuide(tourGuideUser1);
        route4.addTourGuide(tourGuideUser4);

        List<Stop> stopsRoute5 = new ArrayList<>(Arrays.asList(stop1, stop2));
        Route route5 = this.toursService.createRoute("Ruta vacia", 900, 20, 5, stopsRoute5);

        // Purchases
        Purchase purchase1 = this.toursService.createPurchase("P001", java.sql.Date.valueOf(today.minusDays(30)), route1, user1);
        Purchase purchase2 = this.toursService.createPurchase("P002", java.sql.Date.valueOf(today.minusDays(30)), route2, user2);
        Purchase purchase3 = this.toursService.createPurchase("P003", java.sql.Date.valueOf(today.minusDays(28)), route3, user3);
        Purchase purchase4 = this.toursService.createPurchase("P004", java.sql.Date.valueOf(today.minusDays(27)), route4, user4);
        Purchase purchase5 = this.toursService.createPurchase("P005", java.sql.Date.valueOf(today.minusDays(27)), route1, user1);
        Purchase purchase6 = this.toursService.createPurchase("P006", java.sql.Date.valueOf(today.minusDays(21)), route2, user2);
        Purchase purchase7 = this.toursService.createPurchase("P007", java.sql.Date.valueOf(today.minusDays(20)), route3, user6);
        Purchase purchase8 = this.toursService.createPurchase("P008", java.sql.Date.valueOf(today.minusDays(20)), route4, user7);
        Purchase purchase9 = this.toursService.createPurchase("P009", java.sql.Date.valueOf(today.minusDays(20)), route1, user1);
        Purchase purchase10 = this.toursService.createPurchase("P010", java.sql.Date.valueOf(today.minusDays(19)), route2, user2);
        Purchase purchase11 = this.toursService.createPurchase("P011", java.sql.Date.valueOf(today.minusDays(18)), route1, user1);
        Purchase purchase12 = this.toursService.createPurchase("P012", java.sql.Date.valueOf(today.minusDays(15)), route2, user8);
        Purchase purchase13 = this.toursService.createPurchase("P013", java.sql.Date.valueOf(today.minusDays(14)), route3, user9);
        Purchase purchase14 = this.toursService.createPurchase("P014", java.sql.Date.valueOf(today.minusDays(12)), route4, user10);
        Purchase purchase15 = this.toursService.createPurchase("P015", java.sql.Date.valueOf(today.minusDays(12)), route1, user1);
        Purchase purchase16 = this.toursService.createPurchase("P016", java.sql.Date.valueOf(today.minusDays(11)), route2, user10);
        Purchase purchase18 = this.toursService.createPurchase("P018", java.sql.Date.valueOf(today.minusDays(10)), route3, user7);
        Purchase purchase19 = this.toursService.createPurchase("P019", java.sql.Date.valueOf(today.minusDays(10)), route1, user1);
        Purchase purchase17 = this.toursService.createPurchase("P017", java.sql.Date.valueOf(today.minusDays(8)), route3, user6);
        Purchase purchase20 = this.toursService.createPurchase("P020", java.sql.Date.valueOf(today.minusDays(7)), route2, user2);

        ItemService itemService1 = this.toursService.addItemToPurchase(service1, 1, purchase4);
        ItemService itemService2 = this.toursService.addItemToPurchase(service5, 2, purchase4);
        ItemService itemService3 = this.toursService.addItemToPurchase(service3, 1, purchase1);
        ItemService itemService4 = this.toursService.addItemToPurchase(service8, 2, purchase2);
        ItemService itemService5 = this.toursService.addItemToPurchase(service7, 1, purchase10);
        ItemService itemService6 = this.toursService.addItemToPurchase(service6, 2, purchase9);
        ItemService itemService7 = this.toursService.addItemToPurchase(service1, 1, purchase8);
        ItemService itemService8 = this.toursService.addItemToPurchase(service2, 1, purchase7);
        ItemService itemService9 = this.toursService.addItemToPurchase(service1, 2, purchase1);
        ItemService itemService10 = this.toursService.addItemToPurchase(service6, 2, purchase20);
        ItemService itemService11 = this.toursService.addItemToPurchase(service3, 1, purchase19);
        ItemService itemService12 = this.toursService.addItemToPurchase(service8, 3, purchase18);
        ItemService itemService13 = this.toursService.addItemToPurchase(service6, 1, purchase17);
        ItemService itemService14 = this.toursService.addItemToPurchase(service6, 3, purchase17);
        ItemService itemService15 = this.toursService.addItemToPurchase(service1, 1, purchase16);
        ItemService itemService16 = this.toursService.addItemToPurchase(service2, 1, purchase15);
        ItemService itemService17 = this.toursService.addItemToPurchase(service1, 1, purchase14);
        ItemService itemService18 = this.toursService.addItemToPurchase(service5, 2, purchase14);
        ItemService itemService19 = this.toursService.addItemToPurchase(service3, 1, purchase13);
        ItemService itemService20 = this.toursService.addItemToPurchase(service8, 3, purchase12);
        ItemService itemService21 = this.toursService.addItemToPurchase(service7, 1, purchase10);
        ItemService itemService22 = this.toursService.addItemToPurchase(service6, 2, purchase11);
        ItemService itemService23 = this.toursService.addItemToPurchase(service1, 1, purchase1);
        ItemService itemService24 = this.toursService.addItemToPurchase(service6, 1, purchase5);

        Review review1 = this.toursService.addReviewToPurchase(4, "Excelente recorrido, me encantó la experiencia.", purchase1);
        Review review2 = this.toursService.addReviewToPurchase(5, "Increíble tour, lo recomendaría a cualquiera.", purchase2);
        Review review3 = this.toursService.addReviewToPurchase(3, "Buen recorrido, pero podría mejorar la organización.", purchase3);
        Review review4 = this.toursService.addReviewToPurchase(2, "No quedé satisfecho con el tour, esperaba más.", purchase4);
        Review review5 = this.toursService.addReviewToPurchase(5, "¡Una experiencia inolvidable! Definitivamente volvería a hacerlo.", purchase5);
        Review review6 = this.toursService.addReviewToPurchase(4, "Me encantó el recorrido, el guía fue muy amable.", purchase6);
        Review review7 = this.toursService.addReviewToPurchase(3, "Estuvo bien, pero no cumplió completamente mis expectativas.", purchase7);
        Review review8 = this.toursService.addReviewToPurchase(1, "Terrible experiencia, no recomendaría este tour a nadie.", purchase8);
        Review review9 = this.toursService.addReviewToPurchase(4, "No esperaba menos", purchase9);
        Review review10 = this.toursService.addReviewToPurchase(5, "Lo haría 10 veces más", purchase12);
        Review review11 = this.toursService.addReviewToPurchase(3, "Nada del otro mundo.", purchase13);
        Review review12 = this.toursService.addReviewToPurchase(2, "Varios inconvenientes en el viaje, incluyendo cambio de horario de salida.", purchase14);
        Review review13 = this.toursService.addReviewToPurchase(5, "Volvería a hacerlo.", purchase15);
        Review review14 = this.toursService.addReviewToPurchase(3, "Me encantó el tour, pero el guía no era ductil con el portugués.", purchase16);
        Review review15 = this.toursService.addReviewToPurchase(3, "Estuvo bien, aunque esperaba más.", purchase17);
        Review review16 = this.toursService.addReviewToPurchase(1, "Muy caro para lo que se brinda.", purchase18);
    }
}
