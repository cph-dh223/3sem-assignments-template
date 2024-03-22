package hotel;

import hotel.config.ApplicationConfig;
import hotel.config.HibernateConfig;
import hotel.config.Routs;
import hotel.ressources.Hotel;
import hotel.ressources.Role;
import hotel.ressources.Room;
import hotel.ressources.User;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;


import com.fasterxml.jackson.databind.ObjectMapper;

public class main {
    public static void main(String[] args) {
        startServer(7070);
    }

    public static void startServer(int port) {
        ObjectMapper om = new ObjectMapper();
        EntityManagerFactory emf = HibernateConfig.getEntityManagerFactory();
        ApplicationConfig applicationConfig = ApplicationConfig.getInstance(emf);
        applicationConfig
                .initiateServer()
                .startServer(port)
                .setExceptionHandling()
                .checkSecurityRoles()
                .setRoute(Routs.getResourses(emf))
                .setRoute(Routs.getSecurityRoutes(emf))
                .setRoute(Routs.securedRoutes(emf))
            ;
    }
    

    private static void setup(EntityManagerFactory emf){
        try (EntityManager em = emf.createEntityManager()) {
            em.getTransaction().begin();
            em.createQuery("DELETE FROM Room r").executeUpdate();
            em.createQuery("DELETE FROM Hotel h").executeUpdate();
            em.createQuery("DELETE FROM User u").executeUpdate();
            em.createQuery("DELETE FROM Role r").executeUpdate();
            
            Hotel h1 = new Hotel("h1", "Street 1");
            Hotel h2 = new Hotel("h2", "Street 2");
            Hotel h3 = new Hotel("h3", "Street 3");
            Room r1_1 = new Room(1, 100f);
            Room r1_2 = new Room(2, 1000f);
            Room r2_1 = new Room(1, 100f);
            Room r2_2 = new Room(2, 1000f);
            Room r3_1 = new Room(1, 100f);
            Room r3_2 = new Room(2, 1000f);

            h1.addRoom(r1_1);
            h1.addRoom(r1_2);
            h2.addRoom(r2_1);
            h2.addRoom(r2_2);
            h3.addRoom(r3_1);
            h3.addRoom(r3_2);
                
            em.persist(h1);
            em.persist(h2);
            em.persist(h3);
            em.persist(r1_1);
            em.persist(r1_2);
            em.persist(r2_1);
            em.persist(r2_2);
            em.persist(r3_1);
            em.persist(r3_2);

            User admin = new User("admin", "1234");
            Role adminRole = new Role("admin");
            admin.addRole(adminRole);

            em.persist(admin);
            em.persist(adminRole);

            em.getTransaction().commit();
        }
    }

}
